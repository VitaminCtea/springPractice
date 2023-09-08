package spring.testRef;

import javax.validation.Validator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.util.Date;

public class TestNumberFormatAnnotationFormatterFactory {

    @Autowired
    private Validator validator;

    private String name;
    @NumberFormat(pattern = "###,##0") private final int total;
    @NumberFormat(style = NumberFormat.Style.PERCENT) private final double money;
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date date;

    public TestNumberFormatAnnotationFormatterFactory(int total, double money) {
        this.total = total;
        this.money = money;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TestNumberFormatAnnotationFormatterFactory{" +
                "validator=" + validator +
                ", total=" + total +
                ", money=" + money +
                ", date=" + date +
                '}';
    }

    @NotBlank
    public String getName() {
        return name;
    }

    public @NotNull Object myValidMethod(@NotNull String arg1, @Max(10) int arg2) { return null; }
}
