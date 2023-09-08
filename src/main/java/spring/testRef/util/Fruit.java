package spring.testRef.util;

public class Fruit {
    public static Fruit APPLE = new Fruit("APPLE");
    public static Fruit BANANA = new Fruit("BANANA");
    public static Fruit GRAPE = new Fruit("GRAPE");

    private final String name;

    public Fruit(String name) { this.name = name; }

    public String getName() { return name; }
}
