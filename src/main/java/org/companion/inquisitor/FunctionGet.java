package org.companion.inquisitor;

import java.util.Map;

/**
 * Retrieve the value from the definition, properties, specific field, or the value itself corresponds to the configuration.
 */
class FunctionGet implements Function {

    private final Function delegateFunction;

    public FunctionGet(FunctionDefinition definition) {
        String param = definition.getParameter1();
        if (VariableReflector.isProperties(param)) {
            delegateFunction = new FunctionGetProperties(param);
        }
        else if (VariableReflector.isField(param)) {
            delegateFunction = new FunctionGetValue(param);
        }
        else if (VariableReflector.isDefinition(param)) {
            delegateFunction = new FunctionGetDefinition(param);
        }
        else {
            delegateFunction = new FunctionReturn(param);
        }
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) {
        return delegateFunction.perform(input, definitions);
    }

    /**
     * Retrieve value from the definitions corresponds to the definition pattern
     */
    private class FunctionGetDefinition implements Function {

        private String definitionKey;
        private String definitionName;

        FunctionGetDefinition(String definition) {
            this.definitionKey = VariableReflector.reflectDefinitionKeyOf(definition);
            this.definitionName = VariableReflector.reflectDefinitionNameOf(definition);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            Map<String, Object> items = definitions.get(definitionName);
            if (items != null) {
                Object value = items.get(definitionKey);
                return String.valueOf(value);
            }
            return null;
        }
    }

    private class FunctionGetValue implements Function {

        private Function getValueFromField;
        private Function getValueFromMap;

        FunctionGetValue(String field) {
            getValueFromField = new FunctionGetMethodField(field);
            getValueFromMap = new FunctionGetFromMap(field);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            return (input instanceof Map) ? getValueFromMap.perform(input, definitions) :
                    getValueFromField.perform(input, definitions);
        }
    }

    private class FunctionGetMethodField implements Function {

        private String methodName;

        FunctionGetMethodField(String field) {
            this.methodName = VariableReflector.reflectFieldMethodOf(field);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            String value = VariableReflector.invoke(input, methodName);
            return (value == null) ? null : value;
        }
    }

    private class FunctionGetFromMap implements Function {

        private String key;

        FunctionGetFromMap(String key) {
            this.key = VariableReflector.reflectFieldOf(key);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            Object data = ((Map<String, ?>) input).get(key);
            return (data == null) ? null : String.valueOf(data);
        }
    }

    private class FunctionGetProperties implements Function {

        private String propertiesKey;

        FunctionGetProperties(String properties) {
            this.propertiesKey = VariableReflector.reflectPropertiesOf(properties);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            return System.getProperty(propertiesKey);
        }
    }

    private class FunctionReturn implements Function {

        private final String value;

        FunctionReturn(String value) {
            this.value = value;
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            return value;
        }
    }


}
