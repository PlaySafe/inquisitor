package org.companion.inquisitor;

import data.Address;
import data.DefaultAddress;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class ValidatorTest {

    Map<String, ValidationRule> validators;

    public ValidatorTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_validator.xml");
        File configResource = new File("src/test/resources/validator_test_cases.xml");
        MetaData metaData = new MetaValidatorFactory().compile(metaResource);
        ValidatorFactory validatorFactory = new ValidatorFactory(metaData);
        Map<String, ValidationRule> validators = validatorFactory.compile(configResource);
        Assert.assertNotNull(validators);

        this.validators = validators;
    }

    @Test
    public void expectValidAddressForNotNullCity() {
        Address notNullCityAddress = new DefaultAddress.Builder()
                .setCity("ABC")
                .build();
        ValidationRule validationRule = validators.get("NOT_NULL_CITY_TEST");
        Assert.assertNotNull(validationRule);
        Assert.assertTrue(validationRule.validate(notNullCityAddress));
    }

    @Test
    public void expectValidAddressForNullPostalCode() {
        Address nullAddress = new DefaultAddress.Builder()
                .setPostalCode(null)
                .build();
        ValidationRule validationRule = validators.get("NULL_POSTAL_CODE_TEST");
        Assert.assertNotNull(validationRule);
        Assert.assertTrue(validationRule.validate(nullAddress));
    }

    @Test
    public void expectValidStateForFirstCharacterIsLetter() {
        Address address = new DefaultAddress.Builder()
                .setState("Silicon Valley")
                .build();
        ValidationRule validationRule = validators.get("FIRST_STATE_IS_LETTER");
        Assert.assertNotNull(validationRule);
        Assert.assertTrue(validationRule.validate(address));
    }

    @Test
    public void expectValidStreetMinLength() {
        System.setProperty("min.street.length", "5");
        Address address = new DefaultAddress.Builder()
                .setStreet("On my street")
                .build();
        ValidationRule validationRule = validators.get("MIN_STREET_LENGTH_PROPERTIES");
        Assert.assertNotNull(validationRule);
        Assert.assertTrue(validationRule.validate(address));
    }

    @Test
    public void expectValidPostalCodeFixMaxLength() {
        Address address = new DefaultAddress.Builder()
                .setPostalCode("74859")
                .build();
        ValidationRule validationRule = validators.get("MAX_POSTAL_CODE_LENGTH_FIX");
        Assert.assertNotNull(validationRule);
        Assert.assertTrue(validationRule.validate(address));
    }

    @Test
    public void expectValidStateMaxLengthDefinition() {
        Address address = new DefaultAddress.Builder()
                .setState("My State")
                .build();
        ValidationRule validationRule = validators.get("MAX_LENGTH_STATE_LESS_THAN_IN_DEFINITION");
        Assert.assertNotNull(validationRule);
        Assert.assertTrue(validationRule.validate(address));
    }

    @Test
    public void expectValidGreaterThanHardCode() {
        ValidationRule validationRule = validators.get("GREATER_THAN_HARDCODE");
        Assert.assertNotNull(validationRule);
        Assert.assertTrue(validationRule.validate(null));
    }

    @Test
    public void expectValidFirst5CharsOfHelloWord() {
        ValidationRule validationRule = validators.get("FIRST_5_CHARS_OF_HELLO_WORLD_EQUALS_HELLO");
        Assert.assertNotNull(validationRule);
        Assert.assertTrue(validationRule.validate(null));
    }

    @Test
    public void expectValidLast6CharsOfHelloWorld() {
        ValidationRule validationRule = validators.get("LAST_6_CHARS_OF_HELLO_WORLD_NOT_EQUALS_WORLD");
        Assert.assertNotNull(validationRule);
        Assert.assertTrue(validationRule.validate(null));
    }

    @Test
    public void expectInvalidWhenNoValidationRuleMatch() {
        ValidationRule validationRule = validators.get("FIRST_4_CHARS_OF_HELLO_WORLD_NOT_EQUALS_HELLO");
        Assert.assertNotNull(validationRule);
        Assert.assertFalse(validationRule.validate(null));
    }

    @Test
    public void expectInvalidWhenExpectSpaceAsNoText(){
        ValidationRule validationRule = validators.get("CONSIDER_HAS_TEXT_FOR_A_SPACE");
        Assert.assertNotNull(validationRule);
        Assert.assertFalse(validationRule.validate(null));
    }
}
