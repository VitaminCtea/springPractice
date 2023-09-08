package spring.flows.pizza.flow;

import static spring.flows.pizza.pay.type.PaymentType.CREDIT_CARD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.flows.pizza.customer.Customer;
import spring.flows.pizza.order.Order;
import spring.flows.pizza.pay.Payment;
import spring.flows.pizza.pay.details.PaymentDetails;
import spring.flows.pizza.pay.mode.CashOrCheckPayment;
import spring.flows.pizza.pay.mode.CreditCardPayment;
import spring.flows.pizza.service.customer.CustomerNotFoundException;
import spring.flows.pizza.service.customer.CustomerService;

@Component
public class PizzaFlowActions {
    private static final Logger LOGGER = LoggerFactory.getLogger(PizzaFlowActions.class);
    @Autowired
    private CustomerService customerService;

    public Customer lookupCustomer(String phoneNumber) throws CustomerNotFoundException {
        Customer customer = customerService.lookupCustomer(phoneNumber);
        if (customer != null) {
            return customer;
        } else {
            throw new CustomerNotFoundException();
        }
    }

    public void addCustomer(Customer customer) {
        LOGGER.warn("TODO: Flesh out the addCustomer() method.");
    }
    public Payment verifyPayment(PaymentDetails paymentDetails) {
        Payment payment = null;
        if (paymentDetails.getPaymentType() == CREDIT_CARD) {
            payment = new CreditCardPayment();
        } else {
            payment = new CashOrCheckPayment();
        }

        return payment;
    }

    public void saveOrder(Order order) {
        LOGGER.warn("TODO: Flesh out the saveOrder() method.");
    }
    public boolean checkDeliveryArea(String zipCode) {
        System.out.println("zipCode is: " + zipCode);
        LOGGER.warn("TODO: Flesh out the checkDeliveryArea() method.");
        return "75075".equals(zipCode);
    }
}
