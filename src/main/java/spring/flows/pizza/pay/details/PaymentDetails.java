package spring.flows.pizza.pay.details;

import spring.flows.pizza.pay.type.PaymentType;

import java.io.Serializable;

public class PaymentDetails implements Serializable {
    private static final long serialVersionUID = 4230293566450372926L;
    private PaymentType paymentType;
    private String creditCardNumber;

    public PaymentType getPaymentType() { return paymentType; }
    public void setPaymentType(PaymentType paymentType) { this.paymentType = paymentType; }

    public String getCreditCardNumber() { return creditCardNumber; }
    public void setCreditCardNumber(String creditCardNumber) { this.creditCardNumber = creditCardNumber; }
}
