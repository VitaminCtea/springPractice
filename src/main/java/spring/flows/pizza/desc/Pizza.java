package spring.flows.pizza.desc;

import spring.flows.pizza.seasoning.Seasoning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pizza implements Serializable {
    private static final long serialVersionUID = -490984874007152761L;
    private PizzaSize size = PizzaSize.LARGE;
    private List<Seasoning> seasonings = new ArrayList<>();

    public PizzaSize getSize() { return size; }
    public void setSize(PizzaSize size) { this.size = size; }

    public List<Seasoning> getToppings() { return seasonings; }
    public void setToppings(List<Seasoning> seasonings) { this.seasonings = seasonings; }

    public void addToppings(String[] ts) { for (String t: ts) seasonings.add(Seasoning.valueOf(t)); }
}
