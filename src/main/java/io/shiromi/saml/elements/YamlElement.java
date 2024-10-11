package io.shiromi.saml.elements;

import io.shiromi.saml.tools.ValuePair;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public abstract class YamlElement<T> extends AbstractElement<T> implements java.io.Serializable {

    public static final byte ELEMENT_ANY = 1,
            ELEMENT_ARRAY = 2,
            ELEMENT_TYPED_ARRAY = 3,
            ELEMENT_NUMBER_FLOAT = 4,
            ELEMENT_NUMBER_INT = 5,
            ELEMENT_NULL = 6,
            ELEMENT_OBJECT = 7,
            ELEMENT_STRING = 8;

    public YamlElement(String name) {
        super(name);
    }

    public ValuePair<T> asNode() {
        return new ValuePair<>(name, value);
    }

    public final T value() {
        return value;
    }

    public static @Nullable YamlElement<?> fromField(@NotNull final Field f, final Object o) {
        return fromField(f, o, null);
    }

    public final byte getType() {
        return getType(this);
    }

    public static byte getType(YamlElement<?> element) {
        if (element instanceof YamlStringElement) return ELEMENT_STRING;
        else if (element instanceof AbstractYamlArray<?>) return ELEMENT_ARRAY;
        return ELEMENT_ANY;
    }

    public static boolean typeEqual(@NotNull YamlElement<?> a, @NotNull YamlElement<?> b) {
        return a.getType() == b.getType();
    }

    public final boolean isType(@MagicConstant(intValues = {ELEMENT_STRING, ELEMENT_NUMBER_INT,
            ELEMENT_NUMBER_FLOAT, ELEMENT_ARRAY, ELEMENT_TYPED_ARRAY,
            ELEMENT_OBJECT, ELEMENT_NULL, ELEMENT_ANY}) byte type) {
        return this.getType() == type;
    }

    public static @Nullable YamlElement<?> fromField(@NotNull final Field f, final Object o, @Nullable String customName) {
        YamlGenericElement e;
        try {
            String name = customName != null ? customName : f.getName();
            e = new YamlGenericElement(name, f.get(o));
        } catch (IllegalAccessException ignored) {
            return null;
        }

        if (e.getTypeOfArray() == String.class ||
                e.getTypeOfArray() == char.class ||
                e.getTypeOfArray() == char[].class) {
            return stringElement(e);
        }

        return e;
    }

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    public static <T extends YamlElement<?>> @Nullable T createEmpty(@MagicConstant(intValues = {ELEMENT_STRING, ELEMENT_NUMBER_INT,
            ELEMENT_NUMBER_FLOAT, ELEMENT_ARRAY, ELEMENT_TYPED_ARRAY,
            ELEMENT_OBJECT, ELEMENT_NULL, ELEMENT_ANY}) byte type, @NotNull String name) {
        Class<T> classOfT = (Class<T>) switch (type) {
            case ELEMENT_STRING -> YamlStringElement.class;
            case ELEMENT_ARRAY -> AbstractYamlArray.class;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        return createEmpty(classOfT, name);
    }

    @SuppressWarnings("unchecked")
    public static <T extends YamlElement<?>> @Nullable T createEmpty(@NotNull Class<? extends YamlElement<?>> target, @NotNull String name) {
        Constructor<?>[] cs = target.getConstructors();

        for (Constructor<?> c : cs) {
            if (c.getParameterCount() == 1) try {
                return (T) c.newInstance(name);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                return null;
            }
        }

        return null;
    }

    @Contract("_ -> new")
    public static @NotNull YamlGenericElement genericElement(@NotNull YamlElement<?> e) {
        return e instanceof YamlGenericElement g ? g : new YamlGenericElement(e.name, e.value);
    }

    public static @NotNull YamlStringElement stringElement(@NotNull YamlElement<?> e) {
        return e instanceof YamlStringElement s ? s : new YamlStringElement(e.name, String.valueOf(e.value));
    }
}
