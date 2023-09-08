package spring.factoryMethodComponent;

public class TestBean {
    private String country;
    private TestBean spouse;
    private String desc;
    private int age;

    public TestBean(String desc) { this(desc, 0); }
    public TestBean(String desc, int age) {
        this.desc = desc;
        this.age = age;
    }

    public void setSpouse(TestBean spouse) { this.spouse = spouse; }
    public void setCountry(String country) { this.country = country; }
    public void setAge(int age) { this.age = age; }

    public TestBean getSpouse() { return spouse; }
    public String getCountry() { return country; }
    public String getDesc() { return desc; }
    public int getAge() { return age; }

    @Override public String toString() {
        return "TestBean{" +
                "country='" + country + '\'' +
                ", spouse=" + spouse +
                ", desc='" + desc + '\'' +
                ", age=" + age +
                '}';
    }
}
