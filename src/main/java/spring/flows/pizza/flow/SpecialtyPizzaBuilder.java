package spring.flows.pizza.flow;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import spring.flows.pizza.desc.Pizza;
import spring.flows.pizza.seasoning.Seasoning;

public class SpecialtyPizzaBuilder implements Action {
   private static final Logger LOGGER = LoggerFactory.getLogger(SpecialtyPizzaBuilder.class);

   public Event execute(RequestContext request) throws Exception {
      String type = request.getRequestParameters().get("pizzaType");

      LOGGER.debug("BUILDING A SPECIALTY PIZZA:  " + type);

      Pizza pizza = (Pizza) request.getFlowScope().get("pizza");
      if ("MEAT".equals(type)) {
         LOGGER.debug("BUILDING A CARNIVORE");

         List<Seasoning> meats = new ArrayList<>();

         meats.add(Seasoning.CANADIAN_BACON);
         meats.add(Seasoning.HAMBURGER);
         meats.add(Seasoning.PEPPERONI);
         meats.add(Seasoning.SAUSAGE);

         pizza.setToppings(meats);
      } else if ("VEGGIE".equals(type)) {
         LOGGER.debug("BUILDING A HERBIVORE");
         List<Seasoning> meats = new ArrayList<>();

         meats.add(Seasoning.GREEN_PEPPER);
         meats.add(Seasoning.MUSHROOM);
         meats.add(Seasoning.PINEAPPLE);
         meats.add(Seasoning.TOMATO);

         pizza.setToppings(meats);
      } else if ("THEWORKS".equals(type)) {
         LOGGER.debug("BUILDING AN OMNIVORE");

         List<Seasoning> meats = new ArrayList<>();
         System.out.println("THE WORKS!");

         meats.add(Seasoning.CANADIAN_BACON);
         meats.add(Seasoning.HAMBURGER);
         meats.add(Seasoning.PEPPERONI);
         meats.add(Seasoning.SAUSAGE);
         meats.add(Seasoning.GREEN_PEPPER);
         meats.add(Seasoning.MUSHROOM);
         meats.add(Seasoning.PINEAPPLE);
         meats.add(Seasoning.TOMATO);
         meats.add(Seasoning.EXTRA_CHEESE);
         meats.add(Seasoning.ONION);
         meats.add(Seasoning.JALAPENO);

         pizza.setToppings(meats);
      }

      request.getFlowScope().put("pizza", pizza);

      return new Event(this, "success");
   }
}
