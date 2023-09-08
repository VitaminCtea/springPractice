package spring.flows.pizza.service.pricing;

import spring.flows.pizza.order.Order;

public interface PricingEngine {
    public float calculateOrderTotal(Order order);
}
