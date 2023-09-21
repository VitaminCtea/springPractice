package spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import spring.annotationConfigurationInterBeanDependencies.TransferServiceImpl;
import spring.converterFactory.ConverterFactoryTest;
import spring.differenceBetweenResourceAndAutowire.Autowire;
import spring.differenceBetweenResourceAndAutowire.CustomResource;
import spring.differenceBetweenResourceAndAutowire.Description;
import spring.differenceBetweenResourceAndAutowire.Test;
import spring.factoryMethodComponent.TestBean;
import spring.randomEnum.Enums;
import spring.resource.ResourceDependencyInjection;
import spring.testPrototype.SingletonBean;
import spring.testPrototype.TestPrototype;
import spring.testRef.event.EmailService;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.PrintStream;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.file.NoSuchFileException;
import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.regex.MatchResult;
import java.util.stream.IntStream;

enum Lala {
    SPRING, SUMMER
}

/**
 * Hello world!
 *
 */
public class App {
    private enum Direction { North, East, South, West }

    // 在基类中定义常规的clone方法，如果有必要，可以在子类重新覆写基类的clone方法
    private static abstract class MapSite<T extends MapSite<T>> implements Cloneable {
        public abstract void enter();
        public T clone() {
            T result = null;
            try {
                result = (T) super.clone();
            } catch (CloneNotSupportedException e) { e.printStackTrace(); }
            return result;
        }
    }

    private static class Room extends MapSite<Room> {
        private int roomNumber;
        private TreeMap<Direction, MapSite<?>> sides = new TreeMap<>(comparator());

        public Room() {}
        public Room(int roomNo) { this(roomNo, /* 默认按照Direction声明的顺序进行排序 */comparator()); }
        public Room(int roomNo, Comparator<Direction> directionComparator) {
            this.roomNumber = roomNo;
            this.sides = new TreeMap<>(directionComparator);
        }

        public void setSide(Direction direction, MapSite<?> site) { sides.put(direction, site); }

        public int getRoomNumber() { return roomNumber; }
        public Map<Direction, MapSite<?>> getSides() { return new HashMap<>(sides); }

        @Override public void enter() { System.out.println("Enter the Room."); }

        @Override public String toString() { return this.getClass().getSimpleName(); }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Room)) return false;
            Room room = (Room) o;
            return roomNumber == room.roomNumber;
        }

        @Override public int hashCode() { return Objects.hash(roomNumber, sides); }
        @Override public Room clone() {
            Room room = super.clone();
            room.sides = (TreeMap<Direction, MapSite<?>>) sides.clone();
            room.sides.clear();
            return room;
        }

        public Room clone(int roomNo) {
            Room room = clone();
            room.setRoomNumber(roomNo);
            return room;
        }

        public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }
        private static <T extends Direction> Comparator<T> comparator() { return Comparator.comparingInt(Enum::ordinal); }
    }

    private static class EnchantedRoom extends Room {
        private String incantations;

        public EnchantedRoom() {}
        public EnchantedRoom(int roomNo, String incantations) {
            super(roomNo);
            this.incantations = incantations;
        }

        public void enter(String incantations) {
            if (!this.incantations.equals(incantations))
                System.out.println("The spell to enter the room is null or incorrect, please re-enter.");
            System.out.println("The door has been opened according to the spell");
        }
    }

    private static class Wall extends MapSite<Wall> {
        public Wall() {}
        @Override public void enter() {}
        @Override public String toString() { return "Wall."; }
    }

    private static class Door extends MapSite<Door> {
        private Room from;
        private Room to;

        public Door() {}
        public Door(Room from, Room to) {
            this.from = from;
            this.to = to;
        }

        @Override public void enter() { System.out.println("Enter the Door."); }
        @Override public String toString() { return "Door."; }

        public void setFrom(Room from) { this.from = from; }
        public void setTo(Room to) { this.to = to; }

        public Door clone(Room from, Room to) {
            Door door = clone();
            door.setFrom(from);
            door.setTo(to);
            return door;
        }
    }

    private static class DoorNeedingSpell extends Door {
        public DoorNeedingSpell() {}
        public DoorNeedingSpell(Room from, Room to) { super(from, to); }
        @Override public void enter() { System.out.println("Enter the DoorNeedingSpell."); }
    }

    public static class MazeSingletonManager {
        private static MazeSingletonManager manager;
        private static final Map<Class<? extends Maze>, Maze> registrar = new HashMap<>();
        private MazeSingletonManager() {}

        public void register(Class<? extends Maze> cls) {
            try {
                registrar.put(cls, cls.getDeclaredConstructor().newInstance());
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        public <T extends Maze> T lookup(Class<? extends T> cls) { return (T) registrar.get(cls); }
        public static MazeSingletonManager instance() {
            if (manager == null) manager = new MazeSingletonManager();
            return manager;
        }
    }

    // 迷宫属于一个游戏，可以使其变为单例(单例也可以有子类，但最好的办法是在基类中使用一个单例注册器(Map)，以便在需要时候进行获取)
    public static class Maze {
        private final ArrayList<Room> rooms = new ArrayList<>();

        static { registerWithMazeSingletonManager(Maze.class); }

        public void addRoom(Room room) { rooms.add(room); }
        public Room getRoom(int roomNo) { return rooms.get(roomNo - 1); }
        public List<Room> getRooms() { return Collections.unmodifiableList(rooms); }
        public boolean hasRoom(int roomNo) { return rooms.contains(new Room(roomNo)); }

        public static void registerWithMazeSingletonManager(Class<? extends Maze> cls) { MazeSingletonManager.instance().register(cls); }
    }

    public static class SpecialMaze extends Maze {
        public static int initialize;
        static { registerWithMazeSingletonManager(SpecialMaze.class); }
    }

    public static class MazeFacade {
        private static boolean isVisit;
        public static <T extends Maze> T getMaze(Class<T> cls) {
            /*
            * 由于内部类不会自动初始化，非静态内部类进行初始化时先需要实例外部类之后才可进行初始化，而静态内部类则不需要
            * 要想自动触发静态内部类的初始化需要在子类中放置一个静态变量，这样当访问这个子类的静态变量时就会触发类的加载(把class文件加载到内存)
            * 而又根据Java继承的规则，先初始化父类的静态字段和静态代码块，然后在初始化子类的静态字段和静态代码块，这样就实现了子类和父类一起进行初始化了
            * 需要注意的是，要想自动触发静态内部类的初始化，则修饰符权限必须大于private(public -> protected -> default -> private)，否则会报错
            * */
            if (!isVisit) {
                isVisit = true;
                int init = SpecialMaze.initialize;  // 自动向MazeSingletonManager类注册Maze
            }
            return MazeSingletonManager.instance().lookup(cls);
        }
    }

    // 抽象工厂模式
    private static class MazeFactory {
        protected final Wall wall = new Wall();
        protected final Room room = new Room();
        protected final Door door = new Door();

        public Maze makeMaze() { return MazeFacade.getMaze(SpecialMaze.class); }
        public Wall makeWall() { return wall.clone(); }
        public Room makeRoom(int roomNo) {
            Room r = room.clone();
            r.setRoomNumber(roomNo);
            return r;
        }
        public Door makeDoor(Room from, Room to) {
            Door d = door.clone();
            d.setFrom(from);
            d.setTo(to);
            return d;
        }
    }

    private static class EnchantedMazeFactory extends MazeFactory {
        private int roomNo = 1;
        @Override public Room makeRoom(int roomNo) {
            if (this.roomNo == roomNo) return new EnchantedRoom(roomNo, castSpell());
            return this.room.clone(roomNo);
        }
        @Override public Door makeDoor(Room from, Room to) {
            if (this.roomNo++ == 1) return new DoorNeedingSpell(from, to);
            return this.door.clone(from, to);
        }
        private String castSpell() { return "天王盖地虎"; }
    }

    private static class MazePrototypeFactory extends MazeFactory {
        private final Maze prototypeMaze;
        private final Room prototypeRoom;
        private final Wall prototypeWall;
        private final Door prototypeDoor;

        public MazePrototypeFactory(Maze prototypeMaze, Room prototypeRoom, Wall prototypeWall, Door prototypeDoor) {
            this.prototypeMaze = prototypeMaze;
            this.prototypeRoom = prototypeRoom;
            this.prototypeWall = prototypeWall;
            this.prototypeDoor = prototypeDoor;
        }

        public MazePrototypeFactory cloneMazePrototypeFactory() {
            return new MazePrototypeFactory(
                    prototypeMaze,
                    prototypeRoom.clone(),
                    prototypeWall.clone(),
                    prototypeDoor.clone()
            );
        }

        @Override public Wall makeWall() { return prototypeWall.clone(); }
        @Override public Room makeRoom(int roomNo) { return prototypeRoom.clone(roomNo); }
        @Override public Door makeDoor(Room from, Room to) { return prototypeDoor.clone(from, to); }
    }

    private static abstract class MazeBuilder {
        protected Room room = new Room();
        protected Door door = new Door();
        protected Wall wall = new Wall();

        public void buildMaze() {}
        public void buildRoom(int roomNo) {}
        public void buildDoor(int fromRoomNo, int toRoomNo) {}
        public abstract Maze getMaze();
    }

    private static class StandardMazeBuilder extends MazeBuilder {
        private Room lastRoom;
        private Maze currentMaze;

        @Override public void buildMaze() { currentMaze = MazeFacade.getMaze(SpecialMaze.class); }
        @Override public void buildRoom(int roomNo) {
            if (!currentMaze.hasRoom(roomNo)) {
                Room room = this.room.clone(roomNo);
                currentMaze.addRoom(room);
                for (Direction direction: Direction.values())
                    room.setSide(direction, this.wall.clone());
            }
        }
        @Override public void buildDoor(int fromRoomNo, int toRoomNo) {
            Room from;

            // 避免重复添加房间(例如：room1 -> room2, room2 -> room3)
            if (lastRoom != null && fromRoomNo == lastRoom.getRoomNumber()) from = lastRoom;
            else from = currentMaze.getRoom(fromRoomNo);

            Room to = currentMaze.getRoom(toRoomNo);
            Door door = this.door.clone(from, to);

            from.setSide(commonWall(fromRoomNo, toRoomNo), door);
            to.setSide(commonWall(toRoomNo, fromRoomNo), door);

            lastRoom = to;
        }

        @Override public Maze getMaze() { return currentMaze; }

        // 寻找两个房间的公共墙部分，使其能安装一个门
        private Direction commonWall(int fromRoomNo, int toRoomNo) {
            final int column = 10;  // 模拟迷宫宽度
            Direction direction;
            if (fromRoomNo + 1 == toRoomNo) direction = Direction.East; // 右
            else if (fromRoomNo + column == toRoomNo) direction = Direction.South;  // 下
            else if (fromRoomNo - 1 == toRoomNo) direction = Direction.West;    // 左
            else direction = Direction.North;  // 上
            return direction;
        }
    }

    private static class CountingMazeBuilder extends MazeBuilder {
        private int rooms;
        private final StandardMazeBuilder builder;

        public CountingMazeBuilder(StandardMazeBuilder builder) { this.builder = builder; }

        @Override public void buildMaze() { builder.buildMaze(); }
        @Override public void buildDoor(int fromNo, int toNo) { builder.buildDoor(fromNo, toNo); }
        @Override public void buildRoom(int roomNo) {
            rooms++;
            builder.buildRoom(roomNo);
        }

        @Override public Maze getMaze() { return builder.getMaze(); }

        public void getCounts() {
            long doors =
                    MazeFacade.getMaze(SpecialMaze.class).getRooms()
                            .stream()
                            .mapToLong(
                                    room -> room.getSides()
                                            .values()
                                            .stream()
                                            .filter(mapSite -> mapSite.getClass().getName().contains("Door"))
                                            .count()
                            ).sum();
            System.out.println("The maze has " + rooms + " rooms and " + doors + " doors.");
        }
    }

    private static class MazeGame {
        /*
        * 当应用抽象工厂时，传统方法是提供一个抽象类或接口，其中声明一个抽象方法用来创建产品
        * 如: abstract class MazeGame { public abstract Maze createMaze(); }
        * class EnchantedMazeGame extends MazeGame { @Override public Maze createMaze() { ... } }
        * class BombedMazeGame extends MazeGame { @Override public Maze createMaze() { ... } }
        *
        * 客户端表示：
        * MazeGame game = new EnchantedMazeGame();
        * game.createMaze();
        *
        * 这里给创建产品的方法传递了一个工厂参数，用于创建迷宫的部件(如：房间、墙、门)，这里由于在创建迷宫的时候构建的过程以及步骤是一样的，所以传递工厂参数比传统方法更灵活，写法更简单
        * */
        public static Maze createMaze(MazeFactory factory) {
            Maze maze = factory.makeMaze();
            Room from = factory.makeRoom(1);
            Room to = factory.makeRoom(2);
            Door door = factory.makeDoor(from, to);

            maze.addRoom(from);
            maze.addRoom(to);

            // 两个房间形成公共门
            from.setSide(Direction.North, factory.makeWall());
            from.setSide(Direction.East, door);
            from.setSide(Direction.South, factory.makeWall());
            from.setSide(Direction.West, factory.makeWall());

            to.setSide(Direction.North, factory.makeWall());
            to.setSide(Direction.East, factory.makeWall());
            to.setSide(Direction.South, factory.makeWall());
            to.setSide(Direction.West, door);
            return maze;
        }

        // 批量创建迷宫内的房间，以验证房间之间门是否正确设置
        public static Maze createMaze(MazeBuilder builder) {
            builder.buildMaze();
            for (int i = 1; i <= 30; i++) builder.buildRoom(i);
            builder.buildDoor(1, 2);
            builder.buildDoor(2, 3);
            builder.buildDoor(3, 13);
            builder.buildDoor(13, 14);
            builder.buildDoor(14, 15);
            builder.buildDoor(15, 25);
            builder.buildDoor(15, 14);
            builder.buildDoor(14, 4);
            return builder.getMaze();
        }

        public static Maze createMaze() {
            Maze maze = makeMaze();
            Room from = makeRoom(1);
            Room to = makeRoom(2);
            Door door = makeDoor(from, to);

            maze.addRoom(from);
            maze.addRoom(to);

            // 两个房间形成公共门
            from.setSide(Direction.North, makeWall());
            from.setSide(Direction.East, door);
            from.setSide(Direction.South, makeWall());
            from.setSide(Direction.West, makeWall());

            to.setSide(Direction.North, makeWall());
            to.setSide(Direction.East, makeWall());
            to.setSide(Direction.South, makeWall());
            to.setSide(Direction.West, door);
            return maze;
        }

        // 工厂方法，要想使用工厂方法模式，需要不能是静态方法，否则子类不能重写部分的特制部分
        public static Maze makeMaze() { return MazeFacade.getMaze(SpecialMaze.class); }
        public static Room makeRoom(int roomNo) { return new Room(roomNo); }
        public static Wall makeWall() { return new Wall(); }
        public static Door makeDoor(Room from, Room to) { return new Door(from, to); }
    }

    private static class EnchantedMazeGame extends MazeGame {
        public static Room makeRoom(int roomNo) { return new EnchantedRoom(roomNo, castSpell()); }
        public static Door makeDoor(Room from, Room to) { return new DoorNeedingSpell(from, to); }
        private static String castSpell() { return "天王盖地虎"; }
    }


    public static void main( String[] args ) throws InterruptedException, IllegalAccessException {
        // ApplicationContext接口继承MessageSource接口
        ApplicationContext context = new ClassPathXmlApplicationContext("springConfig/spring-mvc.xml");

        // 也可以以编程的方式注册自定义的Scope
//        ConfigurableBeanFactory configurableBeanFactory = new DefaultListableBeanFactory();
//        configurableBeanFactory.registerScope("work", new CustomScope());

//        Arrays.asList(configurableBeanFactory.getRegisteredScopeNames()).forEach(System.out::println);
//        System.out.println(configurableBeanFactory.getRegisteredScope("work").getConversationId());

//        Outer outer = context.getBean("outer", Outer.class);
//        outer.print(outer);
//
//        EmailService emailService = context.getBean("emailService", EmailService.class);
//        emailService.sendEmail("john.doe@example.org", "测试ApplicationEvent");
//        emailService.sendEmail("blockedlist@example.org", "测试ApplicationEvent");
//        emailService.sendEmail("blockedlist@example.org", "测试ApplicationEvent");
//
//        DependsOnExoticType dependsOnExoticType = context.getBean("dependsOnExoticType", DependsOnExoticType.class);
//        System.out.println(dependsOnExoticType);
//
//        TestNumberFormatAnnotationFormatterFactory annotationFormatterFactory =
//                context.getBean("testNumberFormatAnnotationFormatterFactory", TestNumberFormatAnnotationFormatterFactory.class);
//        System.out.println(annotationFormatterFactory);

//        UserDao userDao = context.getBean("userDao", UserDao.class);
//        userDao.show();
//        userDao.showNext();
//
//        Person person = context.getBean("women", Person.class);
//        person.likePerson();
//        Animal animal = (Animal) person;
//        animal.eat();
//
//        spring.testRef.util.Person p = context.getBean("exampleUtilPerson", spring.testRef.util.Person.class);
//        System.out.println(p);
//
//        FavoriteFruit fruit = context.getBean("favoriteFruit", FavoriteFruit.class);
//        System.out.println(fruit);

        List<String> list = new ArrayList<String>(){{ add("One"); add("Two"); add("Three"); add("Four"); add("Five"); add("Six"); }};
        BiConsumer<Object, ForEach.Status> print =
                (a, b) ->
                        System.out.printf(
                                "%s -> { count: %d, index: %d, first: %b, last: %b }%n",
                                a,
                                b.getCount(),
                                b.getIndex(),
                                b.isFirst(),
                                b.isLast()
                        );
        BiConsumer<Object, Integer> consumer = (data, x) -> {
            System.out.println("=======================================================================");
            forEach(data, 0, 0, x, print);
        };

        forEach(null, 10, 20, 4, print);
        consumer.accept(list, 1);
        consumer.accept(list, 2);
        consumer.accept(list, 3);

        Map<String, Integer> map =
                new HashMap<String, Integer>() {{
                    put("one", 1); put("two", 2); put("three", 3); put("four", 4); put("five", 5); put("six", 6); }};
        List<Map.Entry<String, Integer>> data = new ArrayList<>(map.entrySet());
        data.sort(Comparator.comparingInt(Map.Entry::getValue));

        consumer.accept(data, 1);
        consumer.accept(data, 2);
        consumer.accept(data, 3);

        class SetType {
            final int i;
            public SetType(int n) { i = n; }
            public boolean equals(Object o) { return o instanceof SetType && (i == ((SetType) o).i); }
            public String toString() { return Integer.toString(i); }
        }

        class HashType extends SetType {
            public HashType(int n) { super(n); }
            public int hashCode() { return i; }
        }

        class TreeType extends SetType implements Comparable<TreeType> {
            public TreeType(int n) { super(n); }
            public int compareTo(TreeType arg) { return (Integer.compare(arg.i, i)); }
        }

        class TypesForSets {
            public <T extends SetType> void fill(Set<T> set, Class<T> type) {
                try {
                    for (int i = 0; i < 10; i++)
                        set.add(type.getDeclaredConstructor(int.class).newInstance(i));
                } catch (Exception e) { throw new RuntimeException(e); }
            }

            public <T extends SetType> void test(Class<? extends Set> set, Class<T> type) {
                try {
                    Set<T> instance = set.getDeclaredConstructor().newInstance();
                    for (int i = 0; i < 3; i++) fill(instance, type);
                    System.out.println(instance);
                } catch (Exception e) { System.out.println(e.getMessage()); }
            }
        }

        TypesForSets typesForSets = new TypesForSets();

        // 以下都正常输出
        typesForSets.test(HashSet.class, HashType.class);
        typesForSets.test(LinkedHashSet.class, HashType.class);
        typesForSets.test(TreeSet.class, TreeType.class);

        // 以下都没有没有实现hashCode，使用了默认的hashCode(Object)
        typesForSets.test(HashSet.class, SetType.class);
        typesForSets.test(HashSet.class, TreeType.class);
        typesForSets.test(LinkedHashSet.class, SetType.class);
        typesForSets.test(LinkedHashSet.class, TreeType.class);

        // 以下转换失败，因为SetType和HashType都没有实现CompareTo方法
        typesForSets.test(TreeSet.class, SetType.class);
        typesForSets.test(TreeSet.class, HashType.class);

        SingletonBean singletonBean = (SingletonBean) context.getBean("singletonBean");
        TestPrototype testPrototype = (TestPrototype) context.getBean("prototypeInstance");
        singletonBean.printTestPrototypeClassAddress();
        singletonBean.printTestPrototypeClassAddress();

        // 单例Bean需要原型Bean
        // 默认原型作用域spring只会在初始化时初始化一次
        // 后续的Spring不管理整个生命周期 原型豆。容器实例化、配置和以其他方式组装 原型对象并将其交给客户端，没有该原型的进一步记录实例。
        // 客户端代码必须清理原型范围 对象并释放原型 Bean 持有的昂贵资源。要得到 Spring 容器要释放原型范围 Bean 持有的资源，请尝试使用 自定义Bean 后处理器，其中包含对需要清理的豆子。
        // 这时需要客户端(用户)来提供一个创建这个原型Bean的方法，这个方法可以是抽象类的抽象方法，也可以是接口！
        // 通过使用look-up属性进行指定进行实例化的方法，可以简单理解为是一个静态工厂，动态的给原型的Bean添加了一个实例化自身的方法，最终单例Bean可以调用这个实例方法进行每次的实例化原型Bean了
        // 如果是抽象类定义的抽象方法的话，必须满足这个格式：<public|protected> [abstract] <return-type> theMethodName(no-arguments);
        // 下面的两个打印结果，地址是相同的，而上述调用的两次printTestPrototypeClassAddress方法地址则是不同的！
        System.out.println(testPrototype);
        System.out.println(testPrototype);

        int[] primes = PrimeGenerator.generatorPrimes(25);
        System.out.println(Arrays.toString(PrimeGenerator.range(0, 3)));
        System.out.println(Arrays.toString(PrimeGenerator.range(-2, 3)));
        System.out.println(Arrays.toString(PrimeGenerator.range(-1, 3)));
        System.out.println(Arrays.toString(PrimeGenerator.range(-2, -4)));
        System.out.println(Arrays.toString(PrimeGenerator.range(2, -4)));
        System.out.println(Arrays.toString(PrimeGenerator.range(primes.length - 1, primes.length)));
        System.out.println(Arrays.toString(PrimeGenerator.rangeClosed(0, primes.length)));
        System.out.println(Arrays.toString(PrimeGenerator.limit(3)));

        Autowire autowire = context.getBean("customAutowire", Autowire.class);
        CustomResource resource = context.getBean("customResource", CustomResource.class);
        Description test = context.getBean("test", Test.class);
        resource.callResourceTestDescriptionMethod();
        test.description();
        resource.callJSR330ProviderInterface();
        resource.callJSR330OptionalInterface();
        resource.callJSR330NullableInterface();

        TestBean publicInstance = context.getBean("publicInstance", TestBean.class);
        TestBean protectedInstance = context.getBean("protectedInstance", TestBean.class);
        TestBean privateInstance = context.getBean("privateInstance", TestBean.class);
        Arrays.stream(new TestBean[]{ publicInstance, protectedInstance, privateInstance }).forEach(System.out::println);

// 无需在使用AnnotationConfigApplicationContext来创建ApplicationContext，因为ClassPathXmlApplicationContext可以解析使用注解进行配置的类
//        ApplicationContext ctx = new AnnotationConfigApplicationContext(SystemTestConfig.class);
        TransferServiceImpl transferService = context.getBean(TransferServiceImpl.class);
        transferService.transfer(100.00, "A123", "C456");

        EmailService emailService = context.getBean("emailService", EmailService.class);
        emailService.sendEmail("known.spammer@example.org", "你好");
        emailService.sendEmail("jiazhuangme@163.com", "你是在学Spring框架么？");
        context.getBean("resourceDependencyInjection", ResourceDependencyInjection.class).printResourceInfo();

        GenericConversionService conversionService = new DefaultConversionService();
        conversionService.addConverterFactory(new ConverterFactoryTest());
        Lala lala = conversionService.convert("summer", Lala.class);
        System.out.println(lala);
        System.out.println(Enum.valueOf(Lala.class, "SPRING"));

        List<Integer> input = new ArrayList<Integer>() {{ add(1); add(2); add(3); }};
        Object result = conversionService.convert(input,
                TypeDescriptor.forObject(input), // List<Integer> type descriptor
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class)));
        System.out.println(result);


        // 创建型模式：抽象工厂模式、建造者模式、工厂方法、原型模式、单例模式
        CountingMazeBuilder countingMazeBuilder = new CountingMazeBuilder(new StandardMazeBuilder());
        printMazeInfo(MazeGame.createMaze(countingMazeBuilder));
        countingMazeBuilder.getCounts();

        MazePrototypeFactory factory = new MazePrototypeFactory(MazeFacade.getMaze(SpecialMaze.class), new Room(), new Wall(), new Door());
        Maze maze = MazeGame.createMaze(factory);
//        printMazeInfo(maze);

        MazePrototypeFactory factory1 = factory.cloneMazePrototypeFactory();
        Maze maze1 = MazeGame.createMaze(factory1);

        System.out.println("maze == maze1? " + (maze == maze1));    // 因为Maze是一个单例，所以地址相同

        RedBlackTree<Character, Integer> redBlackTree = new RedBlackTree<>();
//        for (int i = 97; i < 123; i++) redBlackTree.put((char) i, i - 97 + 1);
//        System.out.println(redBlackTree.get('z'));
//        redBlackTree.put('A', 1);
//        redBlackTree.put('C', 2);
//        redBlackTree.put('E', 3);
//        redBlackTree.put('H', 4);
//        redBlackTree.put('M', 5);
//        redBlackTree.put('R', 6);
//        redBlackTree.put('S', 7);
//        redBlackTree.put('X', 8);
//        redBlackTree.floor('D');

        RedBlackTreeOriginalVersion<Character, Integer> redBlackTreeOriginalVersion = new RedBlackTreeOriginalVersion<>();

        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < 200; i++) {
            for (int k = 65; k < 91; k++) redBlackTreeOriginalVersion.put((char) k, k);

            char c = (char) random.nextInt(65, 91);

            RedBlackTreeOriginalVersion<Character, Integer>.RedBlackNode deleteNode = redBlackTreeOriginalVersion.delete(c);
            System.out.printf("delete %c -> ", deleteNode.key);

            redBlackTreeOriginalVersion.inOrder();
            System.out.print("\t");

            System.out.println(redBlackTreeOriginalVersion.isRBTree() ? "符合红黑树" : "不符合红黑树");

            redBlackTreeOriginalVersion.clear();
        }

        byte b1 = 1;
        byte b2 = 2;

        char c1 = '1';
        char c2 = '2';

        short s1 = 1;
        short s2 = 2;
        // 在byte、char、short数据使用算数运算符之后，得到的结果都是int类型，如果想赋值给原本的类型，则需要进行强转
        System.out.println(getType(b1 * b2));   // class java.lang.Integer
        System.out.println(getType(c1 * c2));   // class java.lang.Integer
        System.out.println(getType(s1 * s2));   // class java.lang.Integer
        System.out.println(getType(b1 * c2));

        StringProcessor.run();


        String threatData =
                "58.27.82.161@02/10/2005\n" +
                        "204.45.234.40@02/11/2005\n" +
                        "58.27.82.161@02/10/2005\n" +
                        "58.27.82.161@02/10/2005\n" +
                        "58.27.82.161@02/10/2005\n" +
                        "[Next log section with different data format]";
        Scanner sc = new Scanner(threatData);
        String pattern = "(\\d+\\.\\d+\\.\\d+\\.\\d+)@(\\d{2}/\\d{2}/\\d{4})";
        while (sc.hasNext(pattern)) {
            sc.next(pattern);
            MatchResult r = sc.match();
            System.out.format("Threat on %s from %s\n", r.group(2), r.group(1));
        }
    }

    private static class SuperClass<A, B> {
        private A a;
        private B b;
        public void getGenericType() {}
    }

    private static class ChildClass extends SuperClass<String, Integer> {
        public void getGenericType() {
            // 根据泛型擦除可以知道在编译期间把泛型都替换成了Object，所以为了可以在运行时找到泛型的具体类型，就必须进行泛型补偿
            // 根据虚拟机编译字节码来看，这个泛型的具体类型储存在class文件中LocalVariableTypeTable中的attribute中signature属性中
            // 但本身具有泛型类生成的class文件是不知道自己的泛型具体类型，只有在编译其他类(调用的地方或子类)时才知道具体类型，
            // 所以这个具体的泛型就保存在调用类或子类中的signature属性里
            // 这样在实际对泛型做具体类型时，就能在这个地方找到泛型类的是什么类型，也就是说这个东西应该在程序运行中所进行的
            Type parentType = getClass().getGenericSuperclass();
            if (parentType instanceof ParameterizedType) {
                ParameterizedType generic = (ParameterizedType) this.getClass().getGenericSuperclass();
                System.out.println(Arrays.toString(generic.getActualTypeArguments()));
            } else throw new UnsupportedOperationException("Unable to obtain generic direct parent class");
        }
    }

    static class Fruit {}
    static class Apple extends Fruit {}
    static class Orange extends Fruit {}

    private static class Holder<T> {
        private T value;
        public Holder() {}
        public Holder(T value) { this.value = value; }
        public void set(T value) { this.value = value; }
        public T get() { return value; }
        public boolean equals(Object value) { return Objects.equals(this.value, value); }
    }

    private static class Generic<T> {}

    private static class TestTryGetGenericType {
        public static void main(String[] args) throws NoSuchFileException {
            ChildClass childClass = new ChildClass();
            childClass.getGenericType();

            Holder<? extends Fruit> holder = new Holder<>();
            Holder<?> holder1 = new Holder<>();

            List<?> ll = new ArrayList<>();

            Fruit fruit = holder.get();
            Apple apple = (Apple) holder.get();

            Orange orange = (Orange) holder.get();

            /*
             * 通配符 "?" 是指必须明确类型，而在编译期就必须确定，虽然和Object参数概念差不多，但Object是在运行期确定类型，根据继承规则，然后向上转型
             * 而通配符? extends Fruit则表示可以进行协变，而不是说编译时告诉编译器说这个类型是Fruit类型，或者你帮我向上转型，这个是不可以的
             * holder.set(new Apple());  // compile error
             * holder.set(new Orange()); // compile error
             *
             * 另外泛型在应用在数组时，由于在编译之后泛型的实际类型就已经被擦除了，所以这时如果有一个类，类的构造器中初始化数组时候，就像这样:
             * this.array = (T[]) new Type[size]
             * 这产生的问题是，在这个类进行初始化的时候T的类型依然是Object，因为此时的赋值类型还没有确定，所以依然是OBject
             * 如果一开始进行赋予一个Object数组，就像这样：this.array = new Object[size]
             * 然后假设有个get方法来获取array数组中的元素，此时应该在get方法内进行强转，return (T) array[index]，然而如果有一个获取数组的方法getArray，
             * return (T[]) array，这时转型就不管用了，会报ClassCastException，这是因为数组是协变的，不是逆变的，在数组创建的那一刻就已经确定数组的类型了
             * 这个确定的类型就是Object，这个跟通配符的原理差不多，都是在编译期间确定，因此没有任何方式可以推翻底层的数组类型，它只能是Object
             *
             * 泛型类型在调用方法的时候是根据你一开始声明的是什么类型，在调用方法是后就会根据这个类型进行调用，如：
             * List<String> list = new ArrayList<>();
             * list.add("1");
             * String str = list.get(0);
             *
             * List<? extends Fruit> list = new ArrayList<>();
             * Fruit fruit = list.get(0);
             *
             * List list = new ArrayList<>();
             * list.add(1);
             * Object obj = list.get(0);
             *
             * */
            Holder<Apple> holderApple = new Holder<>();
            boolean equals = holder.equals(holderApple);

            testGetter(new GetterImp());

            SortedMap<String, Integer> sortedMap =
                    new TreeMap<>(Comparator.comparingInt(String::length).thenComparing(String.CASE_INSENSITIVE_ORDER));
            Random rand = ThreadLocalRandom.current();
            for (int i = 0; i < 10; i++)
                sortedMap.put(Character.toString((char) (rand.nextInt(10) + 65)), Integer.valueOf(i));
            StringBuilder sb = new StringBuilder("{ ");
            Iterator<Map.Entry<String, Integer>> it = sortedMap.entrySet().iterator();
            for (int index = 0, size = sortedMap.size(); it.hasNext(); index++) {
                Map.Entry<String, Integer> entry = it.next();
                sb.append(entry.getKey()).append(": ").append(entry.getValue());
                if (index < size - 1) sb.append(", ");
            }
            sb.append(" }");
            System.out.println(sb);
            System.out.println(sortedMap.firstKey());
            System.out.println(sortedMap.lastKey());
            System.out.println(findSingleChar("aabbcdd"));
            System.out.println(findSingleChar("abcdabd"));

            Class<?>[] classes = {int.class, float.class, double.class};
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < classes.length; j++)
                    System.out.println(i == 0 ? primitiveToWrapper(classes[j]) : wrapperToPrimitive(primitiveToWrapper(classes[j])));

            Collection<String> tryConcurrentModificationExceptionList = new ArrayList<>();
            Iterator<String> iter = tryConcurrentModificationExceptionList.iterator();
            tryConcurrentModificationExceptionList.add("123");  // 试图在获取迭代器之后进行添加操作，此时进行add操作之后modCount > expectedModCount
            try {
                String element = iter.next();   // expectedModCount和modCount不一致时，会触发fail-fast机制，抛出ConcurrentModificationException
            } catch (ConcurrentModificationException e) {
                String message = e.toString();
                System.out.println(ConsoleOutputController.generatorColorText(message, ConsoleOutputController.ForegroundColor.MAGENTA));
                System.out.println(ConsoleOutputController.generatorFontTypeText(message, ConsoleOutputController.DisplayMode.UNDERLINE));
                System.out.println(ConsoleOutputController.generatorBackgroundColorText(message, ConsoleOutputController.BackGroundColor.MAGENTA));
                System.out.println(
                        ConsoleOutputController.generatorFontTypeAndForegroundColorText(
                                message,
                                ConsoleOutputController.DisplayMode.UNDERLINE,
                                ConsoleOutputController.ForegroundColor.CYAN
                        )
                );
                System.out.println(
                        ConsoleOutputController.generatorForegroundAndBackgroundColorText(
                                message,
                                ConsoleOutputController.ForegroundColor.BLUE,
                                ConsoleOutputController.BackGroundColor.BLACK
                        )
                );
            }

            System.out.println("=========================== 利用CharBuffer来交换ByteBuffer数据中相邻的字符 ===========================");

            // 如果使用ByteBuffer.wrap("UsingBuffers".getBytes())会乱码，因此必须使用Charset.encoded方法提前进行编码，但使用默认的编码还是一样会乱码
            // 如果必须使用ByteBuffer.wrap方法，则无论使用什么字符编码，前提必须保证使用高位优先的字符集来保证字节顺序(ByteBuffer默认高位优先)，如下：
            // ByteBuffer.wrap(StandardCharsets.UTF_16BE.encode("UsingBuffers").array()).asCharBuffer().rewind()
            // 字符集 "UTF-16BE" 默认使用的就是按高位优先的字节顺序进行存放的，可以使用ByteBuffer的order方法来改变字节的存放顺序
            // ByteOrder.BIG_ENDIAN 和 ByteOrder.LITTLE_ENDIAN
            char[] dataChars = "UsingBuffers".toCharArray();
            CharBuffer charBuffer = ByteBuffer.allocate(dataChars.length * 2).asCharBuffer();
            charBuffer.put(dataChars);

            System.out.printf("%s ", ConsoleOutputController.generatorColorText(charBuffer.rewind(), ConsoleOutputController.ForegroundColor.BLUE));

            Arrays.stream(ConsoleOutputController.ForegroundColor.values())
                    .forEach(color ->
                            System.out.printf(
                                    "%s ",
                                    ConsoleOutputController.generatorColorText(
                                            swapCharBufferDataAdjacentCharacters(charBuffer).rewind(),
                                            color
                                    )
                            ));
            System.out.println();

            ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
            StringBuilder builder = new StringBuilder("#");
            for (int i = 0; i < 6; i++) builder.append(Character.forDigit(threadLocalRandom.nextInt() & 15, 16));
            System.out.println(ConsoleOutputController.generatorColorText(builder, ConsoleOutputController.ForegroundColor.BLUE));

            String hexString;
            System.out.println(hexString = Integer.toHexString('a'));
            System.out.println(Integer.parseUnsignedInt(hexString, 16));

            System.out.println("6553600二进制前导0的数量：" + numberOfLeadingZeros(6553600));
            System.out.println(getUnicode("你好"));

            PrintStream printStream = new PrintStream(new BufferedOutputStream(System.out));
            printStream.format("%s: [%d, %d]\n", "coordinate", 1, 2);
            printStream.flush();

            TestLombok lombok = new TestLombok();
            System.out.println(lombok.getAge());

            String[] compareString = {"BLACK", "WHITE", "RED", "GREEN", "CYAN", "BLUE", "BLACA", "BLACK", "a", "b", "a"};
            Arrays.sort(compareString, Comparator.comparingInt(x -> x.toString().length()).thenComparing(new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    String previosString = o1.toString();
                    return compare(previosString, o2.toString(), previosString.length());
                }

                private int compare(String o1, String o2, int len) {
                    char c1 = o1.charAt(0);
                    char c2 = o2.charAt(0);
                    if (c1 == c2) {
                        if (len == 1) return 0;
                        return compare(o1.substring(1), o2.substring(1), len - 1);
                    }
                    return Character.compare(c1, c2);
                }
            }));

            System.out.println(Arrays.toString(compareString));
            IntStream.range(0, 20).forEach(x -> RoShamBol.match(RoShamBol.newItem(), RoShamBol.newItem()));
            System.out.println("============================================================================");
            RoShamBol2.play(RoShamBol2.class, 10);
            System.out.println("============================================================================");
            RoShamBol2.play(RoShamBol3.class, 10);

            System.out.println(Arrays.toString(ChildTestAnnotation.class.getAnnotation(CustomAnnotations.class).annotationType().getDeclaredMethods()));

//            MultiThreadCopy multiThreadCopy = new MultiThreadCopy();
//            CopyFileTime copyFileTime = new CopyFileTime(multiThreadCopy, true);
//            CustomCopyFile copyFile = new CustomCopyFile(
//                    new File("C:\\Users\\jiazh\\Desktop\\1"),
//                    new File("C:\\Users\\jiazh\\Desktop\\1"),
//                    copyFileTime
//            );
//            copyFile.copy();
//            multiThreadCopy.shutdown();
//            System.out.println(copyFileTime.getTime());

//            String[] data = { "1100011", "1100000", "1100110", "1101010", "1110010", "1000010", "0100010" };
//            String[] data2 = { "1100100", "1100111", "1100001", "1101101", "1110101", "1000101", "0100101" };
//            for (String errorCrc: data) System.out.println(crcErrorRecovery(errorCrc, "1011"));
//            System.out.println("==============================================================");
//            for (String errorCrc: data2) System.out.println(crcErrorRecovery(errorCrc, "1101"));
            System.out.println(crcErrorRecovery("110010110101010", "111010001"));
            System.out.println(crcErrorRecovery(createRandomErrorCrc(0x65, 0x1D1), 0x1D1));
            System.out.println(binary2decimalSuccessiveProductAddition("101000101"));
            System.out.println(binary2decimalSuccessiveProductAddition("1010"));
            System.out.println(binary2decimalSuccessiveBaseAdditionMethod(0.101001));
            System.out.println(binary2decimalSuccessiveBaseAdditionMethod(0.111100));
            System.out.println(decimal2binaryMultiplicationBasedRoundingMethod(10.8));
            System.out.println(binary2decimalSuccessiveBaseAdditionMethod(1100.111100));
            System.out.println(decimal2binaryMultiplicationBasedRoundingMethod(binary2decimalSuccessiveBaseAdditionMethod(1100.111100)));
            System.out.println(removeLeadingZeros(new StringBuilder("1100.00011")));
//            System.out.println(binary2decimalSuccessiveBaseAdditionMethod(1110.00112));

            List<Template<?>> list = new ArrayList<>();
            list.add(createTemplate("c", "%+.2f", "打印正数和负数的符号"));
            list.add(createTemplate("空格", "|% .2f|", "在正数之前添加空格"));
            list.add(createTemplate("0", "%010.2f", "数字前面补0"));
            list.add(createTemplate("-", "|%-10.2f|", "左对齐"));
            list.add(createTemplate("(", "%(.2f", "将负数括在括号内", -3333.33));
            list.add(createTemplate(",", "%,.2f", "添加千分符"));
            list.add(createTemplate("#(对于x或0格式)", "%#x", "添加前缀0x或0", 97));
            list.add(createTemplate("$", "%1$d %1$x", "指定要格式化的参数索引。例如，%1$d %1$x将以十进制和十六进制格式打印第1个参数", 159));
            list.add(createTemplate("<", "%d %<x", "格式化前面说明的数值。例如，%d%<x将以十进制和十六进制打印同一个数值", 159));
            printfUsage(list);
            System.out.printf(
                    "%1$s %2$tY-%2$tm-%2$td %2$tH:%2$tM:%2$td\n",
                    ConsoleOutputController.generatorColorText("当前日期：", ConsoleOutputController.ForegroundColor.BLUE),
                    LocalDateTime.now()
            );

//            timerForDisplayingTime();
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(Task.asRunnable(() -> {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Hello World.");
                throw new Exception();
            }));
            service.shutdown();
        }
    }

    interface Task {
        void run() throws Exception;
        static <T extends Throwable> void throwAs(Throwable t) throws T { throw (T) t; }
        static Runnable asRunnable(Task task) {
            return () -> {
                try {
                    task.run();
                } catch (Exception e) {
                    Task.throwAs(e);
                }
            };
        }
    }

    private static void timerForDisplayingTime() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        service.scheduleAtFixedRate(() -> {
            System.out.println("At the tone, the time is " + LocalDateTime.now());
            Toolkit.getDefaultToolkit().beep();
        }, 0, 1, TimeUnit.SECONDS);
        service.scheduleAtFixedRate(() -> {
            while (JOptionPane.showConfirmDialog(null, "Quit program?") != 0);
            service.shutdown();
            System.exit(0);
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    private static class Template<T> {
        public String sign;
        public String template;
        public String desc;
        public T val;
        public Template(String sign, String template, String desc, T val) {
            this.sign = sign;
            this.template = template;
            this.desc = desc;
            this.val = val;
        }
    }

    private static Template<Double> createTemplate(String sign, String template, String desc) {
        return createTemplate(sign, template, desc, 3333.33);
    }

    private static <T> Template<T> createTemplate(String sign, String template, String desc, T val) {
        return new Template<>(sign, template, desc, val);
    }

    private static void printfUsage(List<Template<?>> data) {
        System.out.printf("%10s%15s%20s%25s\n", "标志", "模板", "示例", "目的");
        for (Template<?> template: data) {
            System.out.printf("%10s%12s", template.sign, "");
            System.out.printf("%s", template.template);
            System.out.printf("%12s", "");
            System.out.printf(template.template, template.val);
            System.out.printf("%20s", "");
            System.out.printf("%s", template.desc);
            System.out.println();
        }
    }

    private static String decimal2binaryMultiplicationBasedRoundingMethod(double decimalFraction) {
        String doubleStr = primitive2string(decimalFraction);
        checkDecimalPointExist(doubleStr);
        String integerPart = getIntegerPart(doubleStr);
        String integerPartBinary = Integer.toBinaryString(Integer.parseInt(integerPart));
        StringBuilder result = new StringBuilder(integerPartBinary + '.');

        BigDecimal decimal = getBigDecimal(doubleStr).subtract(getBigDecimal(integerPart));
        BigDecimal base = getBigDecimal(2);
        BigDecimal minuend = BigDecimal.ONE;

        double record = decimal.doubleValue();
        double temp;
        do {
            int val = 0;
            if ((temp = getBigDecimalDoubleValue((decimal = decimal.multiply(base)))) >= 1) {
                val++;
                decimal = decimal.subtract(minuend);
            }
            result.append(val);
        } while (temp != 0 && Double.compare(record, temp) != 0);

        return removeLeadingZeros(result);
    }

    // 逐次除基相加法(小数)
    private static double binary2decimalSuccessiveBaseAdditionMethod(double binaryDecimal) {
        String str = primitive2string(binaryDecimal);
        checkDecimalPointExist(str);
        checkDataValid(str);
        final int LENGTH = str.length(), DECIMAL_LENGTH = LENGTH - 2, MASK = 1;
        int begin = findDecimalPointPosition(str) + 1;
        int end = LENGTH - 1;

        BigDecimal[] cache = { BigDecimal.ZERO, BigDecimal.ONE };
        BigDecimal result = cache[0];
        BigDecimal base = getBigDecimal(2);

        for (int i = end; i >= begin; i--)
            result = result.add(cache[char2decimal(str, i) & MASK]).divide(base, DECIMAL_LENGTH, RoundingMode.HALF_UP);
        return getBigDecimalDoubleValue(result.add(getBigDecimal(ConversionTool.binary2decimal(getIntegerPart(str)))));
    }

    private static String getIntegerPart(String data) { return data.substring(0, findDecimalPointPosition(data)); }
    private static <T> BigDecimal getBigDecimal(T val) { return new BigDecimal(String.valueOf(val)); }
    private static double getBigDecimalDoubleValue(BigDecimal decimal) { return decimal.doubleValue(); }

    private static int char2decimal(String data, int index) { return Character.getNumericValue(data.charAt(index)); }
    private static int findDecimalPointPosition(String data) { return data.indexOf('.'); }

    private static void checkDataValid(String binary) {
        int position = binary.indexOf('.');
        check(binary, 0, position);
        check(binary, position + 1, binary.length());
    }

    private static void check(String data, int begin, int end) {
        for (int i = begin; i < end; i++) {
            char c = data.charAt(i);
            if (c != '0' && c != '1')
                throw new IllegalArgumentException(
                        String.format("The data must be composed of binary numerical values. Wrong position at index %d", i));
        }
    }

    private static void checkDecimalPointExist(String data) {
        if (findDecimalPointPosition(data) == -1)
            throw new IllegalArgumentException("The parameter is not a pure decimal");
    }

    private static <T> String primitive2string(T primitive) {
        try {
            // 等价于String.valueOf(primitive)
            return primitiveToWrapper(primitive.getClass())
                    .getDeclaredMethod("toString")
                    .invoke(primitive)
                    .toString();
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static String removeLeadingZeros(StringBuilder sb) {
        int begin = sb.indexOf(".") + 1, end = begin;
        for (int i = begin; Objects.equals(sb.charAt(i), '0'); i++, end++);
        if (begin != end) sb.delete(begin, end);
        return sb.toString();
    }

    // 二进制转十进制(逐次乘积相加，先加后乘，最后一位不乘2)
    private static int binary2decimalSuccessiveProductAddition(String binary) {
        final int LENGTH = binary.length();
        int result = 0;
        for (int i = 0; i < LENGTH; i++) {
            result |= char2decimal(binary, i);
            if (i < LENGTH - 1) result <<= 1;
        }
        return result;
    }

    private static final Random rand = ThreadLocalRandom.current();
    private static String createRandomErrorCrc(int info, int polynomial) {
        return createRandomErrorCrc(info, Integer.toBinaryString(polynomial));
    }

    private static String createRandomErrorCrc(int info, String polynomial) {
        String crc = createCrc(info, polynomial);
        return Integer.toBinaryString(ConversionTool.binary2decimal(crc) ^ (1 << rand.nextInt(crc.length() - 1)));
    }

    private static String createCrc(int info, String polynomial) {
        final int LENGTH = polynomial.length() - 1,
                threshold = 1 << LENGTH,
                decimalPolynomial = ConversionTool.binary2decimal(polynomial);
        checkPolynomialValidity(decimalPolynomial);
        checkInfoCodeAndVerificationCode(info, LENGTH);
        int result = info << LENGTH,
                leftShiftDigits = LENGTH;

        while (leftShiftDigits != 0) {
            if (threshold > info) {
                info <<= 1;
                --leftShiftDigits;
            }
            if (threshold <= info) info ^= decimalPolynomial;
        }

        return Integer.toBinaryString(result | info);
    }

    private static void checkInfoCodeAndVerificationCode(int k, int r) throws IllegalArgumentException {
        if (calcValidDigits(k) + r > (1 << r) - 1)    // k + r <= 2^r - 1
            throw new IllegalArgumentException("The information and checksum digits do not match.");
    }

    private static void checkPolynomialValidity(int polynomial) {
        if (polynomial == 1 || (polynomial & 1) == 0)
            throw new IllegalArgumentException("Incorrect polynomial format.");
    }

    // CRC纠错
    private static String crcErrorRecovery(String crc, int polynomial) {
        return crcErrorRecovery(crc, Integer.toBinaryString(polynomial));
    }

    private static String crcErrorRecovery(int crc, String polynomial) {
        return crcErrorRecovery(Integer.toBinaryString(crc), polynomial);
    }

    private static String crcErrorRecovery(int crc, int polynomial) {
        return crcErrorRecovery(Integer.toBinaryString(crc), Integer.toBinaryString(polynomial));
    }

    private static String crcErrorRecovery(String crc/* 校验码 */, String polynomial/* 多项式 */) {
        int decimalCrc = ConversionTool.binary2decimal(crc);
        final int CRC_LENGTH = crc.length(),
                CARRY_COUNT = CRC_LENGTH - 1,
                ONLY_HIGHEST_1_CRC_LENGTH = 1 << CARRY_COUNT,

                POLYNOMIAL_LENGTH = polynomial.length(),
                REMAINDER_MAXIMUM_NUMBER_DIGITS = 1 << POLYNOMIAL_LENGTH - 1,
                ERROR_CORRECTION_POSITION = ConversionTool.binary2decimal(polynomial.substring(0, POLYNOMIAL_LENGTH - 1)),

                DECIMAL_POLYNOMIAL = ConversionTool.binary2decimal(polynomial),
                ORIGIN_REMAINDER = verifyIfVerificationCodeIncorrect(crc, polynomial);

        if (shouldNotError(ORIGIN_REMAINDER)) return crc;
        int remainder = ORIGIN_REMAINDER;

        do {
            // 将错误移动到最高位时，进行改错，最高位做异或操作，0 -> 1, 1 -> 0
            if (remainder == ERROR_CORRECTION_POSITION) decimalCrc ^= ONLY_HIGHEST_1_CRC_LENGTH;

            // 因为当余数位数不足多项式的位数时，只能在后面补零操作，商无意义，等到和多项式二进制串长度一致时在进行模2除
            // 0010 -> 0100 -> 1000 ^ 1011 = 011
            if ((remainder <<= 1) >= REMAINDER_MAXIMUM_NUMBER_DIGITS) remainder ^= DECIMAL_POLYNOMIAL;

            // 这里的目的是消除最高位为1，效果为100110 -> 001101(循环放置，当最高位为1时，无需进行消除，只需左移即可)
            if (ONLY_HIGHEST_1_CRC_LENGTH <= decimalCrc) {
                int highBitToLowBit = decimalCrc >> CARRY_COUNT;
                decimalCrc ^= ONLY_HIGHEST_1_CRC_LENGTH;
                decimalCrc <<= 1;
                decimalCrc |= highBitToLowBit;  // 把原来的最高位放在校验码的最后面
            } else decimalCrc <<= 1;

            // System.out.println(Integer.toBinaryString(decimalCrc));
        } while (remainder != ORIGIN_REMAINDER);

        String result = Integer.toBinaryString(decimalCrc);
        return fillZero(CRC_LENGTH - result.length(), result);
    }

    private static int verifyIfVerificationCodeIncorrect(String crc, String polynomial) {
        final int CRC_LENGTH = crc.length(),
                POLYNOMIAL_LENGTH = polynomial.length(),
                POLYNOMIAL_DECIMAL_VALUE = ConversionTool.binary2decimal(polynomial);

        int binary_4_bits_group_count = CRC_LENGTH / POLYNOMIAL_LENGTH,
                index = 0,
                result = ConversionTool.binary2decimal(crc.substring(index, POLYNOMIAL_LENGTH));

        while (true) {
            int remainderValidDigits = calcValidDigits(result);
            if (remainderValidDigits == POLYNOMIAL_LENGTH)     // 余数小于多项式二进制的长度时，必须什么也不做(条件允许时候直接返回结果)
                result ^= POLYNOMIAL_DECIMAL_VALUE;

            if (index == CRC_LENGTH) break;

            if (binary_4_bits_group_count > 0) { // 有几个4位的二进制
                index += POLYNOMIAL_LENGTH;
                binary_4_bits_group_count--;
            }

            if (remainderValidDigits < POLYNOMIAL_LENGTH) { // remainder位数不够就落位
                int remainderCrcDigits = CRC_LENGTH - index,
                        fallingCorrectPosition = remainderCrcDigits - 1,    // 余数后面添加的位置(从crc落下)
                        leftShiftDigits = remainderCrcDigits,
                        start = 0,
                        end = remainderCrcDigits;

                // 测量一下CRC余下的位数 + remainderValidDigits是否还能凑够polynomial的长度
                if (POLYNOMIAL_LENGTH - remainderValidDigits < remainderCrcDigits) {
                    fallingCorrectPosition = POLYNOMIAL_LENGTH - remainderValidDigits;
                    leftShiftDigits = fallingCorrectPosition--;
                    start = remainderValidDigits;
                    end = POLYNOMIAL_LENGTH;
                }

                result <<= leftShiftDigits;
                for (; start < end; start++, index++, fallingCorrectPosition--)
                    result |= (Character.getNumericValue(crc.charAt(index)) << Math.max(fallingCorrectPosition, 0));
            }
        }

        return result;
    }

    private static int calcValidDigits(int val) { return 32 - Integer.numberOfLeadingZeros(val); }
    private static boolean shouldNotError(int verificationCode) { return verificationCode == 0; }
    private static String fillZero(int len, String val) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) sb.append(0);
        sb.append(val);
        return sb.toString();
    }

    @CustomAnnotations
    private static class TestAnnotation {}
    private static class ChildTestAnnotation extends TestAnnotation {}

    @Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface CustomAnnotations { int value() default 10; }

    // 两路分发版本3
    private enum RoShamBol3 implements Competitor<RoShamBol3> {
        PAPER, SCISSORS, ROCK;
        private static final Outcome[][] TABLE = {              // 类似于Map, key -> RoShamBol3[], 只不过key使用ordinal方法来代替
                { Outcome.DRAW, Outcome.LOSE, Outcome.WIN },    // { PAPER: { PAPER, SCISSORS, ROCK } }
                { Outcome.WIN, Outcome.DRAW, Outcome.LOSE },    // { SCISSORS: { PAPER, SCISSORS, ROCK } }
                { Outcome.LOSE, Outcome.WIN, Outcome.DRAW }     // { ROCK: { PAPER, SCISSORS, ROCK } }
        };

        @Override public Outcome complete(RoShamBol3 competitor) { return TABLE[this.ordinal()][competitor.ordinal()]; }
    }

    // 两路分发版本2
    enum Outcome { WIN, LOSE, DRAW }
    private interface Competitor<T extends Competitor<T>> { Outcome complete(T competitor); }
    private enum RoShamBol2 implements Competitor<RoShamBol2> {
        PAPER(Outcome.DRAW, Outcome.LOSE, Outcome.WIN),
        SCISSORS(Outcome.WIN, Outcome.DRAW, Outcome.LOSE),
        ROCK(Outcome.LOSE, Outcome.WIN, Outcome.DRAW);
        private final Outcome paper;
        private final Outcome scissors;
        private final Outcome rock;
        RoShamBol2(Outcome paper, Outcome scissors, Outcome rock) {
            this.paper = paper;
            this.scissors = scissors;
            this.rock = rock;
        }

        @Override public Outcome complete(RoShamBol2 it) {
            switch (it) {
                default:
                case PAPER: return paper;
                case SCISSORS: return scissors;
                case ROCK: return rock;
            }
        }

        public static <T extends Competitor<T>> void match(T a, T b) { System.out.printf("%s VS %s: %s\n", a, b, a.complete(b)); }
        public static <T extends Enum<T> & Competitor<T>> void play(Class<T> cls, int size) {
            IntStream.range(0, size).forEach(x -> match(Enums.random(cls), Enums.random(cls)));
        }
    }

    // 两路分发版本1
    private static class RoShamBol {
        private static final Random RAND = ThreadLocalRandom.current();
        public static Item newItem() {
            switch (RAND.nextInt(3)) {
                default:
                case 0: return new Scissors();
                case 1: return new Paper();
                case 2: return new Rock();
            }
        }

        public static void match(Item a, Item b) { System.out.printf("%s VS %s: %s\n", a, b, a.complete(b)); }
    }

    private static abstract class Item {
        public abstract Outcome eval(Paper item);
        public abstract Outcome eval(Scissors item);
        public abstract Outcome eval(Rock item);
        public abstract Outcome complete(Item item);
        @Override public String toString() { return getClass().getSimpleName(); }
    }

    private static class Paper extends Item {   // 布
        @Override public Outcome complete(Item item) { return item.eval(this); }
        @Override public Outcome eval(Paper paper) { return Outcome.DRAW; }
        @Override public Outcome eval(Scissors scissors) { return Outcome.WIN; }
        @Override public Outcome eval(Rock rock) { return Outcome.LOSE; }
    }

    private static class Scissors extends Item {    // 剪刀
        @Override public Outcome complete(Item item) { return item.eval(this); }
        @Override public Outcome eval(Paper paper) { return Outcome.LOSE; }
        @Override public Outcome eval(Scissors scissors) { return Outcome.DRAW; }
        @Override public Outcome eval(Rock rock) { return Outcome.WIN; }
    }

    private static class Rock extends Item {    // 石头
        @Override public Outcome complete(Item item) { return item.eval(this); }
        @Override public Outcome eval(Paper paper) { return Outcome.WIN; }
        @Override public Outcome eval(Scissors scissors) { return Outcome.LOSE; }
        @Override public Outcome eval(Rock rock) { return Outcome.DRAW; }
    }

    // Enum类实现了Comparable接口，所以使用enum时候默认就可以使用Arrays.sort方法进行排序(Arrays.sort(Compare.values()))
    enum Compare { BLACK, WHITE, RED, GREEN, CYAN, BLUE, BLACA }

    @lombok.Getter
    @lombok.Setter
    private static class TestLombok { private int age = 13; }

    private static String getUnicode(String data) {
        StringBuilder unicodeResult = new StringBuilder();
        for (char c: data.toCharArray()) {
            if (c > 255) unicodeResult.append("\\u");
            else unicodeResult.append("\\u00");
            unicodeResult.append(Integer.toHexString(c));
        }
        return unicodeResult.toString();
    }

    // 计数前导 0
    private static int numberOfLeadingZeros(int i) {
        if (i <= 0) return i == 0 ? 32 : 0;
        int n = 31;
        if (i >= 1 << 16) { n -= 16; i >>>= 16; }
        if (i >= 1 <<  8) { n -=  8; i >>>=  8; }
        if (i >= 1 <<  4) { n -=  4; i >>>=  4; }
        if (i >= 1 <<  2) { n -=  2; i >>>=  2; }
        return n - (i >>> 1);   // i = 1， 2， 3情况
    }

    private static CharBuffer swapCharBufferDataAdjacentCharacters(CharBuffer buffer) {
        while (buffer.hasRemaining()) {
            buffer.mark();  // mark = position 标记一下此时position的位置
            char previousChar = buffer.get();   // position移动
            char nextChar = buffer.get();   // position移动
            buffer.reset(); // 此时需要将position进行归位，以便于开始进行字符交换 position = mark
            buffer.put(nextChar).put(previousChar);     // put方法：如果数据不存在则添加数据，否则则直接进行覆盖(用新值替换旧值)
        }

        return buffer;
    }

    private static Class<?> primitiveToWrapper(Class<?> data) {
        return !data.isPrimitive() ? data : Array.get(Array.newInstance(data, 1), 0).getClass();
    }
    private static Class<?> wrapperToPrimitive(Class<?> data) {
        if (data.isPrimitive()) return data;
        Map<Class<?>, Class<?>> wrapperMap = new HashMap<>();
        Class<?>[] primitiveClasses = {
                char.class, boolean.class, byte.class, short.class, int.class, long.class, float.class, double.class };
        for (Class<?> primitiveClass: primitiveClasses) wrapperMap.put(primitiveToWrapper(primitiveClass), primitiveClass);
        return wrapperMap.get(data);
    }

    // 寻找字符串中唯一一个只出现一次的字符，其他字符必须出现的次数必须是两次或两次以上
    private static char findSingleChar(String data) {
        char result = 0;
        for (char c: data.toCharArray()) result ^= c;
        return result;
    }

    private interface GenericGetter<T extends GenericGetter<T>> { T get(); }
    private interface Getter extends GenericGetter<Getter> {}
    private static class GetterImp implements Getter { @Override public Getter get() { System.out.println(getClass().getSimpleName()); return this; } }
    private static void testGetter(Getter getter) {
        Getter result = getter.get();
        GenericGetter<Getter> genericGetter = getter.get();
    }

    private interface Processor {
        String name();
        Object process(Object input);
    }

    private static class Apply {
        public static void process(Processor processor, Object data) {
            System.out.println("Using Processor " + processor.name());
            System.out.println(processor.process(data));
        }
    }

    private static abstract class StringProcessor implements Processor {
        public static void run() {
            String DATA = "If she weighs the same as a duck, she's made of wood";
            Processor[] processors = { new UpperCase(), new LowerCase(), new Splitter() };
            for (Processor processor: processors) Apply.process(processor, DATA);
        }

        @Override public String name() { return getClass().getSimpleName(); }
    }

    private static class UpperCase extends StringProcessor { @Override public String process(Object input) { return ((String) input).toUpperCase(); } }
    private static class LowerCase extends StringProcessor { @Override public String process(Object input) { return ((String) input).toLowerCase(); } }
    private static class Splitter extends StringProcessor {
        @Override public String process(Object input) { return Arrays.toString(((String) input).split("\\s")); }
    }

    private static class Pet { public Pet() {} }
    private static class Dog extends Pet { public Dog() {} }
    private static class Mutt extends Dog { public Mutt() {} }
    private static class Pug extends Dog { public Pug() {} }

    private static class Cat extends Pet { public Cat() {} }
    private static class EgyptianMau extends Cat { public EgyptianMau() {} }
    private static class Manx extends Cat { public Manx() {} }
    private static class Cymric extends Manx { public Cymric() {} }

    private static class Rodent extends Pet { public Rodent() {} }
    private static class Rat extends Rodent { public Rat() {} }
    private static class Mouse extends Rodent { public Mouse() {} }
    private static class Hamster extends Rodent { public Hamster() {} }

    private static abstract class PetCreator {
        private final ThreadLocalRandom random = ThreadLocalRandom.current();
        public abstract List<Class<? extends Pet>> types();
        public Pet randomPet() {
            try {
                return types().get(random.nextInt(types().size())).getDeclaredConstructor().newInstance();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        public Pet[] createArray(int size) {
            Pet[] result = new Pet[size];
            for (int i = 0; i < size && (result[i] = randomPet()) != null; i++);
            return result;
        }

        public ArrayList<Pet> arrayList(int size) {
            ArrayList<Pet> result = new ArrayList<>();
            Collections.addAll(result, createArray(size));
            return result;
        }
    }

    private static class LiteralPetCreator extends PetCreator {
        public static List<Class<? extends Pet>> allTypes =
                Collections.unmodifiableList(
                        Arrays.asList(
                                Pet.class, Dog.class, Cat.class, Rodent.class, Mutt.class, Pug.class,
                                EgyptianMau.class, Manx.class, Cymric.class, Rat.class, Mouse.class, Hamster.class
                        )
                );

        private static List<Class<? extends Pet>> types = allTypes.subList(allTypes.indexOf(Mutt.class), allTypes.size());
        @Override public List<Class<? extends Pet>> types() { return types; }
    }

    private interface MapCounter<T> extends Map<Class<? extends T>, Integer> {
        void count(T obj);
        default String convertString() {
            StringBuilder sb = new StringBuilder("{ ");
            forEach((key, value) -> sb.append(key.getSimpleName()).append("=").append(value).append(", "));
            sb.delete(sb.length() - 2, sb.length());
            sb.append(" }");
            return sb.toString();
        }
    }

    private static class PetCounter extends LinkedHashMap<Class<? extends Pet>, Integer> implements MapCounter<Pet> {
        private static final long serialVersionUID = -239634576686599407L;

        public PetCounter() { LiteralPetCreator.allTypes.forEach(x -> put(x, 0)); }

        @Override public String toString() { return convertString(); }
        @Override public void count(Pet pet) {
            forEach((k, v) -> {
                if (k.isInstance(pet))
                    put(k, v + 1);
            });
        }
    }

    private static class TypeCounter extends HashMap<Class<?>, Integer> implements MapCounter<Object> {
        private static final long serialVersionUID = 5351486881230090663L;
        private final Class<?> baseType;
        public TypeCounter(Class<?> baseType) { this.baseType = baseType; }

        @Override public String toString() { return convertString(); }
        @Override public void count(Object obj) {
            Class<?> type = obj.getClass();
            if (!baseType.isAssignableFrom(type))
                throw new RuntimeException(
                        String.format(
                                "%s incorrect type: %s, should be type or subtype of %s",
                                obj,
                                type.getSimpleName(),
                                baseType.getSimpleName()
                        )
                );
            countClass(type);
        }

        // 递归计数
        private void countClass(Class<?> type) {
            Integer quantity = get(type);
            put(type, quantity == null ? 1 : quantity + 1);
            Class<?> superClass = type.getSuperclass();
            // 判断baseType是否是superClass的父类(或接口)，或baseType和superClass都是baseType类型，如果是则返回true
            // 这意味着调用isAssignableFrom方法的class对象必须是大于等于isAssignableFrom方法参数内的class类型(父类型或相同类型)
            // isInstance方法是指否是参数是否是调用方的派生类或本身
            /*
            * interface I {}
            * class A implements I {}
            * class B extends A {}
            *
            * B b = new B()
            * I.class.isInstance(b) // true
            * A.class.isInstance(b) // true
            * B.class.isInstance(b) // true
            * */
            if (superClass != null && baseType.isAssignableFrom(superClass))
                countClass(superClass);
        }
    }

    private static class Pets {
        public static final PetCreator creator = new LiteralPetCreator();
        public static Pet randomPet() { return creator.randomPet(); }
        public static Pet[] createArray(int size) { return creator.createArray(size); }
        public static ArrayList<Pet> arrayList(int size) { return creator.arrayList(size); }
    }

    private static class PetCount {
        public static void main(String[] args) {
            printInfo(new PetCounter());
            System.out.println("========================================================================");
            printInfo(new TypeCounter(Pet.class));
        }

        private static void printInfo(MapCounter<? super Pet> counter) {
            for (Pet pet: Pets.createArray(20)) {
                System.out.print(pet.getClass().getSimpleName() + " ");
                counter.count(pet);
            }
            System.out.println();
            System.out.println(counter);
        }
    }



    private static String getType(Object a) { return a.getClass().toString(); }

    private static void printMazeInfo(Maze maze) {
        maze.getRooms().forEach(room -> {
            boolean hasDoor = false;
            for (MapSite<?> mapSite: room.getSides().values()) {
                if (mapSite.getClass().getName().contains("Door")) {
                    hasDoor = true;
                    break;
                }
            }

            if (hasDoor) {
                System.out.println("RoomNo: " + room.getRoomNumber());
                if (room instanceof EnchantedRoom)
                    ((EnchantedRoom) room).enter("天王盖地虎");
                room.getSides().forEach(((direction, m) -> System.out.println("\t" + direction + ": " + m)));
            }
        });
    }

    public static class PrimeGenerator {
        private static boolean[] notPrime;
        private static int[] primes;
        private static int val;
        private static int primeTotalCount;

        // 欧拉筛法时间复杂度O(n)，埃式筛法时间复杂度O(nlgn)
        public static int[] generatorPrimes(int n) {
            if (n < 2) return new int[0];
            init(n);
            eliminateMultiplesPrimeNumbers();
            return filterPrimesAndGenerateNewPrimes();
        }

        private static void init(int n) {
            notPrime = new boolean[n + 1];
            primes = new int[(int) (n / 2.5)];
            val = n;
        }

        private static void eliminateMultiplesPrimeNumbers() {
            final int LENGTH = notPrime.length;
            for (int i = 2; i < LENGTH; i++) eliminateMultiplesPrimeNumbersOf(i);
        }

        private static void eliminateMultiplesPrimeNumbersOf(int i) {
            if (shouldBePrime(i)) setPrimeToPrimes(i);
            // 欧拉筛法可以防止埃式筛法重复筛选某个合数，欧拉筛法在埃式筛法的基础上，让每个合数只被它的一个质因子筛选一次，以达到不重复的目的(这里不应该是最小质因数，因为根据流程不符合)
            // 当素数为2时，primes = [2], 2 * 2 = 4 = true，之后跳出循环！下一次素数为3，primes = [2, 3], 3 * 2 = 6 = true, 3 * 3 = 9 = true，跳出循环
            // 可见，如果是最小质因数筛选的话，那么6质因数为2和3，显然在素数2的时候没有使用素数2进行筛选，在素数3的时候才进行筛选
            // 埃式筛法 -> 比如12，当素数2进行筛除一次，素数3时还会在筛除一次
            // 欧拉筛法主要过程是每一轮循环的i(素数时)分别和primes内已有的素数进行相乘，即可使得i的素数的倍数(合数)进行标记
            for (int j = 0; j < primeTotalCount && i * primes[j] <= val; j++) {
                setPrimeMultipleNotPrime(i * primes[j]);
                if (isPrimeFactorOfVal(i, primes[j])) break;
            }
        }

        private static void setPrimeToPrimes(int val) { primes[primeTotalCount++] = val;}
        private static void setPrimeMultipleNotPrime(int index) { notPrime[index] = true; }

        private static boolean shouldBePrime(int i) { return !notPrime[i]; }
        private static boolean isPrimeFactorOfVal(int val, int prime) { return val % prime == 0; }

        private static int[] filterPrimesAndGenerateNewPrimes() {
            int[] result = new int[primeTotalCount];
            System.arraycopy(primes, 0, result, 0, primeTotalCount);
            return (primes = result);
        }

        public static int[] limit(int limitNumber) {
            if (limitNumber == 0) return new int[0];
            if (limitNumber < 0) limitNumber = -limitNumber;
            int length = Math.min(limitNumber, primeTotalCount);
            int[] result = new int[length];
            System.arraycopy(primes, 0, result, 0, length);
            return result;
        }

        public static int[] rangeClosed(int begin, int end) { return range(begin, end + 1); }
        public static int[] range(int begin, int end) {
            if (begin < 0) { begin = -begin; }
            if (end < 0) { end = -end; }
            if (end > primes.length) end = primes.length;

            begin = Math.min(begin, end);
            end = Math.max(begin, end);

            int length = end - begin;
            int[] result = new int[length];
            for (int i = begin, j = 0; i < end; i++) result[j++] = primes[i];
            return result;
        }
    }

    public static <T> void forEach(Object collection, BiConsumer<T, ForEach.Status> func) { forEach(collection, 0, 0, 0, func); }

    public static <T> void forEach(Object collection, int begin, int end, int step, BiConsumer<T, ForEach.Status> func) {
        checkSpecifiedDataStructure(collection);
        ForEach<T> forEach = new ForEach<>(collection, begin, end, step);
        Iterator<T> iterator = forEach.iterator();
        for (int i = forEach.getBegin(); i <= forEach.getEnd(); i += forEach.getStep())
            if (iterator.hasNext())
                func.accept(iterator.next(), forEach.varStatus);
    }

    private static void checkSpecifiedDataStructure(Object collection) {
        if (Objects.nonNull(collection) && (!(collection instanceof Collection || collection instanceof Map)))
            throw new IllegalArgumentException("collection参数必须是Collection或Map.");
    }

    private static class ForEach<T> implements Iterable<T> {
        public int getBegin() { return begin; }
        public int getEnd() { return end; }
        public int getStep() { return step; }

        public static class Status {
            private int count = 1;
            private int index;
            private boolean first;
            private boolean last;

            public int getCount() { return count; }
            public void setCount(int count) { this.count = count; }
            public int getIndex() { return index; }
            public void setIndex(int index) { this.index = index; }
            public boolean isFirst() { return first; }
            public void setFirst(boolean first) { this.first = first; }
            public boolean isLast() { return last; }
            public void setLast(boolean last) { this.last = last; }
        }

        private final Status varStatus = new Status();
        private final int begin;
        private final int end;
        private final int step;
        private final Object[] data;

        public ForEach(Object data, int begin, int end, int step) {
            this.data = generatorData(data, begin, end);
            this.begin = begin;
            this.step = step == 0 ? 1 : step;
            this.end = end == 0 ? this.data.length : end;
        }

        private Object[] generatorData(Object data, int begin, int end) {
            if (Objects.nonNull(data)) {
                if (data instanceof Collection) return ((Collection<?>) data).toArray();
                else if (data instanceof Map) return ((Map<?, ?>) data).entrySet().toArray();
                else throw new IllegalArgumentException("data必须是实现Collection接口或实现Map接口的数据.");
            } else return IntStream.range(begin, end).boxed().toArray();
        }

        @Override public Iterator<T> iterator() {
            return new Iterator<T>() {
                private int currentIndex = begin;
                @Override public boolean hasNext() {
                    boolean isLast = varStatus.count + step <= data.length;
                    if (end != 0) return isLast && currentIndex <= end;
                    return isLast;
                }

                @Override public T next() {
                    int curIndex = currentIndex - begin;
                    Object item = data[curIndex];
                    if (curIndex == 0) varStatus.setFirst(true);
                    else if (curIndex == data.length - 1) {
                        varStatus.setFirst(false);
                        varStatus.setLast(true);
                    } else {
                        varStatus.setFirst(false);
                        varStatus.setLast(false);
                    }

                    varStatus.setCount(curIndex + 1);
                    varStatus.setIndex(curIndex);
                    currentIndex += step;
                    return (T) item;
                }
            };
        }
    }
}
