package spring.testRef.util;

public class Person {
    private Fruit fruit;

    @Override
    public String toString() {
        return "Person{" +
                "fruit=" + fruit.getName() +
                '}';
    }

    public void setFruit(Fruit fruit) {
        this.fruit = fruit;
    }

    public Fruit getFruit() {
        return fruit;
    }
}
