package pl.gajowy.phonebook.domain;

import javax.validation.Validation;

//enum implementation of singleton pattern
//see http://stackoverflow.com/questions/70689/efficient-way-to-implement-singleton-pattern-in-java
public enum Validator {
    instance(Validation.buildDefaultValidatorFactory().getValidator());

    private final javax.validation.Validator validator;

    public javax.validation.Validator get() {
        return validator;
    }

    Validator(javax.validation.Validator validator) {
        this.validator = validator;
    }
}
