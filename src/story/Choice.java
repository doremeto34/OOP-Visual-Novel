package story;

import java.util.function.BooleanSupplier; 

public class Choice {
    private String text;
    private Runnable action;
    private BooleanSupplier condition;

    public Choice(String text, Runnable action) {
        this(text, action, () -> true); 
    }

    public Choice(String text, Runnable action, BooleanSupplier condition) {
        this.text = text;
        this.action = action;
        this.condition = condition;
    }
    
    public String getText() {
    	return text;
    }
    public Runnable getAction() {
    	return action;
    }
    public BooleanSupplier getCondition() {
    	return condition;
    }
}
