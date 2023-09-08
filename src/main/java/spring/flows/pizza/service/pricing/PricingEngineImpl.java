package spring.flows.pizza.service.pricing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spring.flows.pizza.desc.Pizza;
import spring.flows.pizza.desc.PizzaSize;
import spring.flows.pizza.order.Order;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PricingEngineImpl implements PricingEngine, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PricingEngineImpl.class);
    private static final long serialVersionUID = 4355526082289594452L;

    private static Map<PizzaSize, Float> SIZE_PRICES;
    static {
        SIZE_PRICES = new HashMap<>();
        SIZE_PRICES.put(PizzaSize.SMALL, 6.99f);
        SIZE_PRICES.put(PizzaSize.MEDIUM, 7.99f);
        SIZE_PRICES.put(PizzaSize.LARGE, 8.99f);
        SIZE_PRICES.put(PizzaSize.GINORMOUS, 9.99f);
    }
    private static final float PRICE_PER_TOPPING = 0.20f;

    public PricingEngineImpl() {}

    public float calculateOrderTotal(Order order) {
        LOGGER.debug("Calculating order total");

        float total = 0.0f;

        for (Pizza pizza : order.getPizzas()) {
            float pizzaPrice = SIZE_PRICES.get(pizza.getSize());
            if(pizza.getToppings().size() > 2) pizzaPrice += (pizza.getToppings().size() * PRICE_PER_TOPPING);
            total += pizzaPrice;
        }

        return total;
    }
}
