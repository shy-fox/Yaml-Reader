package io.shiromi.saml.annotations;

import java.lang.annotation.ElementType;

import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.RECORD_COMPONENT})
public @interface SerializedItem {
    String name() default DEFAULT_NAME;

    String DEFAULT_NAME = "varName";
}
