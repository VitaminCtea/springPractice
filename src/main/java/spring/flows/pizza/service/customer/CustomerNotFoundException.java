package spring.flows.pizza.service.customer;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException() {}
    public CustomerNotFoundException(String message) { super(message); }
}
