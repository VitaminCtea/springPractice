package spring.flows.pizza.pay.mode;


import spring.flows.pizza.pay.Payment;

public class CreditCardPayment extends Payment {
  public CreditCardPayment() {}
  
  private String authorization;
  public void setAuthorization(String authorization) {
    this.authorization = authorization;
  }
  public String toString() {
    return "CREDIT:  $" + getAmount() + " ; AUTH: " + authorization;
  }
}
