package spring.flows.pizza.service.payment;

import org.springframework.stereotype.Component;
import spring.flows.pizza.exception.PaymentException;

public class PaymentProcessor {
    public PaymentProcessor() {}

    public void approveCreditCard(String creditCardNumber, String expMonth, String expYear, float amount) throws PaymentException {
        if (amount > 20.00) throw new PaymentException();
    }
}
