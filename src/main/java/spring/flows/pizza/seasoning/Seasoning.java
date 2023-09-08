package spring.flows.pizza.seasoning;

import org.apache.commons.lang.WordUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public enum Seasoning implements Serializable {
    PEPPERONI,
    SAUSAGE,
    HAMBURGER,
    MUSHROOM,
    CANADIAN_BACON,
    PINEAPPLE,
    GREEN_PEPPER,
    JALAPENO,
    TOMATO,
    ONION,
    EXTRA_CHEESE;

    public static List<Seasoning> asList() { return Arrays.asList(Seasoning.values()); }

    @Override
    public String toString() {
        return WordUtils.capitalizeFully(name().replace('_', ' '));
    }
}
