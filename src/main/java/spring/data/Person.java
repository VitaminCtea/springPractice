package spring.data;

import org.hibernate.validator.constraints.Range;

public class Person {
    private String name;

    @Range(min = 1, max = 100, message = "age字段最小为1，最大为100，您所输入的值不在此范围内，请重新进行输入！")
    private int age;
    private String email;
    private String date;

    public Person() {}

    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public Person(String name, int age, String email, String date) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", date=" + date +
                '}';
    }
}
