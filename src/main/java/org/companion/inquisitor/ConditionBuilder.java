package org.companion.inquisitor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class ConditionBuilder {

    private final Map<String, Class<? extends Condition>> conditionLogicMap;

    ConditionBuilder(Map<String, Class<? extends Condition>> conditionLogicMap) {
        this.conditionLogicMap = Collections.unmodifiableMap(new HashMap<>(conditionLogicMap));
    }

    /**
     * @param definition the condition definition
     * @return a new instance of condition corresponds to the logic of {@link ConditionDefinition}.
     *
     * @throws IllegalArgumentException if the logic doesn't match to any condition class
     */
    Condition build(ConditionDefinition definition) {
        String logic = definition.getLogic();
        Class<? extends Condition> conditionClass = conditionLogicMap.get(logic);
        if (conditionClass == null) {
            throw new IllegalArgumentException("There is no condition : " + logic);
        }
        try {
            return conditionClass.getConstructor(definition.getClass()).newInstance(definition);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
