package io.shiromi.saml.annotations;

import java.lang.annotation.ElementType;

import java.lang.annotation.Target;

/**
 * Mark a variable to carry a different name in file, as well as a possible conversion into a class variable,
 * e.g. using it to make a variable easier to understand in code:
 * <blockquote><pre>{@code
 * @SerializedItem(name="name") final String locationName;}</pre></blockquote>
 */
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.RECORD_COMPONENT})
public @interface SerializedItem {
    String name() default DEFAULT_NAME;

    String DEFAULT_NAME = "varName";
}
