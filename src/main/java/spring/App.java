package spring;

import jakarta.annotation.Nonnull;
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
import spring.randomEnum.RandomColor;
import spring.resource.ResourceDependencyInjection;
import spring.testPrototype.SingletonBean;
import spring.testPrototype.TestPrototype;
import spring.testRef.event.EmailService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

        System.out.println("SuperClass classloader is: " + SuperClass.class.getClassLoader());
        System.out.println("The parent class loader of SuperClass is: " + SuperClass.class.getClassLoader().getParent());
        System.out.println("The parent class loader of SuperClass is: " + SuperClass.class.getClassLoader().getParent().getParent());
        System.out.println("int classloader is: " + int.class.getClassLoader());
        new Restaurant();
    }

    private interface PoolInterface { int getId(); }
    private static class Pool {     // Semaphore模拟对象池
        private final PoolInterface[] items;
        private final boolean[] checkedOut;
        private final Semaphore available;
        private final Object checkInLock = new Object();
        private final Object checkOutLock = new Object();
        public Pool(Class<? extends PoolInterface> classObject, int size) {
            this.items = (PoolInterface[]) Array.newInstance(classObject, size);
            this.checkedOut = new boolean[size];
            this.available = new Semaphore(size, true);
            for (int i = 0; i < size; i++) {
                try {
                    this.items[i] = (PoolInterface) classObject.getDeclaredConstructors()[0].newInstance();
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public <T extends PoolInterface> T checkOut() throws InterruptedException {
            available.acquire();
            return getItem();
        }

        private <T extends PoolInterface> T getItem() {
            int len = items.length;
            for (int i = 0; i < len; i++) {
                if (!checkedOut[i]) {
                    synchronized (checkOutLock) {
                        if (!checkedOut[i]) {
                            checkedOut[i] = true;
                            return (T) items[i];
                        }
                    }
                }
            }
            return null;
        }

        public void checkIn(PoolInterface x) { if (releaseItem(x)) available.release(); }
        private boolean releaseItem(PoolInterface x) {
            int id = x.getId();
            if (items[id].getId() != id) return false;
            if (checkedOut[id]) {
                synchronized (checkInLock) {
                    if (checkedOut[id]) {
                        checkedOut[id] = false;
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private static class Fat implements PoolInterface {
        private volatile double d;
        private static int counter = -1;
        private final int id = ++counter;
        public Fat(){ for (int i = 1; i < 10000;i ++) d = d + (Math.PI + Math.E) / (double) i; }
        public void operation() { System.out.println(this); }
        @Override public String toString() { return "Fat id: " + id; }
        @Override public int getId() { return id; }
    }

    private static class CheckoutTask<T> implements Runnable {
        private static int counter = -1;
        private final int id = ++counter;
        private final Pool pool;
        public CheckoutTask(Pool pool) { this.pool = pool; }
        @Override public void run() {
            try {
                PoolInterface item = pool.checkOut();
                System.out.println(this + " checked out " + item);
                TimeUnit.SECONDS.sleep(1);
                System.out.println(this + " checking in " + item);
                pool.checkIn(item);
            } catch (InterruptedException e) { System.out.println(e.getMessage()); }
        }
        @Override public String toString() { return "CheckoutTask id: " + id; }
    }

    private static class SemaphoreDemo {
        private static final int SIZE = 25;
        public static void main(String[] args) throws InterruptedException {
            Pool pool = new Pool(Fat.class, SIZE);
            ExecutorService exec = Executors.newCachedThreadPool();
            for (int i = 0; i < SIZE; i++) exec.execute(new CheckoutTask<>(pool));
            System.out.println("All CheckoutTasks created");
            List<Fat> list = new ArrayList<>();
            for (int i = 0; i < SIZE; i++) {
                Fat fat = pool.checkOut();
                System.out.println(i + ": main() thread checked out ");
                fat.operation();
                list.add(fat);
            }
            Future<?> blocked = exec.submit(() -> {
                try {
                    pool.checkOut();
                } catch (InterruptedException e) {
                    System.out.println("checkOut() interrupted");
                }
            });
            TimeUnit.SECONDS.sleep(2);
            blocked.cancel(true);
            List<Fat> copyList = new ArrayList<>(list);
            copyList.sort(Comparator.comparingInt(Fat::getId));
            System.out.println("Checking in objects in " + copyList);
            for (Fat fat: list) pool.checkIn(fat);
            for (Fat fat: list) pool.checkIn(fat);
            exec.shutdown();
        }
    }

    private static class ReaderWriterList<T> {  // ReentrantReadWriteLock(写少读多的情况)
        private final ArrayList<T> lockedList;
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        public ReaderWriterList(int size, T initialValue) { lockedList = new ArrayList<>(Collections.nCopies(size, initialValue)); }
        public void set(int index, T element) {
            Lock wlock = lock.writeLock();
            wlock.lock();
            try {
                lockedList.set(index, element);
            } finally { wlock.unlock(); }
        }
        public T get(int index) {
            Lock rlock = lock.readLock();
            rlock.lock();
            try {
                return lockedList.get(index);
            } finally { rlock.unlock(); }
        }

        public static void main(String[] args) {
            new ReaderWriterListTest(30, 1);
        }
    }

    private static class ReaderWriterListTest {
        private final ExecutorService exec = Executors.newCachedThreadPool();
        private static final int SIZE = 100;
        private static final Random rand = new Random(47);
        private final ReaderWriterList<Integer> list = new ReaderWriterList<>(SIZE, 0);
        public ReaderWriterListTest(int readers, int writers) {
            for (int i = 0; i < readers; i++) exec.execute(new Reader());
            for (int i = 0; i < writers; i++) exec.execute(new Writer());
        }
        private class Writer implements Runnable {
            @Override public void run() {
                try {
                    for (int i = 0; i < 20; i++) {
                        int nextInt = rand.nextInt();
                        list.set(i, nextInt);
                        System.out.printf("          第%d位置设置的值是: %d%n", i, nextInt);
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println("此时的读的线程数量：" + list.lock.getReadLockCount());
                    }
                } catch (InterruptedException e) { System.out.println(e.getMessage()); }
                System.out.println("Writer finished, shutting down");
                exec.shutdownNow();
            }
        }
        private class Reader implements Runnable {
            private int index = 0;
            @Override public void run() {
                try {
                    while (!Thread.interrupted()) {
                        for (int i = 0; i < SIZE; i++) {
                            int originIndex = index;
                            int getValue = list.get(originIndex);
                            index = getValue == 0 ? index : index + 1;
                            System.out.printf("第%d位置获取的值是: %d%n", originIndex, getValue);
                            TimeUnit.MILLISECONDS.sleep(50);
                        }
                    }
                } catch (InterruptedException e) { System.out.println(e.getMessage()); }
            }
        }
    }

    private static class QuoteServer {
        public static final int LISTEN_PORT = 4445;
        public QuoteServer() throws IOException { new QuoteServerThread().start(); }
    }

    private static class QuoteServerThread extends Thread {
        private final DatagramSocket socket;
        private BufferedReader reader;
        private static final int BUFFERED_READER_SIZE = 8192;
        public static volatile boolean moreQuotes = true;
        private final Random rand = ThreadLocalRandom.current();
        public QuoteServerThread() throws IOException {
            super();
            super.setName(getClass().getSimpleName());
            socket = new DatagramSocket(new InetSocketAddress(QuoteServer.LISTEN_PORT));
            try { reader = new BufferedReader(new FileReader("one-liners.txt"), BUFFERED_READER_SIZE); }
            catch (FileNotFoundException e) { System.err.println("Could not open quote file. Serving time instead."); }
        }

        @Override public void run() {
            while (moreQuotes) {
                try {
                    byte[] buffer = new byte[256];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    System.out.println("客户端：" + new String(packet.getData(), 0, packet.getLength()));
                    buffer = getNextQuote().getBytes();
                    packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
                    socket.send(packet);
                    TimeUnit.MILLISECONDS.sleep(rand.nextInt(1000) * 5);
                } catch (IOException | InterruptedException e) {
                    noMoreQuotes();
                    throw new RuntimeException(e);
                }
            }
        }

        private String getNextQuote() {
            String returnValue;
            try {
                if (Objects.isNull(reader)) return new Date().toString();
                if (Objects.isNull((returnValue = reader.readLine()))) {
                    noMoreQuotes();
                    returnValue = "No more quotes, GoodBye.";
                }
            } catch (IOException e) { returnValue = "IOException occurred in server"; }
            return returnValue;
        }

        private void noMoreQuotes() {
            moreQuotes = false;
            try { reader.close(); }
            catch (IOException ex) { throw new RuntimeException(ex); }
        }
    }

    private static class QuoteClient {
        public static void main(String[] args) throws IOException {
//            new QuoteServer();
//            DatagramSocket socket = new DatagramSocket();
//            byte[] buffer = new byte[256];
//            byte[] information = "客户端发送消息！".getBytes();
//            DatagramPacket requestPacket =
//                    new DatagramPacket(information, information.length, InetAddress.getByName("127.0.0.1"), QuoteServer.LISTEN_PORT);
//            while (QuoteServerThread.moreQuotes) {
//                socket.send(requestPacket);
//                DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
//                socket.receive(responsePacket);
//                System.out.printf("服务端: %s%n", new String(responsePacket.getData(), 0, responsePacket.getLength()));
//            }
//
//            socket.close();
            displayInterfaceInformation();
            System.out.println(HttpCookie.domainMatches(".example.com", "www.example.com"));
            for (TextStyle style : TextStyle.values())
                System.out.printf("%s: %s%n", style.name(), DayOfWeek.MONDAY.getDisplayName(style, Locale.getDefault()));
            System.out.printf(
                    "%d年%d月份有%d天%n",
                    YearMonth.now().getYear(),
                    YearMonth.now().getMonth().getValue(),
                    YearMonth.of(YearMonth.now().getYear(), 2).lengthOfMonth()
            );
            System.out.printf("%d是闰年么？%b%n", 2024, Year.isLeap(2024));
            ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Harbin"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a");
            System.out.println(zonedDateTime.format(formatter));
            LocalDate today = LocalDate.now();
            System.out.println(today.plus(Period.ofDays(2)));
            System.out.println(LocalDateTime.now().plus(1, ChronoUnit.MONTHS));
            System.out.println(LocalDate.now().with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)));

            class TemporalAdjusterPayday {
                private final LocalDate temporal;
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMM dd").withLocale(Locale.US);

                public TemporalAdjusterPayday(LocalDate temporal) {
                    this.temporal = temporal;
                }

                private LocalDate calcNextPayday() {
                    // 评估传入日期并返回下一个发薪日，假设发薪日每月发生两次：
                    // 15 日和当月的最后一天。如果计算的日期发生在周末，则使用前一个星期五
                    return temporal.with(fromLocalDate -> {
                        LocalDate toLocalDate = LocalDate.from(fromLocalDate);
                        int day;
                        if (toLocalDate.getDayOfMonth() < 15) day = 15;
                        else day = toLocalDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
                        toLocalDate = toLocalDate.withDayOfMonth(day);
                        if (toLocalDate.getDayOfWeek() == DayOfWeek.SATURDAY || toLocalDate.getDayOfWeek() == DayOfWeek.SUNDAY)
                            toLocalDate = toLocalDate.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
                        return fromLocalDate.with(toLocalDate);
                    });
                }

                @Override
                public String toString() {
                    return String.format(
                            "%s: %s%n%s: %s%n",
                            "Given the date",
                            temporal.format(formatter),
                            "the next payday",
                            calcNextPayday().format(formatter));
                }
            }
            // 2013 年，6 月 15 日和 6 月 30 日都在周末。运行 NextPayday 示例，分别日期为 6 月 3 日和 6 月 18 日（2013 年）
            LocalDate givenDate = LocalDate.of(2013, Month.JUNE, 3);
            System.out.println(new TemporalAdjusterPayday(givenDate));
            System.out.println(new TemporalAdjusterPayday(givenDate.withDayOfMonth(18)));
            TemporalQuery<Boolean> isBirthday =
                    temporal ->
                            temporal.get(ChronoField.DAY_OF_MONTH) == 10 && temporal.get(ChronoField.MONTH_OF_YEAR) == Month.AUGUST.getValue();
            System.out.println(LocalDate.now().query(isBirthday));
            // 查找3月份的所有星期一的日期，查找四月的所有的星期二的日期
            System.out.println(Arrays.toString(findSpecifiedMonthWithinAllSpecifiedWeeks(Month.MARCH, DayOfWeek.MONDAY)));
            System.out.println(Arrays.toString(findSpecifiedMonthWithinAllSpecifiedWeeks(Month.APRIL, DayOfWeek.TUESDAY)));

            LocalDate startDate = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
            DateTimeFormatter formatPattern = DateTimeFormatter.ofPattern("yyyy年MMM");
            for (int currentYear = startDate.getYear(); currentYear == startDate.getYear(); startDate = startDate.plus(Period.ofMonths(1))) {
                System.out.printf("%s:%n", startDate.format(formatPattern));
                for (DayOfWeek week: DayOfWeek.values()) {
                    System.out.printf(
                            ConsoleOutputController.generatorColorText(
                                    "\t%s: %s%n", randomColor.generatorRandomForegroundColor(1)),
                            week,
                            Arrays.toString(findSpecifiedMonthWithinAllSpecifiedWeeks(startDate.getMonth(), week)));
                }
            }

            System.out.println("1234567.6666666".replaceAll("\\B(?=(\\d{3})+(?!\\d|$))", ","));
            System.out.println(Paths.get("/foo/bar").isAbsolute());
            System.out.println(Paths.get("/foo/bar/example.txt").toAbsolutePath());
            Path path = Paths.get("/foo");
            System.out.println(path.resolve("./../home/joe"));
            System.out.println(path.resolve("/home/joe")); // resolve参数如果是绝对路径的话会直接返回参数的值

            Path p1 = Paths.get("home");
            Path p3 = Paths.get("home/sally/bar");
            Path p3_to_p1 = p3.relativize(p1);
            System.out.println(p3_to_p1);
            for (Path name: p3) System.out.println(name);
            System.out.println(FileSystems.getDefault().getUserPrincipalLookupService().lookupPrincipalByName("jiazh"));    // 文件的所有者
            System.out.println(
                    Files.getFileAttributeView(Paths.get("one-liners.txt"), BasicFileAttributeView.class)
                            .readAttributes()
                            .isDirectory()
            );

            System.out.format("%-20s %12s %12s %12s\n", "FileSystem", "kbytes", "used", "available");
            for (FileStore store: FileSystems.getDefault().getFileStores()) {
                long total = store.getTotalSpace() / 1024;
                long used = (store.getTotalSpace() - store.getUnallocatedSpace()) / 1024;
                long available = store.getUsableSpace() / 1024;
                String str = store.toString();
                if (str.length() > 20) {
                    System.out.println(str);
                    str = "";
                }
                System.out.format("%-20s %12d %12d %12d\n", str, total, used, available);
            }

            Path link = Paths.get(System.getProperty("user.dir"), "one-linersLink.txt");
            if (Files.notExists(link))
                Files.createLink(link, Paths.get(System.getProperty("user.dir"), "one-liners.txt"));
            System.out.println(Files.isSymbolicLink(link));
            System.out.println(
                    FileSystems.getDefault()
                            .getPathMatcher("glob:**/*.{java,class}")
                            .matches(Paths.get("/com/bar/App.java")));
            String fileType = Files.probeContentType(link);
            System.out.printf("文件类型: %s%n", fileType == null ? "unknown file type" : fileType);

            Properties defaultProps = new Properties();
            try (FileInputStream inputStream = new FileInputStream("defaultProperties.properties")) { defaultProps.load(inputStream); }
            Properties applicationProps = new Properties(defaultProps);
            applicationProps.put("job", "Java Engineer");
            applicationProps.put("game", "Honor of Kings");
            try (Writer fileWriter =
                         new OutputStreamWriter(Files.newOutputStream(Paths.get("applicationProperties.properties")), StandardCharsets.UTF_8)) {
                applicationProps.store(fileWriter, "存储应用程序属性");
            }

            int binary = 0b10100;   // 二进制表示
            System.out.println(binary);

            class Person {
                @lombok.Getter public final String name;
                @lombok.Getter public final int age;
                public Person(String name, int age) {
                    this.name = name;
                    this.age = age;
                }
                @Override public String toString() { return name + " - " + age; }
            }

            Comparator<Person> comparator = Comparator.comparing(Person::getName).thenComparing(Person::getAge, Comparator.reverseOrder());
            List<Person> compareList =
                    Arrays.asList(
                            new Person("Alice", 25),
                            new Person("Bob", 30),
                            new Person("Alice", 22));
            compareList.sort(comparator);
            compareList.forEach(System.out::println);
        }

        private interface Mammal { String identifyMyself(); }
        private static class NewHorse { public String identifyMyself() { return "I am a horse.";} }
        // 父类的方法可以覆盖接口的抽象方法，子类可以不必实现
        private static class Mustang extends NewHorse implements Mammal {
            public static void main(String... args) throws IOException, InterruptedException {
                Mustang myApp = new Mustang();
                System.out.println(myApp.identifyMyself());
//                System.out.format("%,3d%n", +4610);
//                System.out.println(numberToThousandth(46101));
                System.out.println(numberToThousandth(-461012));
                testNumberToThousandth(10);
//                System.out.println(new DecimalFormat("###,###").format(46101));
                // Java SE 7 中，必须指定 type 参数的值
                // 否则编译器Collections.emptyList()是 List<Object>
                processStringList(Collections.<String>emptyList());
                List<?> lala = Arrays.asList(1, 2, 3);  // List<T>是 List<?>的子类型，对于任意具体类型 T
                List<?> test = new ArrayList<>();
                test.add(null); // List<?>‌：‌禁止添加除null外的任何元素‌（编译器无法确定具体类型，存在类型安全风险）‌
                System.out.println(test);
                System.out.println(gcd(3, 7));
                // 后面的 <符号代表使用前一个数进行格式化，强制使用+，‌用零填充左侧空白， 20是总宽度，10是小数宽度，默认%f保留六位小数
                System.out.format("%f, %<+020.10f %n", Math.PI);

                Path testFilePath = Paths.get(System.getProperty("user.dir"), "userDefinedFileAttributeView.txt");
                UserDefinedFileAttributeView userDefinedFileAttributeView =
                        Files.getFileAttributeView(testFilePath, UserDefinedFileAttributeView.class);
                String key = "user.mimetype";
                userDefinedFileAttributeView.write(key, Charset.defaultCharset().encode("text/html"));
                ByteBuffer buffer = ByteBuffer.allocate(userDefinedFileAttributeView.size(key));
                userDefinedFileAttributeView.read(key, buffer);
                System.out.println(Charset.defaultCharset().decode(buffer.flip()));

                // 不指定StandardOpenOption，则默认StandardOpenOption.READ模式
                try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(testFilePath)) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(10);
                    CharBuffer charBuffer = CharBuffer.allocate(byteBuffer.capacity());
                    CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();   // 处理中文乱码，以UTF-8标准读取
                    StringBuilder sb = new StringBuilder();
                    BiConsumer<Runnable, Boolean> process = (operate, isEnd) -> {
                        byteBuffer.flip();
                        CoderResult coderResult = decoder.decode(byteBuffer, charBuffer, !isEnd);
                        if (coderResult == CoderResult.UNDERFLOW) System.out.println("继续输入，还有数据.");
                        else if (coderResult == CoderResult.OVERFLOW) throw new RuntimeException("charBuffer空间太小了，请增大空间后重试.");
                        operate.run();
                        charBuffer.flip();
                        sb.append(charBuffer);
                        if (isEnd) charBuffer.clear();
                    };
                    // 调用byteBuffer.compact()相当于调用buffer.clear()，只不过前者会保留上次未读取到的数据，防止截断
                    while (seekableByteChannel.read(byteBuffer) > 0) process.accept(byteBuffer::compact/*保留未解码字节，避免字符截断‌*/, true);
                    process.accept(() -> decoder.flush(charBuffer)/* 处理末尾字节‌ */, false);
                    System.out.println(sb);
                }
                readAsynchronousFileChannel(testFilePath);
                latch.await();

                Calendar c = Calendar.getInstance();
                c.set(2025, Calendar.AUGUST, 1);
                /**
                 * tB：特定于区域表示的月份
                 * td/te：表示天数，区别是td不满足两位的时候补零
                 * ty/tY：ty表示两位的年份，tY表示完整的年份
                 * tl：表示12小时制中的小时。
                 * tM：表示2位数字的分钟，根据需要使用前导零。
                 * tp：表示特定于区域设置的上午/下午（小写）。一般来说就是am和pm
                 * tm：表示2位数字的月份，根据需要使用前导零。
                 * tD：表示日期为%tm%td%ty 输出08/01/25
                 * */
                System.out.format("%tB %td, %tY %tl:%tM%tp%n", c, c, c, c, c, c); // 输出八月 01, 25 4:31下午

                System.out.println(new DecimalFormat("###,###").format(1234567));
                System.out.println(Integer.toString(65, 16));
                System.out.println(Integer.parseInt("230", 5));

                String searchMe = "Green Eggs and Ham";
                String findMe = "Eggs";
                int searchMeLength = searchMe.length();
                int findMeLength = findMe.length();
                boolean foundIt = false;
                for (int i = 0; i <= (searchMeLength - findMeLength); i++) {
                    if (searchMe.regionMatches(i, findMe, 0, findMeLength)) {
                        foundIt = true;
                        System.out.println(i);
                        System.out.println(searchMe.substring(i, i + findMeLength));
                        break;
                    }
                }
                if (!foundIt)
                    System.out.println("No match found.");

                Node<String> node1 = new Node<>("a");
                node1.add("b");
                node1.add("c");

                Node<String> node2 = new Node<>("a1");
                node2.add("b2");
                node2.current.next = node1.next;
                System.out.println(isCrossNode(node1, node2));

                Function<Integer, Integer> createHour = h -> 60 * 60 * h;
                Function<Integer, Boolean> isLeapYear = year -> year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
                Function<Integer, Integer> determineDaysInYear = y -> isLeapYear.apply(y) ? 366 : 365;
                long currentTime = System.currentTimeMillis();  // 使用System.currentTimeMillis()获取的是UTC时间戳
                long seconds = currentTime / 1000;
                int secondsInDay = createHour.apply(24);
                int totalDays = (int) seconds / secondsInDay;
                int copyTotalDays = totalDays;
                int remainderSecondsInDay = (int) seconds % secondsInDay;
                int timeZoneOffset = createHour.apply(8); // 8小时偏移的秒数(添加时区偏移修正（UTC+8）) UTC+8比UTC快8小时
                remainderSecondsInDay += timeZoneOffset;
                int cYear = 1970;
                int cMonth = 1;

                // 转成年份 -> 2025年
                for (int currentYear = determineDaysInYear.apply(cYear);
                     totalDays >= currentYear && (totalDays -= currentYear) > 0; currentYear = determineDaysInYear.apply(++cYear));

                int[] months = new int[12];
                for (int m = 1; m <= 12; m++) {
                    int cm;
                    switch (m) {
                        case 1: case 3: case 5: case 7: case 8: case 10: case 12: cm = 31; break;
                        case 4: case 6: case 9: case 11: cm = 30; break;
                        default: cm = cYear % 400 == 0 || cYear % 4 == 0 && cYear % 100 != 0 ? 29 : 28; break;
                    }
                    months[m - 1] = cm;
                }

                while (totalDays >= months[cMonth - 1] && (totalDays -= months[cMonth++ - 1]) > 0); // 转成月份 -> 几月
                int cDay = totalDays + 1;
                int cHour = (remainderSecondsInDay / 60 / 60) % 24;
                int cMinutes = (remainderSecondsInDay / 60) % 60;
                int cSeconds = remainderSecondsInDay % 60;
                String cWeek = new String[] { "一", "二", "三", "四", "五", "六", "日" }[(copyTotalDays + 3) % 7]; // 1970年是星期四，所以weeks里的偏移量是3
                System.out.printf("%d-%02d-%02d %02d:%02d:%02d 星期%s%n", cYear, cMonth, cDay, cHour, cMinutes, cSeconds, cWeek);
                Child comparable = max(Arrays.asList(new Child(3), new Child(1), new Child(2)));
                System.out.printf("%s -> %d%n", comparable, comparable.value);
                List<?>[] lsa = new List<?>[10];
                lsa[0] = new ArrayList<Integer>();
                lsa[1] = new LinkedList<String>();
                Arrays.stream(lsa).filter(Objects::nonNull).forEach(x -> System.out.println(x.getClass().getSimpleName()));

                Pattern pattern = Pattern.compile("(\\d)(?=(\\d{3})+$)");
                String testNum = "1";
                while (!isExceedsMaxValueMinusOne(testNum)) {
                    System.out.println(pattern.matcher(testNum).replaceAll("$1,"));
                    testNum += testNum.charAt(testNum.length() - 1) - '0' + 1;
                }

                Path path1 = Paths.get("/fo/bar/baz/asdf/quux/..");
                Path path2 = Paths.get("/foo/bar/baz/asdf/quux/../..");
                Path path3 = Paths.get("/foo/bar/../asdf/./quux");
                Path path4 = Paths.get("/foo/bar/../../asdf/./quux");
                System.out.println(path1.normalize());  // \foo\bar\baz\asdf
                System.out.println(path2.normalize());  // \foo\bar\baz
                System.out.println(path3.normalize());  // \foo\asdf\quux
                System.out.println(path4.normalize());  // \asdf\quux

                Path source = Paths.get("a/aab/bc/d");
                Path target = Paths.get("/x/y");
                Path file = Paths.get("a/aab/bcc/s/r.txt");
                // /a/b从b出发到target内的d.txt，由于是不同目录得先找到相同的父目录，所以在b往上是公共父目录，即：../c/d.txt
                Path relative = source.relativize(file);
                Path merge = target.resolve(relative);  // 拼接：/x/y../c/d.txt
                // 路径有相对路径表示符号(..)，所以要消除，变为真正的path，调用normalize，结果未/x/c/d.txt，y路径为什么没了？
                // 因为在规范化时候有相对路径../，所以要在target路径中往上跳一个目录，如果有两个相对路径../../则往上跳两级目录，以此类推
                Path normalize = merge.normalize();
                System.out.println(normalize);
                System.out.println(relative);

                String[][] urls = new String[][] {
                        { "a/aab/bc/d", "a/aab/bcc/s/r.txt" },
                        { "a/aab/bcc/s/r.txt", "a/aab/bc/d" },
                        { "a/aab/b/c", "a/aab/b/c" },
                        { "a/aab/b", "a/aab/b/c" },
                        { "a/aab/b/c", "a/aab/b" },
                        { "a/aab/b/c", "a/aab/b/cd" },
                        { "a/aab/b/cd", "a/aab/b/c" }
                };

                Arrays.stream(urls).forEach(pairs -> {
                    Path a = Paths.get(pairs[0]);
                    Path b = Paths.get(pairs[1]);
                    String origin = a.relativize(b).toString();
                    String custom = relativize(a.toString(), b.toString());
                    System.out.printf("source: %s, other: %s -> native relative: %s, custom relative: %s, 符合预期么？%b%n",
                            a, b, origin.isEmpty() ? "none" : origin, custom.isEmpty() ? "none" : custom, origin.equals(custom));
                });
                System.out.println("精准验证结果：" + (PosixCompatibilityUtil.isSupported() ? "真正支持" : "不支持"));
                System.out.println(PosixCompatibilityUtil.distinguishingOperatingSystem());

                Path existFile = Paths.get("./acl.txt");
                Path createNewFile = Paths.get("./newAcl.txt");
                Files.deleteIfExists(createNewFile);
                Files.createFile(createNewFile);

                AclFileAttributeView aclFileAttributeView = Files.getFileAttributeView(existFile, AclFileAttributeView.class);
                List<AclEntry> aclEntries = aclFileAttributeView.getAcl();
                for (AclEntry aclEntry: aclEntries) {
                    System.out.println("主体（用户/组）：" + aclEntry.principal());
                    System.out.println("权限类型：" + (aclEntry.type() == AclEntryType.ALLOW ? "允许" : "拒绝"));
                    System.out.println("权限详情：" + aclEntry.permissions());
                    System.out.println("---");
                }

                AclFileAttributeView newAclAttributesView = Files.getFileAttributeView(createNewFile, AclFileAttributeView.class);
                UserPrincipal jiazh = existFile.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName(System.getProperty("user.name"));
                AclEntry entry = AclEntry.newBuilder()
                        .setType(AclEntryType.DENY) // 拒绝访问(此项必须设置)
                        .setPrincipal(jiazh)    // 哪个用户(此项必须设置)
                        .setPermissions(AclEntryPermission.READ_DATA, AclEntryPermission.READ_ATTRIBUTES)   // 禁止权限(此项可选设置)
                        .build();
                List<AclEntry> aclEntryList = newAclAttributesView.getAcl();
                aclEntryList.add(0, entry); // 确保生效，顺序优先
                newAclAttributesView.setAcl(aclEntryList);
                UserPrincipal u = (UserPrincipal) Files.getAttribute(createNewFile, "acl:owner");   // 动态获取视图属性
                System.out.println(u);
                System.out.printf("%s%n",
                        Files.getFileStore(existFile).supportsFileAttributeView(AclFileAttributeView.class) ? "Windows系统" : "Posix标准系统");
            }

            private static class PosixStandardFileSystem {
                private static final Pattern PATTERN = Pattern.compile("^([ugoa]+)([+-=])([rwx]*)$");
                private static final String USER_PRINCIPAL = "ugo";
                private static final String OPERATOR = "+-=";
                private static final String ACCESS_PERMISSION = "rwx";
                private interface Changer { Set<PosixFilePermission> change(Set<PosixFilePermission> perms); }
                public static Changer compile(String expression) {   // java -R u+x,g=r,o-w a
                    if (expression.length() < 2) throw new IllegalArgumentException("Invalid mode");
                    final Set<PosixFilePermission> addPermissions = new HashSet<>();
                    final Set<PosixFilePermission> removePermissions = new HashSet<>();

                    Map<Character, Boolean> allUserPrincipal = createStatusTable(USER_PRINCIPAL);
                    Map<Character, Boolean> allOperator = createStatusTable(OPERATOR);
                    Map<Character, Boolean> allPermission = createStatusTable(ACCESS_PERMISSION);

                    for (String eachExpression: expression.split(",")) {
                        if (eachExpression.isEmpty()) throw new IllegalArgumentException("Invalid mode: empty sub-expression");
                        Matcher matcher = PATTERN.matcher(eachExpression);
                        if (!matcher.matches()) throw new IllegalArgumentException("Invalid mode");
                        String userPrincipal = matcher.group(1);
                        char operator = matcher.group(2).charAt(0);
                        String permission = matcher.group(3);

                        for (int i = 0; i < userPrincipal.length(); i++) {  // 设置主体
                            if (userPrincipal.charAt(i) == 'a') {   // a为全部模式，需要把ugo都设为true
                                setAllAllow(allUserPrincipal, USER_PRINCIPAL::charAt, USER_PRINCIPAL.length());
                                break;
                            }
                            setAllow(allUserPrincipal, userPrincipal.charAt(i));
                        }

                        boolean hasUserPrincipal = false;   // 必须指定主体
                        for (boolean target: allUserPrincipal.values()) {
                            if (target) {
                                hasUserPrincipal = true;
                                break;
                            }
                        }
                        if (!hasUserPrincipal) throw new IllegalArgumentException("Invalid mode");

                        setAllow(allOperator, operator);  // 具体操作
                        setAllAllow(allPermission, permission::charAt, permission.length());    // 设置权限

                        boolean isAssign = operator == '=';
                        if (isAssign && permission.isEmpty()) {   // 对应"u="情况，这样的情况说明需要把rwx权限都要删除，所以只保留"-"操作符
                            for (int i = 0; i < allOperator.size(); i++) {
                                char op = OPERATOR.charAt(i);
                                allOperator.replace(op, op == '-');
                            }
                            setAllAllow(allPermission, ACCESS_PERMISSION::charAt, allPermission.size());    // 所有权限打开
                            operator = '-';
                        }

                        Set<PosixFilePermission> current = addPermissions;
                        Set<PosixFilePermission> next = removePermissions;
                        boolean canActualOperated = allOperator.get(operator);
                        if (operator == '-' && canActualOperated) {
                            current = removePermissions;
                            next = addPermissions;
                        }

                        updaterPermissions(canActualOperated, current, next, allUserPrincipal, allPermission, isAssign);
                        resetStatusTable(allUserPrincipal);
                        resetStatusTable(allOperator);
                        resetStatusTable(allPermission);
                    }

                    return perms -> {
                        perms.addAll(addPermissions);
                        perms.removeAll(removePermissions);
                        return perms;
                    };
                }

                private static void setAllAllow(Map<Character, Boolean> coll, Function<Integer, Character> genKey, int len) {
                    for (int i = 0; i < len; i++)
                        setAllow(coll, genKey.apply(i));
                }
                private static void setAllow(Map<Character, Boolean> map, char key) { map.replace(key, true); }
                private static Map<Character, Boolean> createStatusTable(String text) {
                    Map<Character, Boolean> result = new HashMap<>();
                    for (int i = 0; i < text.length(); i++) result.put(text.charAt(i), false);
                    return result;
                }

                private static void resetStatusTable(Map<Character, Boolean> table) {
                    table.replaceAll((k, v) -> false);
                }

                private static void updaterPermissions(
                        boolean shouldUpdate,
                        Set<PosixFilePermission> addPermissions,
                        Set<PosixFilePermission> removePermissions,
                        Map<Character, Boolean> targets,
                        Map<Character, Boolean> permissions,
                        boolean isAssign
                ) {
                    if (shouldUpdate) {
                        PosixFilePermission[] posixFilePermissions = PosixFilePermission.values();
                        for (int i = 0, index = 0; i < targets.size(); i++, index += 3) {
                            if (targets.get(USER_PRINCIPAL.charAt(i))) {
                                for (int j = 0; j < permissions.size(); j++) {
                                    PosixFilePermission posixFilePermission = posixFilePermissions[index + j];
                                    boolean canAccess  = permissions.get(ACCESS_PERMISSION.charAt(j));
                                    if (canAccess) addPermissions.add(posixFilePermission);
                                    if (isAssign && !canAccess) removePermissions.add(posixFilePermission);
                                }
                            }
                        }
                    }
                }

                public static void setPosixFilePermissions(Path file, Changer changer) {
                    try {
                        Set<PosixFilePermission> perms = Files.getPosixFilePermissions(file);
                        Files.setPosixFilePermissions(file, changer.change(perms));
                    } catch (IOException x) { System.err.println(x); }
                }

                public static class TreeVisitor implements FileVisitor<Path> {
                    private final Changer changer;
                    public TreeVisitor(Changer changer) { this.changer = changer; }
                    @Override @Nonnull public FileVisitResult preVisitDirectory(Path dir, @Nonnull BasicFileAttributes attrs) {
                        setPosixFilePermissions(dir, changer);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override @Nonnull public FileVisitResult visitFile(Path file, @Nonnull BasicFileAttributes attrs) {
                        setPosixFilePermissions(file, changer);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override @Nonnull public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                        if (exc != null) System.err.println("WARNING: " + exc);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override @Nonnull public FileVisitResult visitFileFailed(Path file, @Nonnull IOException exc) {
                        System.err.println("WARNING: " + exc);
                        return FileVisitResult.CONTINUE;
                    }
                }

                public static void usage() {
                    System.err.println("java Chmod [-R] symbolic-mode-list file...");
                    System.exit(-1);
                }

                public static void main(String[] args) throws IOException {
                    if (args.length < 2) usage();
                    int argsIndex = 0;
                    if (args[argsIndex].equals("-R")) {
                        if (args.length < 3) usage();
                        argsIndex++;
                    }

                    Changer changer = compile(args[argsIndex++]);
                    TreeVisitor visitor = new TreeVisitor(changer);
                    Set<FileVisitOption> opts = Collections.emptySet(); // 不遵循软连接
                    for (; argsIndex < args.length; argsIndex++) Files.walkFileTree(Paths.get(args[argsIndex]), opts, Integer.MAX_VALUE, visitor);
                }
            }

            private static class PosixCompatibilityUtil {
                private static final Path TEST_PATH = Paths.get(".");
                private static PosixFileAttributes attributes;
                public static boolean isSupported() {
                    if (!FileSystems.getDefault().supportedFileAttributeViews().contains(PosixFileAttributeView.class.getName())) { return false; }
                    boolean isSupportPosix;
                    try {   // 支持但还需判断是否真正兼容支持(苹果系统支持并兼容Posix)
                        if (attributes == null) attributes = Files.readAttributes(TEST_PATH, PosixFileAttributes.class);
                        isSupportPosix = attributes.owner() != null && attributes.permissions() != null;
                    } catch (UnsupportedOperationException | IOException unsupportedOperationException) { isSupportPosix = false; }
                    return isSupportPosix;
                }
                public static String distinguishingOperatingSystem() {
                    try {
                        Files.readAttributes(TEST_PATH, DosFileAttributes.class);
                        return "DOS(Windows系统)";
                    } catch (UnsupportedOperationException unsupportedOperationException) {
                        try {
                            Files.readAttributes(TEST_PATH, PosixFileAttributes.class);
                            boolean isWSLDirectory = false;
                            for (Path p: createWSLDirectory()) {
                                if (Files.exists(p) && Files.isDirectory(p)) {
                                    isWSLDirectory = true;
                                    break;
                                }
                            }
                            return isWSLDirectory ? "Windows(WSL)系统" : "Posix兼容系统(Linux、macOS、WSL等)";
                        } catch (UnsupportedOperationException un) {
                            return "未知操作系统";
                        } catch (IOException e) { return "PosixPosix兼容系统读取时发生IO异常"; }
                    } catch (IOException e) { return "Windows读取时发生IO异常"; }
                }

                private static Path[] createWSLDirectory() {
                    Path[] paths = new Path[3];
                    for (int i = 0; i < 3; i++) paths[i] = Paths.get("/mnt/" + (char) (99 + i));
                    return paths;
                }
            }

            public static String relativize(String source, String other) {
                int relativeCount = 0, startIndex = 0, index = 0, otherIndex = 0, sourceLen = source.length(), otherLen = other.length();
                if (isSeparatorChar(source.charAt(index))) index++;
                if (isSeparatorChar(other.charAt(otherIndex))) otherIndex++;
                boolean findNotSame = false;
                StringBuilder sb = new StringBuilder();

                while (index < sourceLen && otherIndex < otherLen) {
                    char sourceChar = source.charAt(index);
                    char otherChar = other.charAt(otherIndex);
                    // source: a/aab/bc/d, other: a/aab/b/e， c和/比较：(source和other对调亦是如此)
                    // 此时other是/，到达了下一个路径，也说明找到了不同，需要提前记录other这一段拼接路径的起始位置，即上一个/的位置(b之前的/)
                    if (isSeparatorChar(otherChar) && isSeparatorChar(sourceChar)) startIndex = otherIndex;
                    if (sourceChar != otherChar) {
                        findNotSame = true;
                        break;
                    }
                    index++;
                    otherIndex++;
                }

                boolean sourceTraversalCompleted = index == sourceLen;
                boolean otherTraversalCompleted = otherIndex == otherLen;

                if (sourceLen == otherLen && sourceTraversalCompleted && otherTraversalCompleted) return "";
                if (findNotSame) {
                    startIndex++;
                    relativeCount = calcRemainderSourceRelativeCount(index, source); // 发现不同了，后面就没有遍历的必要了直接消耗source
                } else { // 当没有找到时候，说明不是source遍历完毕就是other遍历完毕，此时需要处理这两种情况
                    if (sourceTraversalCompleted) { // 如果source先消耗完，则直接更新startIndex即可，source: a/b, other: a/b/c
                        boolean notEqual = !isSeparatorChar(other.charAt(otherIndex));  // source: a/b, other: a/bc情况
                        if (notEqual) relativeCount++;
                        startIndex = notEqual ? startIndex + 1 : sourceLen + 1;
                    } else if (otherTraversalCompleted) {    // 如果other先消耗完，需要最后进行增加相对符号的数量，source: a/bc, other: a/b
                        startIndex = !isSeparatorChar(source.charAt(index)) ? startIndex + 1 : otherLen;
                        relativeCount = calcRemainderSourceRelativeCount(startIndex + 1, source);
                    }
                }

                for (int i = 0; i < relativeCount; i++) {
                    sb.append("..");
                    if (i < relativeCount - 1) sb.append(File.separatorChar);
                }

                if (startIndex < otherLen) {
                    if (relativeCount > 0) sb.append(File.separatorChar);
                    sb.append(other, startIndex, otherLen);
                }

                return sb.toString();
            }

            private static boolean isSeparatorChar(char c) { return c == 47 || c == 92; }
            private static int calcRemainderSourceRelativeCount(int findIndex, String source) {
                int relativeCount = 1;
                while (findIndex < source.length()) {
                    if (source.charAt(findIndex) == File.separatorChar) relativeCount++;
                    findIndex++;
                }
                return relativeCount;
            }

            private static class PathStatusUtils {
                // NFS路径：服务器标识:/共享目录[/子目录...]/[文件名]
                // 服务器标识：可包含 IP（如 192.168.1.100）、主机名（如 nfs-server-01）、域名（如 storage.example.com），允许包含 .、-。
                // 路径部分：以 / 分隔的目录或文件，目录 / 文件名可包含字母、数字、_、-，文件可带扩展名（如 app.log）或无扩展名（如 README），
                // 也可是纯目录路径（如 /data/share/）。
                // 由于域名和主机名均可能包含字母 / 数字 /-，需确保域名规则优先于主机名规则（避免主机名规则误匹配域名）。
                private static final Pattern NFS_PATH_REGEX =
                        Pattern.compile(
                                "^(((25[0-5]|2[0-4]\\d|1[0-9]\\d|\\d\\d|\\d)\\.)+\\d{1,3}|" +  // ipv4地址
                                        "([a-zA-Z0-9][a-zA-Z0-9-]*(?<!-)\\.)+([a-zA-Z0-9]+)|" +  // 域名
                                        "[a-zA-Z0-9][a-zA-Z0-9-]*(?<!-))" +  // 主机名
                                        ":(/[a-zA-Z0-9_./-]+)+(/[a-zA-Z0-9]+\\.[a-zA-Z]+)?$"  // 目录/xxx/xx或者/xxx/xx/xx.com
                        );
                public static void main(String[] args) {
                    // 测试不存在文件和无权限的路径（预期抛出异常）
                    List<Boolean> results = Stream.of("D:/docs/note.txt", "/root/private/data").map(x -> {
                        try {
                            return PathStatusUtils.isPathExists(x);
                        } catch (PathAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList());
                    for (int i = 0; i < results.size(); i++) System.out.printf("测试%d - 路径存在状态：%s\n", i + 1, results.get(0));
                    String[] nfsMatch = new String[] {
                            "192.168.1.200:/data/file.txt",
                            "nfs01:/home/user-123/docs",
                            "sub.example.co.uk:/logs",
                            "server-2024.tech123:/backup",
                    };
                    String[] nfsNotMatch = new String[] { "example-.com:/data", "sub-.example.com:/data", "nfs-server-:/data", "-nfs:/data" };

                    test(nfsMatch, "nfs");
                    test(nfsNotMatch, "nfsNot");
                }

                private static void test(String[] data, String name) {
                    for (int i = 0; i < data.length; i++)
                        if (!NFS_PATH_REGEX.matcher(data[i]).matches())
                            System.out.println(name + "Match: " + i);
                    boolean isMatch = "nfs".equals(name);
                    System.out.println(Arrays.stream(data).allMatch(x -> isMatch == NFS_PATH_REGEX.matcher(x).matches()));
                }

                public static boolean isPathExists(String pathStr) throws PathAccessException {
                    Path path = Paths.get(pathStr);
                    boolean exist = Files.exists(path);
                    boolean notExist = Files.notExists(path);
                    // 三种核心场景：1.路径明确存在；2.路径明确不存在；3.exists和notExists都为false情况
                    if (exist) return true;
                    else if (notExist) return false;
                    else { throw new PathAccessException(buildErrorMessage(path)); }
                }

                private static String buildErrorMessage(Path path) {
                    try { Files.isReadable(path); }
                    catch (SecurityException ex) { return showErrorMessage(path, "程序没有该路径的访问权限(账号权限不足)."); }
                    catch (Exception ex) { return showErrorMessage(path, ex.getMessage()); }
                    if (isRemotePath(path)) return showErrorMessage(path, "路径为网络文件(如NFS/SMB)，可能网络断开或远程服务不可用.");
                    return showErrorMessage(path, "路径可能正被其他进程创建或删除，处于临时不稳定状态.");
                }

                private static String showErrorMessage(Path path, String info) {
                    return String.format("无法确定路径 [%s] 的存在状态，可能原因：%s\n", path, info);
                }

                private static boolean isRemotePath(Path path) {
                    String pathStr = path.toString().toLowerCase();
                    // 匹配常见远程路径前缀（NFS 通常以服务器IP/域名开头，SMB 以 \\ 开头），Windows SMB 路径（如 \\server\share）
                    return pathStr.startsWith("\\\\") || NFS_PATH_REGEX.matcher(pathStr).matches();
                }

                private static class PathAccessException extends Exception {
                    public PathAccessException(String message) { super(message); }
                }
            }

            public static boolean isExceedsMaxValueMinusOne(String input) {
                try {
                    return Long.parseLong(input) > Long.MAX_VALUE - 1;
                } catch (NumberFormatException e) {
                    System.err.println("输入格式无效: " + e.getMessage());
                    return true; // 或根据需求抛出自定义异常
                }
            }

            /**
             * PECS（Producer-Extends, Consumer-Super）原则：
             * 生产者场景‌（读取数据）：使用? extends T，此时集合作为数据源是安全的，因为所有元素至少是T类型
             * ‌消费者场景‌（写入数据）：需改用? super T，此时可安全添加T及其子类对象
             */
            // Comparable<? super T>（PECS 原则）。
            // Comparable 是消费型接口（消费 T 进行比较），使用 ? super T 允许更灵活的泛型类型接受（如 T 的子类）。
            // 如果改成omparable<? extends T>:
            //   类型矛盾‌：compareTo方法需消费T类型参数，但? extends T通配符限制了参数只能是T的未知子类，导致实际调用时可能传入不匹配的具体子类型
            //   编译风险‌：例如，若T为Number，Comparable<? extends Number>可能接受compareTo(Integer)或compareTo(Double)，但无法保证实现类实际支持所有子类比较。
            // T extends Comparable<? super T>表示类型T必须实现Comparable接口，且该接口的泛型参数可以是T或其父类。这种设计允许T与其父类类型进行比较，而不仅限于自身或子类
            // 例如，若Student继承Person且Person实现了Comparable<Person>，则Student无需显式实现Comparable<Student>，仍可满足此约束
            // 支持继承体系中的类型无需重复实现Comparable，减少代码冗余
            // Comparable<T>要求严格与自身类型比较，如String只能与String比较,Comparable<? super T>放宽限制，允许与父类比较。
            // 例如GregorianCalendar（继承Calendar并实现Comparable<Calendar>）可满足T extends Comparable<? super T>，但不符合T extends Comparable<T>
            // 确保compareTo方法能安全调用，因? super T保证参数至少是T类型
            // public static <T extends Object & Comparable<? super T>> T max(Collection<T> coll)写法是兼容旧代码，为了在泛型擦除之后和旧代码的签名一致
            // 当使用多重边界时，边界中提到的第一个类型将用作类型变量的擦除，旧代码签名public static Object max(Collection coll)
            public static <T extends Comparable<? super T>> T max(List<? extends T> list) { return max(list, 0, list.size()); }
            public static <T extends Comparable<? super T>> T max(List<? extends T> list, int begin, int end) {
                T maxElem = list.get(begin);
                for (++begin; begin < end; ++begin) {
                    T current = list.get(begin);
                    if (maxElem.compareTo(current) < 0)
                        maxElem = current;
                }
                return maxElem;
            }

            private static class Parent implements Comparable<Parent> { // Comparable<? super T>支持子类进行比较
                protected final int value;
                public Parent(int value) { this.value = value; }
                @Override public int compareTo(Parent other) { return Integer.compare(this.value, other.value); }
            }

            private static class Child extends Parent { public Child(int value) { super(value);} }
            private static class GenericVarianceExample {
                // 使用? super T支持逆变比较，实际比较通过Comparable<Parent>实现
                public static <T> void sortWithParentComparison(List<T> list) { list.sort(null); }
                public static void main(String[] args) {
                    List<Child> children = Arrays.asList(new Child(3), new Child(1), new Child(2));
                    sortWithParentComparison(children); // Child实现了Comparable<Parent>，可通过? super T支持
                    System.out.println("Sorted children:");
                    children.forEach(c -> System.out.printf("%s -> %d%n", c, c.value));
                }
            }

            private static class Node<T> {
                @lombok.Getter private final T item;
                private Node<T> next;
                public Node<T> current;
                public Node(T item) { this.item = item; }

                public void add(T item) {
                    Node<T> newNode = new Node<>(item);
                    Node<T> node = this;
                    for (; node.next != null; node = node.next);
                    node.next = current = newNode;
                }
            }

            private static boolean isCrossNode(Node<String> a, Node<String> b) {
                if (a.next == null || b.next == null) return false;
                Node<String> bluePointer = a;
                Node<String> greenPointer = b;

                for (;;) {
                    if (bluePointer == greenPointer) return true;
                    if (bluePointer.next == null && greenPointer.next == null) return false;
                    if (bluePointer.next == null) bluePointer = b;
                    if (greenPointer.next == null) greenPointer = a;
                    bluePointer = bluePointer.next;
                    greenPointer = greenPointer.next;
                }
            }

            private static class ForkBlur extends RecursiveAction {
                private final int[] mSource;
                private final int mStart;
                private final int mLength;
                private final int[] mDestination;
                private int mBlurWidth = 15; // Processing window size, should be odd.
                public ForkBlur(int[] src, int start, int length, int[] dst) {
                    mSource = src;
                    mStart = start;
                    mLength = length;
                    mDestination = dst;
                }

                // Average pixels from source, write results into destination.
                protected void computeDirectly() {
                    int sidePixels = (mBlurWidth - 1) / 2;
                    for (int index = mStart; index < mStart + mLength; index++) {
                        // Calculate average.
                        float rt = 0, gt = 0, bt = 0;
                        for (int mi = -sidePixels; mi <= sidePixels; mi++) {
                            int mindex = Math.min(Math.max(mi + index, 0), mSource.length - 1);
                            int pixel = mSource[mindex];
                            rt += (float) ((pixel & 0x00ff0000) >> 16) / mBlurWidth;
                            gt += (float) ((pixel & 0x0000ff00) >> 8) / mBlurWidth;
                            bt += (float) ((pixel & 0x000000ff)) / mBlurWidth;
                        }

                        // Re-assemble destination pixel.
                        int dpixel = (0xff000000) | (((int) rt) << 16) | (((int) gt) << 8) | (((int) bt));
                        mDestination[index] = dpixel;
                    }
                }
                protected static int sThreshold = 10000;

                @Override protected void compute() {
                    if (mLength < sThreshold) {
                        computeDirectly();
                        return;
                    }

                    int split = mLength / 2;
                    invokeAll(
                            new ForkBlur(mSource, mStart, split, mDestination),
                            new ForkBlur(mSource, mStart + split, mLength - split, mDestination)
                    );
                }

                // Plumbing follows.
                public static void main(String[] args) throws Exception {
                    String srcName = "bb.jpg";
                    File srcFile = new File(srcName);
                    BufferedImage image = ImageIO.read(srcFile);

                    System.out.println("Source image: " + srcName);

                    BufferedImage blurredImage = blur(image);

                    String dstName = "blurred.jpg";
                    File dstFile = new File(dstName);
                    System.out.println(dstFile);
                    ImageIO.write(blurredImage, "jpg", dstFile);
                    System.out.println("Output image: " + dstName);
                }

                public static BufferedImage blur(BufferedImage srcImage) {
                    int w = srcImage.getWidth();
                    int h = srcImage.getHeight();

                    int[] src = srcImage.getRGB(0, 0, w, h, null, 0, w);
                    int[] dst = new int[src.length];

                    System.out.println("Array size is " + src.length);
                    System.out.println("Threshold is " + sThreshold);

                    int processors = Runtime.getRuntime().availableProcessors();
                    System.out.println(processors + " processor"
                            + (processors != 1 ? "s are " : " is ")
                            + "available");

                    ForkBlur fb = new ForkBlur(src, 0, src.length, dst);

                    ForkJoinPool pool = new ForkJoinPool();

                    long startTime = System.currentTimeMillis();
                    pool.invoke(fb);
                    long endTime = System.currentTimeMillis();

                    System.out.println("Image blur took " + (endTime - startTime) +
                            " milliseconds.");

                    BufferedImage dstImage =
                            new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    dstImage.setRGB(0, 0, w, h, dst, 0, w);

                    return dstImage;
                }
            }

            private static abstract class AbstractImageBlur {
                protected final BufferedImage inputImage;
                protected int blurRadius;
                protected int blurKernel;
                protected int width;
                protected int height;
                protected int totalKernel;
                protected int[] originImageData;
                public AbstractImageBlur(BufferedImage bufferedImage, int blurRadius) {
                    this.inputImage = bufferedImage;
                    initial(blurRadius);
                }

                public AbstractImageBlur(Path imageFile, int blurRadius) throws IOException {
                    this.inputImage = ImageIO.read(imageFile.toFile());
                    initial(blurRadius);
                }

                private void initial(int blurRadius) {
                    this.blurRadius = blurRadius;
                    this.blurKernel = blurRadius * 2 + 1;
                    this.width = inputImage.getWidth();
                    this.height = inputImage.getHeight();
                    this.totalKernel = blurKernel * blurKernel;
                    DataBuffer buffer = inputImage.getRaster().getDataBuffer();
                    // TYPE_INT_RGB、TYPE_INT_ARGB
                    if (buffer instanceof DataBufferInt) this.originImageData = ((DataBufferInt) buffer).getData();
                    else if (buffer instanceof DataBufferByte) {  // BGR
                        byte[] srcBytes = ((DataBufferByte) buffer).getData();
                        int width = inputImage.getWidth();
                        int height = inputImage.getHeight();
                        int[] transformImageData = new int[width * height];

                        for (int i = 0; i < srcBytes.length; i += 3) {
                            int b = srcBytes[i] & 0xFF;     // 确保是个正值
                            int g = srcBytes[i + 1] & 0xFF;
                            int r = srcBytes[i + 2] & 0xFF;
                            transformImageData[i / 3] = (r << 16) | (g << 8) | b;  // 组合为 0x00RRGGBB
                        }
                        this.originImageData = new DataBufferInt(transformImageData, transformImageData.length).getData();
                    } else this.originImageData = inputImage.getRGB(0, 0, width, height, null, 0, width);
                }

                protected BufferedImage testTime() {
                    long prev = System.currentTimeMillis();
                    BufferedImage bufferedImage = blurImageHandler();
                    long current = System.currentTimeMillis();
                    System.out.println("用时: " + (current - prev));
                    return bufferedImage;
                }

                protected int[] decomposeRGB(int pixel) {
                    return new int[] { (pixel >> 16) & 0xff, (pixel >> 8) & 0xff, pixel & 0xff };
                }

                protected BufferedImage blurImageHandler() {
                    int[] blurImageData = processBlurImageData();
                    BufferedImage blurImage = new BufferedImage(width, height, inputImage.getType());
                    System.out.println(inputImage.getType());
                    blurImage.setRGB(0, 0, width, height, blurImageData, 0, width);
                    return blurImage;
                }

                protected String findFileType(Path path) {
                    try {
                        return Files.probeContentType(path).split("/")[1];
                    } catch (IOException e) { throw new RuntimeException(e); }
                }

                protected int disposeBoundary(int value, int end) { return Math.max(0, Math.min(value, end)); }
                protected int normalize(double red, double green, double blue) {
                    return 0xFF000000 | (((int) red) << 16) | (((int) green) << 8) | (int) blue;
                }

                public abstract int[] processBlurImageData();
            }

            private static class AverageBlur extends AbstractImageBlur {
                public AverageBlur(Path imageFile, int blurRadius) throws IOException { super(imageFile, blurRadius); }
                @Override public int[] processBlurImageData() {
                    int[] blurImageData = new int[originImageData.length];
                    for (int y = 0; y < height; y++)
                        for (int x = 0; x < width; x++)
                            blurImageData[y * width + x] = calculateBlurredPixel(y, x, originImageData);
                    return blurImageData;
                }

                private int calculateBlurredPixel(int y, int x, int[] originImageData) {
                    double red = 0, green = 0, blue = 0;
                    for (int dy = -blurRadius; dy <= blurRadius; dy++) {
                        for (int dx = -blurRadius; dx <= blurRadius; dx++) {
                            int ny = disposeBoundary(dy + y, height - 1);
                            int nx = disposeBoundary(dx + x, width - 1);
                            int[] originRGB = decomposeRGB(originImageData[ny * width + nx]);
                            red += originRGB[0];
                            green += originRGB[1];
                            blue += originRGB[2];
                        }
                    }

                    double averageKernel = (1.0 / totalKernel);
                    red *= averageKernel;
                    green *= averageKernel;
                    blue *= averageKernel;

                    return normalize(red, green, blue);
                }

                public static void main(String[] args) throws IOException {
                    AbstractImageBlur averageBlur = new AverageBlur(Paths.get("bb.jpg"), 7);
                    BufferedImage blurImage = averageBlur.testTime();
                    ImageIO.write(blurImage, "jpg", new File("blur_new.jpg"));
                }
            }

            private static class SlideWindowAverageBlur extends AverageBlur {
                public SlideWindowAverageBlur(Path imageFile, int blurRadius) throws IOException { super(imageFile, blurRadius); }
                @Override public int[] processBlurImageData() { return verticalSlideWindow(horizontalSlideWindow()); }

                private int[] horizontalSlideWindow() {     // 水平方向滑动窗口（行处理）
                    return slideWindow(originImageData, height, width, true);
                }

                private int[] verticalSlideWindow(int[] imageData) {    // 垂直方向滑动窗口（列处理）
                    return slideWindow(imageData, width, height, false);
                }

                private int[] slideWindow(int[] imageData, int end, int innerEnd, boolean isHorizontal) {
                    int[] slideImageData = new int[width * height];
                    for (int y = 0; y < end; y++) { // width
                        // 水平横向左侧边界开始，如 3*3卷积，则使用镜像填充则是[2, 1, 1, 2, 3], 右侧[1, 2, 3, 3, 2], 合起来就是[{2, 1, 1}, 2, 3, 3, 2]
                        // {}为滑动窗口，先算出originImageData[1] + originImageData[0] + originImageData[0]，因为滑动窗口大小为 blurKernel
                        int[] initialWindowPixels = initialFirstSlideWindowValue(originImageData, y, isHorizontal);
                        int red = initialWindowPixels[0], green = initialWindowPixels[1], blue = initialWindowPixels[2];
                        int size = isHorizontal ? width - 1 : height - 1;

                        for (int x = 0; x < innerEnd; x++) {    // height
                            int x1 = isHorizontal ? x : y;
                            int y1 = isHorizontal ? y : x;
                            int slideDirection = isHorizontal ? x1 : y1;
                            // red / blurKernel、green / blurKernel、blue / blurKernel取平均值
                            slideImageData[y1 * width + x1] =
                                    0xff000000 | ((red / blurKernel) << 16) | ((green / blurKernel) << 8) | (blue / blurKernel);
                            int previousBoundary = disposeBoundary(slideDirection - blurRadius, size);    // 0, 0, 1
                            int nextBoundary = disposeBoundary(slideDirection + blurRadius, size);    // 1, 2, 3

                            int[] previousPixels = makeSlide(imageData, isHorizontal, previousBoundary, x1, y1);
                            int[] nextPixels = makeSlide(imageData, isHorizontal, nextBoundary, x1, y1);

                            // 下一次滑动则为舍弃左侧一个元素，加入下一个元素
                            red = red - previousPixels[0] + nextPixels[0];
                            green = green - previousPixels[1] + nextPixels[1];
                            blue = blue - previousPixels[2] + nextPixels[2];
                        }
                    }
                    return slideImageData;
                }

                private int[] initialFirstSlideWindowValue(int[] imageData, int direction, boolean isHorizontal) {
                    int red = 0, green = 0, blue = 0;
                    for (int start = -blurRadius; start <= blurRadius; start++) {   // 初始化第一个窗口
                        int srcIndex =
                                isHorizontal ?
                                        direction * width + disposeBoundary(start, width - 1) :
                                        disposeBoundary(start, height - 1) * width + direction;
                        int[] horizontalRGB = decomposeRGB(imageData[srcIndex]);
                        red += horizontalRGB[0];
                        green += horizontalRGB[1];
                        blue += horizontalRGB[2];
                    }
                    return new int[] { red, green, blue };
                }

                private int[] makeSlide(int[] imageData, boolean isHorizontal, int boundary, int x, int y) {
                    return decomposeRGB(imageData[isHorizontal ? y * width + boundary : boundary * width + x]);
                }

                public static void main(String[] args) throws IOException {
                    Path imageFile = Paths.get("bb.jpg");
                    SlideWindowAverageBlur slideWindowAverageBlur = new SlideWindowAverageBlur(imageFile, 5);
                    BufferedImage blurImage = slideWindowAverageBlur.testTime();
                    ImageIO.write(blurImage, slideWindowAverageBlur.findFileType(imageFile), new File("blur_slideWindowAverageBlur.jpg"));
                }
            }

            private static class GaussianBlur extends AbstractImageBlur {
                protected final float sigma;    // 值越大越模糊
                public GaussianBlur(Path imageFile, int blurRadius, float sigma) throws IOException {
                    super(imageFile, blurRadius);
                    this.sigma = sigma;
                }
                @Override public int[] processBlurImageData() {
                    double[] gaussianKernel = processGaussianKernel();
                    int[] gaussianImageData = new int[originImageData.length];

                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            double red = 0, green = 0, blue = 0;
                            double weightSum = 0;

                            for (int dy = -blurRadius; dy <= blurRadius; dy++) {
                                for (int dx = -blurRadius; dx <= blurRadius; dx++) {
                                    int ny = disposeBoundary(dy + y, height - 1);
                                    int nx = disposeBoundary(dx + x, width - 1);
                                    int[] originPixels = decomposeRGB(originImageData[ny * width + nx]);
                                    double weight = gaussianKernel[(dy + blurRadius) * blurKernel + (dx + blurRadius)];
                                    red += originPixels[0] * weight;
                                    green += originPixels[1] * weight;
                                    blue += originPixels[2] * weight;
                                    weightSum += weight;
                                }
                            }

                            red /= weightSum;
                            green /= weightSum;
                            blue /= weightSum;

                            gaussianImageData[y * width + x] = normalize(red, green, blue);
                        }
                    }
                    return gaussianImageData;
                }

                private double[] processGaussianKernel() {
                    double[] kernel = new double[totalKernel];
                    double sum = 0;
                    for (int y = -blurRadius; y <= blurRadius; y++) {
                        for (int x = -blurRadius; x <= blurRadius; x++) {
                            double gaussianFuzzyFormula =
                                    Math.exp((double) -(x * x + y * y) / (2 * sigma * sigma)) / (2 * Math.PI * sigma * sigma);
                            kernel[(y + blurRadius) * blurKernel + (x + blurRadius)] = gaussianFuzzyFormula;
                            sum += gaussianFuzzyFormula;
                        }
                    }

                    for (int i = 0; i < totalKernel; i++) kernel[i] /= sum;
                    return kernel;
                }

                public static void main(String[] args) throws IOException {
                    AbstractImageBlur gaussianBlur = new GaussianBlur(Paths.get("bb.jpg"), 7, 50);
                    BufferedImage blurImage = gaussianBlur.testTime();
                    ImageIO.write(blurImage, "jpeg", new File("blur_gaussianBlur.jpg"));
                }
            }

            private static class WindowGaussianBlur extends GaussianBlur {
                public WindowGaussianBlur(Path imageFile, int blurRadius, float sigma) throws IOException {
                    super(imageFile, blurRadius, sigma);
                }

                @Override public int[] processBlurImageData() {
                    return calculateGaussianWindow(
                            calculateGaussianWindow(originImageData, height, width, true), width, height, false);
                }

                private int[] calculateGaussianWindow(int[] imageData, int end, int innerEnd, boolean isHorizontal) {
                    double[] gaussianKernel = processGaussianKernel();
                    int[] gaussianBlurImageData = new int[width * height];
                    int range = isHorizontal ? width - 1 : height - 1;
                    for (int y = 0; y < end; y++) { // height
                        for (int x = 0; x < innerEnd; x++) {    // width
                            double red = 0, green = 0, blue = 0;
                            int x1 = isHorizontal ? x : y;
                            int y1 = isHorizontal ? y : x;
                            for (int i = -blurRadius; i <= blurRadius; i++) {
                                int value = disposeBoundary(x + i, range);
                                int srcIndex = isHorizontal ? y * width + value : value * width + y;
                                int[] originPixels = decomposeRGB(imageData[srcIndex]);
                                double weight = gaussianKernel[i + blurRadius];

                                red += originPixels[0] * weight;
                                green += originPixels[1] * weight;
                                blue += originPixels[2] * weight;
                            }

                            gaussianBlurImageData[y1 * width + x1] = normalize(red, green, blue);
                        }
                    }
                    return gaussianBlurImageData;
                }

                private double[] processGaussianKernel() {
                    double[] kernel = new double[blurKernel];
                    double sum = 0;
                    for (int i = -blurRadius; i <= blurRadius; i++) {
                        double gaussianFuzzyFormula = Math.exp(-(i * i) / (2 * sigma * sigma)) / (Math.sqrt(2 * Math.PI) * sigma);
                        kernel[i + blurRadius] = gaussianFuzzyFormula;
                        sum += gaussianFuzzyFormula;
                    }

                    for (int i = 0; i < blurKernel; i++) kernel[i] /= sum;
                    return kernel;
                }

                public static void main(String[] args) throws IOException {
                    AbstractImageBlur gaussianBlur = new WindowGaussianBlur(Paths.get("bb.jpg"), 7, 50.0F);
                    BufferedImage blurImage = gaussianBlur.testTime();
                    ImageIO.write(blurImage, "jpeg", new File("blur_slideWindowGaussianBlur.jpg"));
                }
            }
            

            private static class SafeLock {
                static class Friend {
                    @lombok.Getter private final String name;
                    private final Lock lock = new ReentrantLock();
                    public Friend(String name) { this.name = name; }
                    public boolean impendingBow(Friend bower) {
                        boolean myLock = false;
                        boolean yourLock = false;
                        try {
                            myLock = lock.tryLock();
                            yourLock = bower.lock.tryLock();
                        } finally {
                            if (!(myLock && yourLock)) {
                                if (myLock) lock.unlock();
                                if (yourLock) bower.lock.unlock();
                            }
                        }
                        return myLock && yourLock;
                    }

                    public void bow(Friend bower) {
                        if (impendingBow(bower)) {
                            try {
                                System.out.format("%s: %s has bowed to me!%n", this.name, bower.getName());
                                bower.bowBack(this);
                            } finally {
                                lock.unlock();
                                bower.lock.unlock();
                            }
                        } else {
                            System.out.format(
                                    "%s: %s started to bow to me, but saw that I was already bowing to him.%n",
                                    this.name,
                                    bower.getName()
                            );
                        }
                    }

                    public void bowBack(Friend bower) {
                        System.out.format("%s: %s has bowed back to me!%n", this.name, bower.getName());
                    }
                }

                static class BowLoop implements Runnable {
                    private final Friend bower;
                    private final Friend bowerFriend;

                    public BowLoop(Friend bower, Friend bowerFriend) {
                        this.bower = bower;
                        this.bowerFriend = bowerFriend;
                    }

                    @Override public void run() {
                        Random random = new Random();
                        for (;;) {
                            try {
                                TimeUnit.SECONDS.sleep(random.nextInt(5));
                            } catch (InterruptedException ignored) {}
                            bowerFriend.bow(bower);
                        }
                    }
                }


                public static void main(String[] args) {
                    final Friend alphonse = new Friend("Alphonse");
                    final Friend gaston = new Friend("Gaston");
                    new Thread(new BowLoop(alphonse, gaston)).start();
                    new Thread(new BowLoop(gaston, alphonse)).start();
                }
            }

            private static final CountDownLatch latch = new CountDownLatch(1);
            private static void readAsynchronousFileChannel(Path testFilePath) throws IOException {
                AsynchronousFileChannel channel = AsynchronousFileChannel.open(testFilePath, StandardOpenOption.READ);
                CharsetDecoder charsetDecoder = StandardCharsets.UTF_8.newDecoder();
                int size = (int) channel.size();
                int MAX_SIZE = 1024;
                if (size < MAX_SIZE) size = growCapacity(size >>= 1, size);
                else size = MAX_SIZE;
                ByteBuffer buffer = ByteBuffer.allocate(size);
                AtomicLong position = new AtomicLong(0);
                int finalSize = size;
                channel.read(buffer, position.get(), buffer, new CompletionHandler<>() {
                    private CharBuffer charReadBuffer = CharBuffer.allocate(5);
                    @Override public void completed(Integer result, ByteBuffer attachment) {
                        if (result > 0) {
                            buffer.flip();
                            CoderResult coderResult = charsetDecoder.decode(buffer, charReadBuffer, false);
                            while (coderResult.isOverflow()) {
                                CharBuffer newCharBuffer = CharBuffer.allocate(growCapacity(charReadBuffer.capacity(), finalSize));
                                charReadBuffer.flip();
                                newCharBuffer.put(charReadBuffer);
                                charReadBuffer.compact();
                                charReadBuffer = newCharBuffer;
                                coderResult = charsetDecoder.decode(buffer, charReadBuffer, false);
                            }
                            flushCharBuffer();
                            buffer.compact();
                            position.addAndGet(result);
                            channel.read(buffer, position.get(), buffer, this);
                        } else {
                            System.out.println();
                            latch.countDown();
                            closeAsynchronousFileChannel();
                        }
                    }

                    private void flushCharBuffer() {
                        charReadBuffer.flip();
                        char[] chars = new char[charReadBuffer.remaining()];
                        charReadBuffer.get(chars);
                        System.out.print(new String(chars)); // 输出实际内容
                        charReadBuffer.clear();
                    }

                    @Override public void failed(Throwable exc, ByteBuffer attachment) {
                        System.err.println("读取失败: " + exc.getMessage());
                        closeAsynchronousFileChannel();
                    }

                    private void closeAsynchronousFileChannel() {
                        try {
                            channel.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }

            private static int growCapacity(int initialCapacity, int maxCapacity) {
                int power = (int) (Math.log(initialCapacity) / Math.log(2));
                while (Math.pow(2, power) < maxCapacity) power++;
                return (int) Math.pow(2, power);
            }

            public static int gcd(int x, int y) {
                for (int r; (r = x % y) != 0; x = y, y = r);
                return y;
            }

            private static void processStringList(List<String> stringList) {
                // process stringList
            }

            private static void testNumberToThousandth(int length) {
                StringBuilder sb = new StringBuilder();
                Random rand = ThreadLocalRandom.current();
                for (int i = 0; i < length;) {
                    int randValue = rand.nextInt(10);
                    if (i == 0 && randValue == 0) continue;
                    sb.append(randValue);
                    System.out.printf("%s -> %s%n", sb, numberToThousandth(Long.parseLong(sb.toString())));
                    i++;
                }
            }
         
            private static String numberToThousandth(long value) {
                String str = Long.toString(value);
                int length = str.length();
                if (length < 4) return str;
                int thousandDigits = 3;
                boolean isNegative = false;
                if (value < 0) {
                    length--;
                    isNegative = true;
                }
                int thousandDigitSymbolQuantity = length / thousandDigits;
                int integerDigits = length % thousandDigits;
                int startIndex = integerDigits;
                StringBuilder sb = new StringBuilder();
                char thousandSeparator = DecimalFormatSymbols.getInstance().getGroupingSeparator();
                if (!isNegative && integerDigits > 0) sb.append(str, 0, startIndex).append(thousandSeparator);
                else if (isNegative) {
                    sb.append("-");
                    startIndex++;
                }
                for (int i = 0; i < thousandDigitSymbolQuantity; i++) {
                    int endIndex = startIndex + thousandDigits;
                    sb.append(str, startIndex, endIndex);
                    if (endIndex < length) sb.append(thousandSeparator);
                    startIndex += thousandDigits;
                }
                return sb.toString();
            }
        }

        private static class WatchDir {
            private final WatchService watcher = FileSystems.getDefault().newWatchService();
            private final Map<WatchKey, Path> keys = new HashMap<>();
            private final boolean recursive;
            private final boolean trace;
            public WatchDir(Path dir, boolean recursive) throws IOException {
                this.recursive = recursive;
                if (recursive) {
                    System.out.format("Scanner %s ...\n", dir);
                    registerAll(dir);
                    System.out.println("Done");
                } else register(dir);
                this.trace = true;
            }

            private void register(Path dir) throws IOException {
                WatchKey key = dir.register(watcher, getMonitorEventTypes());
                if (trace) {
                    Path prev = keys.get(key);
                    if (prev == null) System.out.format("register: %s\n", dir);
                    else {
                        if (!dir.equals(prev))
                            System.out.format("update: %s -> %s\n", prev, dir);
                    }
                }
                keys.put(key, dir);
            }

            // 获取监听事件：ENTRY_CREATE  ENTRY_DELETE  ENTRY_MODIFY  排除OVERFLOW
            private WatchEvent.Kind<?>[] getMonitorEventTypes() {
                return Arrays.stream(StandardWatchEventKinds.class.getDeclaredFields())
                        .filter(field -> Modifier.isStatic(field.getModifiers()) && field.getName().contains("_"))
                        .map(field -> {
                            try { return cast((WatchEvent.Kind<?>) field.get(null)); }
                            catch (IllegalAccessException e) { throw new RuntimeException(e); }
                        }).toArray(WatchEvent.Kind[]::new);
            }

            private void registerAll(final Path start) throws IOException {
                Files.walkFileTree(start, new SimpleFileVisitor<>() {
                    @Override public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        register(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
            @SuppressWarnings("unchecked")
            private <T> WatchEvent<T> cast(WatchEvent<?> event) { return (WatchEvent<T>) event; }
            @SuppressWarnings("unchecked")
            private <T> WatchEvent.Kind<T> cast(WatchEvent.Kind<?> event) { return (WatchEvent.Kind<T>) event; }

            // WatchEvent 具体事件类型（创建、修改、删除等），包含文件路径和事件类型‌
            // WatchKey 监控事件集合，通过WatchService.take() 或 poll()获取‌
            // Watchable 被监控对象（如Path实例），需注册到 WatchService
            public void processEvents() {
                for (;;) {
                    WatchKey key;
                    try { key = watcher.take(); } catch (InterruptedException e) { return; }
                    Path dir = keys.get(key);
                    if (dir == null) {
                        System.out.println("WatchKey not recognized!");
                        continue;
                    } 
                    for (WatchEvent<?> event: key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        if (kind == StandardWatchEventKinds.OVERFLOW) continue;
                        Path child = dir.resolve((Path) cast(event).context());
                        System.out.format("kind -> context -> %s: %s\n", kind.name(), child);
                        if (recursive && (kind == StandardWatchEventKinds.ENTRY_CREATE)) {
                            try {
                                if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) 
                                    registerAll(child);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    boolean valid = key.reset();
                    if (!valid) {
                        keys.remove(key);
                        if (keys.isEmpty()) break;
                    }
                }
            }

            public static void usage() {
                System.out.println("usage: java WatchDir [-r] dir");
                System.exit(-1);
            }

            public static void main(String[] args) throws IOException {
                if (args.length == 0 || args.length > 2) usage();
                boolean recursive = false;
                int dirArg = 0;
                if (args[0].equals("-r")) {
                    if (args.length < 2) usage();
                    recursive = true;
                    dirArg++;
                }
                new WatchDir(Paths.get(args[dirArg]), recursive).processEvents();
            }
        }

        private static Object[] findSpecifiedMonthWithinAllSpecifiedWeeks(Month month, DayOfWeek week) {
            LocalDate date = Year.now().atMonth(month).atDay(1).with(TemporalAdjusters.firstInMonth(week));
            return Stream.iterate(
                    date,
                    d -> date.getMonth() == d.getMonth(),
                    d -> d.with(TemporalAdjusters.next(week))).toArray();
        }

        private static void displayInterfaceInformation() {
            try {
                Class<?> networkInterfaceClass = NetworkInterface.class;
                List<Method> methods =
                        Arrays.stream(networkInterfaceClass.getDeclaredMethods())
                                .filter(method ->
                                        method.getReturnType() == Enumeration.class &&
                                                !Modifier.isStatic(method.getModifiers()))
                                .collect(Collectors.toList());
                List<NetworkInterface> networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());
                int length = networkInterfaceList.size();
                String segmentationSymbol = ": ";
                for (int i = 0; i < length; i++) {
                    NetworkInterface networkInterface = networkInterfaceList.get(i);
                    String displayName = "Display name: %s\n";
                    String name = "Name: %s\n";
                    printf(displayName, networkInterface.getDisplayName());
                    printf(name, networkInterface.getName());

                    for (Method method: methods) {  // 找到 getSubInterfaces 和 getInetAddresses方法
                        Enumeration<?> result = (Enumeration<?>) method.invoke(networkInterface);
                        while (result.hasMoreElements()) {
                            Object type = result.nextElement();
                            Class<?> invokeMethodClass = type.getClass();
                            if (type instanceof NetworkInterface) {
                                if (method.getName().contains("Sub")) {
                                    displayName = "\tSub Interface " + displayName;
                                    name = "\tSub Interface " + name;
                                }
                                printf(displayName, invoke(type, invokeMethodClass, "getDisplayName"));
                                printf(name, invoke(type, invokeMethodClass, "getName"));
                            } else if (type instanceof InetAddress) {
                                printf("%s: %s\n", invokeMethodClass.getName(), type);
                            }
                        }
                    }
                    /*
                      isUp返回网络接口是否已启动并正在运行
                      isLoopBack返回网络接口是否为环回接口
                      isPointToPoint返回网络接口是否为点对点接口。典型的点对点接口是通过调制解调器的 PPP 连接。
                      isVirtual返回此接口是否为虚拟接口（也称为子接口）。
                      supportsMulticast返回网络接口是否支持多播
                      getHardwareAddress返回接口的硬件地址（通常为 MAC）（如果有）
                      getMTU返回此接口的最大传输单元 （MTU）
                      */
                    for (Method method: networkInterfaceClass.getDeclaredMethods()) { // 查看网络接口类型
                        String format = "%s%s%s%n";
                        String methodName = method.getName();

                        if (Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 0) {
                            Object returnValue = method.invoke(networkInterface);
                            if (method.getReturnType() == boolean.class) {  // isXXX
                                String show = "? ";
                                if (!methodName.startsWith("is") && !methodName.startsWith("get")) {
                                    methodName = convertToSpace(methodName);
                                    show = segmentationSymbol;
                                }
                                printf(format, methodName, show, returnValue);
                            }
                            else if (method.getReturnType() == byte[].class)    // getHardwareAddress方法
                                printf(format, convertToSpace(methodName), segmentationSymbol, Arrays.toString((byte[]) returnValue));
                            else if (methodName.equals("getMTU"))   // getMTU方法
                                printf(format, convertToSpace(methodName), segmentationSymbol, returnValue);
                        }
                    }

                    if (i < length - 1)
                        System.out.println("======================================================================");
                }
            } catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException | SocketException e) {
                throw new RuntimeException(e);
            }
        }
        private static Object invoke(
                Object instance,
                Class<?> invokeMethodClass,
                String methodName
        ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            return invokeMethodClass.getDeclaredMethod(methodName).invoke(instance);
        }

        private static final RandomColor randomColor = new RandomColor();
        private static void printf(String format, Object... args) {
            System.out.printf(
                    ConsoleOutputController.generatorColorText(
                            format, randomColor.generatorRandomForegroundColor(1)), args);
        }

        private static String convertToSpace(String data) {
            int length = data.length();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                char item = data.charAt(i);
                if (Character.isUpperCase(item) && (i + 1 < length && Character.isLowerCase(data.charAt(i + 1)))) {
                    sb.append(" ").append((char) (item + 32));
                    continue;
                }
                sb.append(item);
            }
            return sb.toString();
        }
    }

    private static class SineDraw extends JPanel {
        private static final int SCALE_FACTOR = 200;
        private int points;
        private double[] sines;
        private double[] cosines;
        public SineDraw() { setCycles(5); }
        @Override public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            makeImageHighFidelityQuality(g2);
            double space = (double) getWidth() / (double) points;
            drawTrigonometricFunctionGraph(g2, Color.RED, space, sines);
            drawTrigonometricFunctionGraph(g2, Color.BLUE, space, cosines);
        }

        private void drawTrigonometricFunctionGraph(Graphics2D g2, Color color, double space, double[] functionValues) {
            int[] yPoints = createYCoordinatePoints(functionValues);
            g2.setColor(color);
            for (int i = 1; i < points; i++)
                g2.drawLine((int) ((i - 1) * space), yPoints[i - 1], (int) (i * space), yPoints[i]);
        }

        private int[] createYCoordinatePoints(double[] functionValues) {
            int[] yPoints = new int[points];    // 绘制在JPanel上点的y坐标
            int height = getHeight();
            for (int i = 0; i < points; i++)
                yPoints[i] = (int) (functionValues[i] * height / 2 * 0.95 + (double) height / 2);
            return yPoints;
        }

        private void makeImageHighFidelityQuality(Graphics2D g) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        }

        public void setCycles(int newCycles) {
            points = SCALE_FACTOR * newCycles * 2;
            sines = new double[points];
            cosines = new double[points];
            for (int i = 0; i < points; i++) {
                sines[i] = Math.sin(Math.PI / SCALE_FACTOR * i);    // 计算正弦值
                cosines[i] = Math.cos(Math.PI / SCALE_FACTOR * i);
            }
            repaint();
        }
    }

    private static class SineWave extends JFrame {
        private final SineDraw sines = new SineDraw();
        public SineWave() {
            add(sines);
            JSlider adjustCycles = new JSlider(1, 30, 5);
            adjustCycles.addChangeListener(e -> sines.setCycles(((JSlider) e.getSource()).getValue()));
            add(BorderLayout.SOUTH, adjustCycles);
        }

        public static void main(String[] args) {
            SwingConsole.runAndCenter(new SineWave(), 700, 400);
            String test = "I Love You!";
            String searchWords = "Love";
            for (int i = 0; i < test.length(); i++) {
                // 如果test包含searchWords，则返回true
                if (test.regionMatches(i, searchWords, 0, searchWords.length())) {
                    System.out.println(test.substring(i, i + searchWords.length()));
                    break;
                }
            }
        }
    }

    // 在搜索框鼠标右键弹出菜单，选择菜单的某一项JMenuItem，然后把JMenuItem的文本自动填充到搜索框
    private static class PopupMenu extends JFrame {
        private final JPopupMenu popupMenu = new JPopupMenu();
        private final JTextField textField = new JTextField(10);
        public PopupMenu() {
            setLayout(new FlowLayout());
            add(textField);
            createJMenuItemAndBindListener();
            PopupListener popupListener = new PopupListener();
            addMouseListener(popupListener);
            textField.addMouseListener(popupListener);
        }

        private void createJMenuItemAndBindListener() {
            ActionListener actionListener = new Action();
            String[] texts = new String[] { "Hither", "Yon", "Afar", "Stay Here" };
            for (int i = 0; i < texts.length; i++) {
                JMenuItem menuItem = new JMenuItem(texts[i]);
                menuItem.addActionListener(actionListener);
                if (i == texts.length - 1) popupMenu.addSeparator();
                popupMenu.add(menuItem);
            }
        }

        private class Action implements ActionListener {
            @Override public void actionPerformed(ActionEvent e) {
                textField.setText(((JMenuItem) e.getSource()).getText());
            }
        }
        private class PopupListener extends MouseAdapter {
            @Override public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
            @Override public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }
            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger())
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        public static void main(String[] args) { SwingConsole.runAndCenter(new PopupMenu(), 300, 200); }
    }

    private static class SwingConsole {
        public static void runAndCenter(final JFrame frame, final int width, final int height) {
            SwingUtilities.invokeLater(() -> {
                frame.setTitle(frame.getClass().getSimpleName());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(width, height);
                frame.setVisible(true);
            });
            swingCenter(frame);
        }

        private static void swingCenter(Component component) {
            SwingUtilities.invokeLater(() -> {
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                int centerX = ((int) ((dimension.getWidth() - component.getWidth())) >> 1);
                int centerY = ((int) ((dimension.getHeight() - component.getHeight())) >> 1);
                component.setLocation(centerX, centerY);
            });
        }
    }

    private static class Button extends JFrame {
        public Button() {
            JButton buttonOne = new JButton("我是ButtonOne");
            JButton buttonTwo = new JButton("我是ButtonTwo");
            JTextField text = new JTextField(10);
            ActionListener buttonListener = e -> text.setText(((JButton) e.getSource()).getText());

            buttonOne.addActionListener(buttonListener);
            buttonTwo.addActionListener(buttonListener);

            FlowLayout layout = new FlowLayout();
            layout.setAlignment(FlowLayout.LEFT);

            setLayout(layout);
            add(buttonOne);
            add(buttonTwo);
            add(text);
        }

        public static void main(String[] args) { SwingConsole.runAndCenter(new Button(), 800, 350); }
    }

    private static class ShowAddListeners extends JFrame {
        private final JTextField searchBox = new JTextField("JTextArea", 25);
        private final JTextPane results = new JTextPane();
        @lombok.Getter private enum JTextPaneTextAlign {
            LEFT(StyleConstants.ALIGN_LEFT), CENTER(StyleConstants.ALIGN_CENTER), RIGHT(StyleConstants.ALIGN_RIGHT);
            private final int align;
            JTextPaneTextAlign(int align) { this.align = align; }
        }
        public ShowAddListeners() {
            EnterKeyListener keyListener = new EnterKeyListener();
            searchBox.addKeyListener(keyListener);

            JPanel exampleComponentNames = createExampleComponent();
            JScrollPane scrollPane = new JScrollPane(results);

            setLayout(new GridBagLayout());

            addComponentToRootContainer(exampleComponentNames, scrollPane);
            addItemToExampleComponent(keyListener, scrollPane, exampleComponentNames);

            results.setEditable(false);
            keyListener.action();
        }

        private void addComponentToRootContainer(JComponent exampleComponentNames, JComponent scrollPane) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            add(createSearchComponent("Swing class name (press Enter): "), constraints);
            add(exampleComponentNames, createGridBagLayout(1, 1));
            add(scrollPane, createGridBagLayout(2, GridBagConstraints.REMAINDER));
        }

        private GridBagConstraints createGridBagLayout(int gridY, int gridHeight) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = gridY;
            constraints.gridwidth = 1;
            constraints.gridheight = gridHeight;
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.fill = GridBagConstraints.BOTH;
            return constraints;
        }

        private void addItemToExampleComponent(EnterKeyListener keyListener, JScrollPane scrollPane, JComponent parentComponent) {
            Border margin = new EmptyBorder(5, 5, 5, 5);
            MouseListener itemButtonListener = new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    ShowAddListeners.this.searchBox.setText(((JLabel) e.getSource()).getText());
                    keyListener.action();
                    resetScrollBarPosition(scrollPane);
                }
            };
            for (String name: getComponentClassNames()) {
                JLabel item = new JLabel(name);
                item.setForeground(createRandomColor(parentComponent.getBackground()));
                item.setBorder(margin);
                item.addMouseListener(itemButtonListener);
                item.setToolTipText(name);
                parentComponent.add(item);
            }
        }

        private JPanel createSearchComponent(String title) {
            JPanel topContent = new JPanel();
            topContent.add(new JLabel(title));
            topContent.add(searchBox);
            return topContent;
        }

        private JPanel createExampleComponent() {
            JPanel exampleComponentNames = new JPanel();
            exampleComponentNames.setBackground(Color.DARK_GRAY);
            return exampleComponentNames;
        }

        /**
         * SimpleAttributeSet是要应用文字样式的映射表，
         * StyleConstants是把样式属性添加进SimpleAttributeSet的类。
         * Document是把文本以及文本属性应用到视图中去
         */
        private final StyledDocument textStyleDocument = results.getStyledDocument();
        private final SimpleAttributeSet textAttributeTable = new SimpleAttributeSet();
        private void insertText(String text, JTextPaneTextAlign textAlign) throws BadLocationException {
            StyleConstants.setAlignment(textAttributeTable, textAlign.getAlign());    // 把属性添加进SimpleAttributeSet， SimpleAttributeSet就是个map
            int length = textStyleDocument.getLength();
            textStyleDocument.setParagraphAttributes(0, length, textAttributeTable, false);
            textStyleDocument.insertString(length, text, textAttributeTable);
        }

        // 每次添加内容超出JScroll高度时候滚动跳就会滚动到最下方，使滚动条在最顶部不动，提升UI体验
        private void resetScrollBarPosition(JScrollPane scrollPane) {
            // 重置滚动条位置需要在专门的事件处理机制进行设置，否则不生效
            SwingUtilities.invokeLater(() ->
                    scrollPane.getVerticalScrollBar().setValue(0));
        }

        private final List<Color> colorList =
                Arrays.stream(Color.class.getFields())
                        .filter(field ->
                                Modifier.isStatic(field.getModifiers()) &&
                                        isUpperCase(field.getName()) &&
                                        Objects.equals(field.getType(), Color.class))
                        .map(field -> {
                            try { return (Color) field.get(null); }
                            catch (IllegalAccessException e) { throw new RuntimeException(e); }
                        }).collect(Collectors.toList());
        private final Random random = new Random();
        private Color createRandomColor(Color bgColor) {
            Color color = colorList.get(random.nextInt(colorList.size()));
            if (bgColor.equals(color)) return createRandomColor(bgColor);
            return color;
        }

        private boolean isUpperCase(String data) {
            int len = data.length();
            for (int i = 0; i < len; i++) {
                if (Character.isLowerCase(data.charAt(i)))
                    return false;
            }
            return true;
        }

        private class EnterKeyListener extends KeyAdapter {
            private final Pattern addListener = Pattern.compile("(add\\w+Listener\\(.*?\\))");
            // 去除方法括号里的参数全限定名称，例如：javax.swing.event.MouseListener，只保留MouseListener
            private final Pattern qualifier = Pattern.compile("\\w+\\.");
            private volatile String previousInputText;
            @Override public void keyPressed(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_ENTER) action(); }
            private void noMatchesFound() { setSearchResultAreaText("No match"); }
            private void clearPreviousText() { setSearchResultAreaText(""); }
            private void setSearchResultAreaText(String text) { results.setText(text); }
            public void action() {
                String inputText = searchBox.getText().trim();
                if (Objects.equals(previousInputText, inputText)) return;
                if (inputText.isEmpty()) {
                    noMatchesFound();
                    return;
                }
                Class<?> componentClass;
                try { componentClass = Thread.currentThread().getContextClassLoader().loadClass("javax.swing." + inputText); }
                catch (ClassNotFoundException ex) {
                    noMatchesFound();
                    return;
                }
                clearPreviousText();
                Set<String> orderContainer = new TreeSet<>(Comparator.naturalOrder());
                for (Method method: componentClass.getMethods()) {
                    Matcher matcher = addListener.matcher(method.toString());
                    if (matcher.find())
                        orderContainer.add(qualifier.matcher(matcher.group(1)).replaceAll("") + "\n");
                }

                for (String text: orderContainer) {
                    try { insertText(text, JTextPaneTextAlign.CENTER); }
                    catch (BadLocationException e) { System.out.println(e.getMessage()); };
                }

                previousInputText = inputText;
            }
        }

        private static List<String> getComponentClassNames() {
            return Arrays.stream(
                    Objects.requireNonNull(
                            new File("./customSwing").listFiles(
                                    (dir, itemFilename) ->
                                            itemFilename.startsWith("J") && Character.isUpperCase(itemFilename.charAt(1))))
                    ).map(itemFile -> {
                        String itemFileName = itemFile.getName();
                        return itemFileName.substring(0, itemFileName.indexOf(".java"));
                    }).collect(Collectors.toList());
        }

        public static void main(String[] args) throws IOException {
            SwingConsole.runAndCenter(new ShowAddListeners(), 800, 400);
            try {
                Class<?> cls = ClassLoader.getSystemClassLoader().loadClass("spring.testClassLoader_loadClassAndClass_forNameDifference.Test");
                System.out.println(Arrays.toString(cls.getMethods()));
                Class<?> cls2 = Class.forName("spring.testClassLoader_loadClassAndClass_forNameDifference.Test");
                System.out.println(cls.getName());
            } catch (ClassNotFoundException e) { e.printStackTrace(); }
        }
    }

    private interface Generator<T> { T next(); }
    private static class BasicGenerator<T> implements Generator<T> {
        private final Class<T> type;
        public BasicGenerator(Class<T> type) { this.type = type; }
        @Override public T next() {
            try {
                return (T) type.getDeclaredConstructors()[0].newInstance();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        public static <T> Generator<T> create(Class<T> type) { return new BasicGenerator<>(type); }
    }
    private static class ExchangerProducer<T> implements Runnable {
        private final Generator<T> generator;
        private final Exchanger<List<T>> exchanger;
        private List<T> holder;
        public ExchangerProducer(Exchanger<List<T>> exchanger, Generator<T> generator, List<T> holder) {
            this.exchanger = exchanger;
            this.generator = generator;
            this.holder = holder;
        }
        @Override public void run() {
            try {
                while (!Thread.interrupted()) {
                    for (int i = 0; i < 10; i++) holder.add(generator.next());
                    holder = exchanger.exchange(holder);
                }
            } catch (InterruptedException e) { System.out.println(e.getMessage()); }
        }
    }

    private static class ExchangerConsumer<T> implements Runnable {
        private final Exchanger<List<T>> exchanger;
        private List<T> holder;
        private volatile T value;
        public ExchangerConsumer(Exchanger<List<T>> exchanger, List<T> holder) {
            this.exchanger = exchanger;
            this.holder = holder;
        }
        @Override public void run() {
            try {
                while (!Thread.interrupted()) {
                    holder = exchanger.exchange(holder);
                    for (T x: holder) {
                        value = x;
                        holder.remove(x);
                    }
                }
            } catch (InterruptedException e) { System.out.println(e.getMessage()); }
            System.out.println("Final value: " + value);
        }
    }

    private static class ExchangerDemo {
        public static void main(String[] args) throws InterruptedException {
            ExecutorService exec = Executors.newCachedThreadPool();
            Exchanger<List<Fat>> xc = new Exchanger<>();
            List<Fat> producerList = new CopyOnWriteArrayList<>(), consumerList = new CopyOnWriteArrayList<>();
            exec.execute(new ExchangerProducer<>(xc, BasicGenerator.create(Fat.class), producerList));
            exec.execute(new ExchangerConsumer<>(xc, consumerList));
            TimeUnit.SECONDS.sleep(2);
            exec.shutdownNow();
        }
    }

    private static class Horse implements Runnable {
        private static int counter = 0;
        private final int id = counter++;
        private int strides = 0;
        private static final Random random = new Random(47);
        private static CyclicBarrier barrier;
        public Horse(CyclicBarrier b) { barrier = b; }
        public synchronized int getStrides() { return strides; }
        @Override public void run() {
            try {
                while (!Thread.interrupted()) {
                    synchronized (this) {
                        strides += random.nextInt(3);   // 模拟速度
                    }
                    barrier.await();
                }
            } catch (InterruptedException | BrokenBarrierException e) { throw new RuntimeException(e); }
        }
        @Override public String toString() { return "Horse " + id + " "; }
        public String tracks() {    // 模拟赛马跑出的距离
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < strides; i++) sb.append("*");
            sb.append(id);
            return sb.toString();
        }
    }

    private static class HorseRace {
        private static final int FINISH_LINE = 75;
        private final List<Horse> horses = new ArrayList<>();
        private final ExecutorService exec = Executors.newCachedThreadPool();
        private final CyclicBarrier barrier;
        public HorseRace(int nHorses, final int pause) {
            barrier = new CyclicBarrier(nHorses, () -> {
                printArea();
                printDistanceOfRaceHorse();
                printResult(pause);
            });
            initRaceHorse(nHorses);
        }

        private void printArea() {  // 模拟场地边界
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < FINISH_LINE; i++) sb.append("=");
            System.out.println(sb);
        }
        // 赛马跑出的距离
        private void printDistanceOfRaceHorse() { for (Horse horse: horses) System.out.println(horse.tracks()); }
        private void printResult(int pauseTime) {   // 宣布结果
            for (Horse horse: horses) {
                if (horse.getStrides() >= FINISH_LINE) {
                    System.out.println(horse + "won!");
                    exec.shutdownNow();
                    return;
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(pauseTime);
            } catch (InterruptedException e) {
                System.out.println("barrier-action sleep interrupted");
            }
        }

        private void initRaceHorse(int nHorses) {
            for (int i = 0; i < nHorses; i++) {
                Horse horse = new Horse(barrier);
                horses.add(horse);
                exec.execute(horse);
            }
        }

        public static void main(String[] args) { new HorseRace(7, 200); }
    }

    private static class DeadlockingDiningPhilosophers {
        public static void main(String[] args) throws IOException {
            ExecutorService exec = Executors.newCachedThreadPool();
            int size = 5, ponderFactor = 0;
            Chopstick[] chopsticks = new Chopstick[size];
            for (int i = 0; i < size; i++) chopsticks[i] = new Chopstick();
            for (int i = 0; i < size; i++) exec.execute(new Philosopher(chopsticks[i], chopsticks[(i + 1) % size], i, ponderFactor));
            System.out.println("Press 'Enter' to quit...");
            System.in.read();
            exec.shutdownNow();
        }
    }

    private static class Chopstick {    // 筷子
        private boolean taken = false;
        public synchronized void take() throws InterruptedException {
            while (taken) wait();
            taken = true;
        }

        public synchronized void drop() throws InterruptedException {
            taken = false;
            notifyAll();
        }
    }

    private static class Philosopher implements Runnable {
        private final Chopstick left;
        private final Chopstick right;
        private final int name;
        private final int ponderFactor;
        private final Random rand = new Random(47);
        public Philosopher(Chopstick left, Chopstick right, int name, int ponderFactor) {
            this.left = left;
            this.right = right;
            this.name = name;
            this.ponderFactor = ponderFactor;
        }
        private void pause() throws InterruptedException {
            if (ponderFactor == 0) return;
            TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor * 250));
        }
        private void philosopherTake(Chopstick chopstick, String direction) throws InterruptedException {
            System.out.printf("%s %s %s%n", this, "grabbing", direction);
            chopstick.take();
        }

        @Override public void run() {
            try {
                while (!Thread.interrupted()) {
                    System.out.println(this + " " + "thinking");
                    pause();
                    philosopherTake(right, "right");
                    philosopherTake(left, "left");
                    System.out.println(this + " eating");
                    pause();
                    right.drop();
                    left.drop();
                }
            } catch (InterruptedException e) { System.out.println(this + " exiting via interrupt"); }
        }
        @Override public String toString() { return "Philosopher" + name; }
    }

    private static class Restaurant {
        private Meal meal;
        private final WaitPerson waitPerson = new WaitPerson();
        private final Chef chef = new Chef();
        private final ExecutorService exec = Executors.newCachedThreadPool();

        public Restaurant() {
            exec.execute(chef);
            exec.execute(waitPerson);
        }

        private static class Meal {
            private final int orderNum;
            public Meal(int orderNum) { this.orderNum = orderNum; }
            @Override public String toString() { return "Meal [orderNum=" + orderNum + "]"; }
        }

        private class WaitPerson implements Runnable {
            @Override public void run() {
                try {
                    while (!Thread.interrupted()) {
                        synchronized (this) {
                            while (meal == null)
                                wait();
                        }
                        System.out.println("WaitPerson got " + meal);
                        synchronized (chef) {
                            meal = null;
                            chef.notifyAll();
                        }
                    }
                } catch (InterruptedException e) { System.out.println("WaitPerson interrupted"); }
            }
        }

        private class Chef implements Runnable {
            private int count = 0;
            @Override public void run() {
                try {
                    while (!Thread.interrupted()) {
                        synchronized (this) {
                            while (meal != null)
                                wait();
                        }
                        if (++count == 10) {
                            System.out.println("Out of food, closing");
                            exec.shutdownNow();
                        }
                        System.out.print("Order up! ");
                        synchronized (waitPerson) {
                            meal = new Meal(count);
                            waitPerson.notifyAll();
                        }
                        TimeUnit.SECONDS.sleep(1);
                    }
                } catch (InterruptedException e) { System.out.println("Chef interrupted"); }
            }
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

            class LeastRecentlyPrincipleLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
                private final BiFunction<LinkedHashMap<K, V>, Map.Entry<K, V>, Boolean> allowCache;
                public LeastRecentlyPrincipleLinkedHashMap(
                        boolean accessOrder,
                        BiFunction<LinkedHashMap<K, V>, Map.Entry<K, V>, Boolean> allowCache
                ) {
                    super(16, 0.75f, accessOrder);
                    this.allowCache = allowCache;
                }

                // 覆写removeEldestEntry，使其让LinkedHashMap有最近最少使用原则功能
                // 方法返回的布尔值代表这个最近最少原则所使用的Cache是多大，超过Cache之后要删除Cache内的第一次添加的Entry
                @Override public boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                    if (Objects.isNull(allowCache)) return super.removeEldestEntry(eldest);
                    return allowCache.apply(this, eldest);
                }
            }

            LinkedHashMap<String, Integer> leastRecentlyPrincipleLinkedHashMap =
                    new LeastRecentlyPrincipleLinkedHashMap<>(true, (map, eldest) -> map.size() > 5);
            for (int i = 0; i < 5; i++) leastRecentlyPrincipleLinkedHashMap.put(String.valueOf(i), (int) Character.forDigit(i, 10));
            System.out.println(Arrays.toString(leastRecentlyPrincipleLinkedHashMap.entrySet().toArray()));
            leastRecentlyPrincipleLinkedHashMap.get("0");
            leastRecentlyPrincipleLinkedHashMap.get("0");
            leastRecentlyPrincipleLinkedHashMap.get("1");
            leastRecentlyPrincipleLinkedHashMap.get("0");
            leastRecentlyPrincipleLinkedHashMap.put("5", 53);
            System.out.println(Arrays.toString(leastRecentlyPrincipleLinkedHashMap.entrySet().toArray()));
//            int[][] lala = {{ 1, 1, 1}, { 0, 1, 1 }, { 2, 1, 1 }};
//            int[][] lala2 = {{ 2, 5, 1 }, { 1, 1, 1 }, { 1, 1, 1}};
//            System.out.println(Arrays.deepToString(matrixMultiplication(lala, lala2)));

            int[][][] lala = {{{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}}};
            int[][] lala2 = {{2, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
            System.out.println(Arrays.deepToString(matrixMultiplication(lala, lala2)));

            // 将一组结果放到ExecutorCompletionService中，使其能根据返回结果的顺序进行遍历
            ExecutorService pool = Executors.newCachedThreadPool();
            ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(pool);
            for (int i = 0; i < 10; i++) {
                int finalI = i;
                completionService.submit(() -> {
                    TimeUnit.SECONDS.sleep(finalI);
                    return (Integer) finalI;
                });
            }

            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println(completionService.take().get());
                } catch (InterruptedException e) { e.printStackTrace(); } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } finally {
                    pool.shutdown();
                }
            }
        }
    }

    private static Object[] matrixMultiplication(Object matrix1, Object matrix2) {
        int matrix1Len = getLength(matrix1);
        Object[][] result = new Object[matrix1Len][];
        if (matrix1.getClass().isArray() && matrix2.getClass().isArray()) {
            for (int row = 0; row < matrix1Len; row++) {
                Object obj1 = getArrayElement(matrix1, row);
                Object obj2 = getArrayElement(matrix2, 0);

                boolean matrix1IsArray = Array.get(obj1, 0).getClass().isArray();
                boolean matrix2IsArray = Array.get(obj2, 0).getClass().isArray();
                if (matrix1IsArray && matrix2IsArray) {
                    result = (Object[][]) matrixMultiplication(obj1, obj2);
                } else if (matrix1IsArray) {
                    result = (Object[][]) matrixMultiplication(obj1, matrix2);
                } else if (matrix2IsArray) {
                    result = (Object[][]) matrixMultiplication(matrix1, obj2);
                } else {
                    int len = getLength(matrix2);
                    if (len != getLength(getArrayElement(matrix1, row)))
                        throw new IllegalArgumentException("The number of columns in the multiplier must be the same as " +
                                "the number of rows in the multiplicand");
                    int elementLen = getLength(obj2);
                    result[row] = new Object[elementLen];
                    for (int column = 0; column < elementLen; column++) {
                        int val = 0;
                        for (int k = 0; k < len; k++) {
                            // 矩阵x * 矩阵y -> x的列数 * y的行数
                            val += convertInt(obj1, k) * convertInt(Array.get(matrix2, k), column);
                        }
                        result[row][column] = val;
                    }
                }
            }
        }
        return result;
    }

    private static Object getArrayElement(Object obj, int index) { return ((Object[]) obj)[index]; }
    private static int convertInt(Object val, int index) { return ((int[]) val)[index]; }
    private static int getLength(Object val) { return Array.getLength(val); }

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
