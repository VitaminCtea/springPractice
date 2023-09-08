package spring.testPrototype;

public abstract class SingletonBean {
    public void printTestPrototypeClassAddress() { System.out.println(createTestPrototype()); }
    protected abstract TestPrototype createTestPrototype();
}
