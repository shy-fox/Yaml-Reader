package io.shiromi.saml.tools;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public interface Matcher {
    static boolean matches(String string, Pattern expression) {
        return matcher(string, expression).matches();
    }

    @Contract("_, _ -> new")
    static java.util.regex.@NotNull Matcher matcher(String string, @NotNull Pattern expression) {
        return expression.matcher(string);
    }

    static boolean isValidName(@NotNull String name) {
        return name.matches("^(-|<<)?[A-Za-z][\\w- ]*");
    }
}
