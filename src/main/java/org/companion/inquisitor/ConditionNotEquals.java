package org.companion.inquisitor;

import java.util.Map;

class ConditionNotEquals implements Condition {

    private final Function function1;
    private final Function function2;

    public ConditionNotEquals(ConditionDefinition definition) {
        this.function1 = definition.getValue1();
        this.function2 = definition.getValue2();
    }

    @Override
    public boolean perform(Object input, Map<String, Map<String, Object>> definitions) {
        String result1 = function1.perform(input, definitions);
        String result2 = function2.perform(input, definitions);
        return !result1.equals(result2);
    }
}
