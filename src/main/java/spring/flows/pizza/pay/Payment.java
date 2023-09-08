package spring.flows.pizza.pay;

import java.io.Serializable;

public class Payment implements Serializable {
    private static final long serialVersionUID = 8327209733485078068L;
    private float amount;

    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount = amount; }
}
