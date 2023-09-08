package spring.flows.pizza.pay.type;

import org.apache.commons.lang.WordUtils;

import java.util.Arrays;
import java.util.List;

// 支付选项
public enum PaymentType {
    CASH, CHECK, CREDIT_CARD;

    public static List<PaymentType> asList() { return Arrays.asList(PaymentType.values()); }

    @Override
    public String toString() {
        return WordUtils.capitalizeFully(name().replace('_', ' '));
    }
}
