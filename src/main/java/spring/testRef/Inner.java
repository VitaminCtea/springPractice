package spring.testRef;

public class Inner {
    private final String name;
    private final int age;
    public Inner(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void print() { System.out.printf("I'm %s, I'm %d years old%n", name, age); }
}
