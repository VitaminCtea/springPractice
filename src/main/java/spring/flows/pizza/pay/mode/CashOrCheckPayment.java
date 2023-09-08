package spring.flows.pizza.pay.mode;


import spring.flows.pizza.pay.Payment;

public class CashOrCheckPayment extends Payment {
  public CashOrCheckPayment() {}
  public String toString() {
    return "CASH or CHECK:  $" + getAmount();
  }
}
