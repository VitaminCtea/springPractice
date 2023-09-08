package spring.testRef.propertyEditor;

import java.beans.PropertyEditorSupport;

public class ExoticTypeEditor extends PropertyEditorSupport {
    // 覆写父类的setAsText方法，这里需要注意的是，在父类中的setAsText方法内调用了setValue方法，所以在子类覆写的时候也需要正确的进行调用这个方法，以便正确的进行赋值
    @Override public void setAsText(String text) throws IllegalArgumentException {
        StringBuilder sb = new StringBuilder();
        for (char c: text.toCharArray()) {
            if (Character.isUpperCase(c)) sb.append("_");
            sb.append(Character.toUpperCase(c));
        }
        setValue(new ExoticType(sb.toString()));
    }
}
