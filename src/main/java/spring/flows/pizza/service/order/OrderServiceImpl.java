package spring.flows.pizza.service.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.flows.pizza.order.Order;

public class OrderServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    public OrderServiceImpl() {}

    public void saveOrder(Order order) {
        LOGGER.debug("SAVING ORDER:  " );
        LOGGER.debug("   Customer:  " + order.getCustomer().getName());
        LOGGER.debug("   # of Pizzas:  " + order.getPizzas().size());
        LOGGER.debug("   Payment:  " + order.getPayment());
    }
}
