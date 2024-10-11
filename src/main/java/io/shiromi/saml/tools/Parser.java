package io.shiromi.saml.tools;

import io.shiromi.saml.elements.YamlElement;
import io.shiromi.saml.elements.YamlGenericElement;
import io.shiromi.saml.elements.YamlNumberElement;
import io.shiromi.saml.elements.YamlStringElement;
import io.shiromi.saml.exceptions.YamlParserException;
import io.shiromi.saml.types.NumberType;
import io.shiromi.saml.types.StringType;

import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Parser {
    private static final @RegExp String REGEX_STRING = "^\\s*([A-Za-z_]\\w*):\\s?\"([^\"]*)\"";
    private static final @RegExp String REGEX_NUMBER = "^^\\s*([A-Za-z_]\\w*):\\s?(\\d+(?:\\.\\d+)?)";

    /**
     * Parses an input {@code String} to a {@link io.shiromi.saml.elements.YamlStringElement} based on a given {@link #REGEX_STRING pattern}
     *
     * @param input the {@code String} to be parsed
     * @return a new instance of a {@code YamlStringElement} if matching the corresponding pattern.
     * @throws YamlParserException if the given input is of an illegal format
     * @see #parseFromStringType(StringType)
     * @since 1.9-02
     */
    @Contract("_ -> new")
    public static @NotNull YamlStringElement stringToYamlStringElement(String input) throws YamlParserException {
        Matcher m = Pattern.compile(REGEX_STRING).matcher(input);
        if (m.matches()) return new YamlStringElement(m.group(1), new StringType(m.group(2)));
        throw new YamlParserException("Illegal input for YamlStringElement," +
                " check input and make sure it matches the pattern: " + REGEX_STRING);
    }

    public static @NotNull YamlNumberElement stringToYamlNumberElement(String input) throws YamlParserException {
        Matcher m = Pattern.compile(REGEX_NUMBER).matcher(input);
        if (m.matches()) return new YamlNumberElement(m.group(1), NumberType.parse(m.group(2)));
        throw new YamlParserException("Illegal input for YamlNumberElement, " +
                " check input and make sure it matches the pattern: " + REGEX_NUMBER);
    }

    /**
     * Parses an input {@link StringType} to a {@link YamlStringElement} based on a given {@link #REGEX_STRING pattern}
     *
     * @param input the {@code StringType} to be parsed
     * @return a new instance of a {@code YamlStringElement} if matching the corresponding pattern
     * @throws YamlParserException if the given input is of an illegal format
     * @see #stringToYamlStringElement(String)
     * @since 1.9-02
     */
    @Contract("_ -> new")
    public static @NotNull YamlStringElement parseFromStringType(@NotNull StringType input) throws YamlParserException {
        return stringToYamlStringElement(input.toString());
    }

    public static @NotNull YamlGenericElement stringToYamlGenericElement(String input) throws YamlParserException {
        try {
            return YamlElement.genericElement(stringToYamlStringElement(input));
        } catch (YamlParserException e) {
            return new YamlGenericElement("empty", null);
        }
    }

    public static @NotNull YamlNumberElement parseFromNumberType(@NotNull NumberType input) throws YamlParserException {
        return stringToYamlNumberElement(input.toString());
    }
}
