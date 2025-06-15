package story;

import java.util.function.BooleanSupplier; 

public class Choice {
    public String text;
    public Runnable action;
    public BooleanSupplier condition;

    public Choice(String text, Runnable action) {
        this(text, action, () -> true); 
    }

    public Choice(String text, Runnable action, BooleanSupplier condition) {
        this.text = text;
        this.action = action;
        this.condition = condition;
    }
}
