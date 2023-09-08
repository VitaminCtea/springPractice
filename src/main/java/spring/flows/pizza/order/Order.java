package spring.flows.pizza.order;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import spring.flows.pizza.customer.Customer;
import spring.flows.pizza.desc.Pizza;
import spring.flows.pizza.pay.Payment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class Order implements Serializable {
    private static final long serialVersionUID = 7308243387148833689L;
    private Customer customer = new Customer();
    private List<Pizza> pizzas = new ArrayList<>();
    private Payment payment;

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer;}
    public List<Pizza> getPizzas() { return pizzas; }
    public void setPizzas(List<Pizza> pizzas) { this.pizzas = pizzas; }
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
    public void addPizza(Pizza pizza) { pizzas.add(pizza); }
    public float getTotal() { return 0.0f; }
}
