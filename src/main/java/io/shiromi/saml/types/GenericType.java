package io.shiromi.saml.types;

import io.shiromi.saml.exceptions.CastException;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * Instances of the class <code>GenericType</code> carry a value of type <code>Object</code>, essentially
 * acting as an <code>Any</code> type, as the same suggests, this class' sole purpose is to allow interchangeability
 * between types and generification of types.
 *
 * @author Shiromi
 * @version 1.0
 */
public final class GenericType extends AbstractType<Object> {
    private Class<?> classOfT;

    /**
     * Creates a new instance of <code>GenericType</code> with the given <code>Object</code>
     *
     * @param value the value
     * @see #GenericType()
     */
    public GenericType(Object value) {
        super(value);
        classOfT = safeAssign(value);
    }

    /**
     * Creates a new instance of <code>GenericType</code> with a value if {@code null}
     *
     * @see #GenericType(Object)
     */
    public GenericType() {
        this(null);
    }

    /**
     * Checks if the value contained by this class is a <code>String</code>, use this to check for availability for
     * {@link #stringType() casting to StringType}
     * <blockquote>
     * <pre>{@code
     *      GenericType g = new GenericType("Foo");
     *      StringType s;
     *
     *      if (g.isString()) s = g.stringType();
     *     }</pre>
     * </blockquote>
     * However, it is not required to use this method as <code>GenericType</code> includes 2 methods for safe casting:
     * <ul>
     * <li>{@link #stringType(boolean)}</li>
     * <li>{@link #stringType(GenericType, boolean)}</li>
     * </ul>
     *
     * @return whether this value is either a <code>String</code>, {@code char} or {@code char[]} or not
     * @see #isBoolean()
     * @see #isNumber()
     * @see #isGeneric()
     */
    public boolean isString() {
        return classOfT == String.class ||
                classOfT == char.class ||
                classOfT == char[].class;
    }

    /**
     * Checks if the value contained by this class is a {@code boolean}, use this to check for availability for
     * {@link #booleanType() casting to BooleanType}
     * <blockquote>
     * <pre>{@code
     *      GenericType g = new GenericType(true);
     *      BooleanType b;
     *
     *      if (g.isBoolean()) b = g.booleanType();
     *     }</pre>
     * </blockquote>
     * However, it is not required to use this method as <code>GenericType</code> includes 2 methods for safe casting:
     * <ul>
     * <li>{@link #booleanType(boolean)}</li>
     * <li>{@link #booleanType(GenericType, boolean)}</li>
     * </ul>
     *
     * @return whether this value is a {@code boolean} or not
     * @see #isString()
     * @see #isNumber()
     * @see #isGeneric()
     */
    public boolean isBoolean() {
        return classOfT == boolean.class;
    }

    /**
     * Checks if the value contained by this class is a <code>Number</code>, use this to check for availability for
     * {@link #numberType() casting to NumberType}
     * <blockquote>
     * <pre>{@code
     *      GenericType g = new GenericType(10.4f);
     *      NumberType n;
     *
     *      if (g.isNumber()) n = g.numberType();
     *     }</pre>
     * </blockquote>
     *
     * @return whether this value is a <code>Number</code> or not
     * @see #isString()
     * @see #isBoolean()
     * @see #isGeneric()
     */
    public boolean isNumber() {
        return classOfT == byte.class ||
                classOfT == short.class ||
                classOfT == int.class ||
                classOfT == long.class ||
                classOfT == float.class ||
                classOfT == double.class;
    }

    /**
     * Checks if this object contains a non-specific type, speaking not <code>String</code>, {@code char} or {@code boolean},
     * essentially disallowing a further cast to other subtypes.
     *
     * @return whether this object contains a non-specific type or not
     * @see #isString()
     * @see #isBoolean()
     * @see #isString()
     */
    public boolean isGeneric() {
        return !(isString() ||
                isBoolean() ||
                isNumber());
    }

    @SuppressWarnings("unchecked")
    private <T> T castTo(@NotNull Class<T> classOfT) throws CastException {
        // check if it is a member of AbstractType
        if (classOfT.getSuperclass() != AbstractType.class)
            throw new CastException("Cast target has to be a subclass of AbstractType");

        if (classOfT == GenericType.class) return (T) this;

        Constructor<T> c;
        try {
            c = classOfT.getConstructor(this.classOfT);
        } catch (NoSuchMethodException e) {
            // shouldn't happen, but if it does, throw an error
            throw new IllegalStateException();
        }


        try {
            return c.newInstance(value);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new InternalError();
        } catch (IllegalArgumentException e) {
            throw new CastException("Cannot cast to " +
                    classOfT.getSimpleName() +
                    " as the value of this object is not of type " +
                    this.classOfT.getSimpleName() +
                    ", got " +
                    classOfT.getSimpleName() +
                    " instead.");
        }
    }

    /**
     * Attempts an <em>unsafe</em> cast to {@link StringType} with this object's value
     *
     * @return a <code>StringType</code> with the specified value if it succeeded
     * @throws CastException if the type of value contained by this object does not match to the values required by
     *                       <code>StringType</code>
     * @see #stringType(boolean)
     * @see #stringTypeSafe()
     */
    public @NotNull StringType stringType() throws CastException {
        return castTo(StringType.class);
    }

    /**
     * Attempts an <em>unsafe</em> cast to {@link BooleanType} with this object's value
     *
     * @return a <code>BooleanType</code> with the specified value if it succeeded
     * @throws CastException if the type of value contained by this object does not match to the values required by
     *                       <code>BooleanType</code>
     * @see #booleanType(boolean)
     * @see #booleanTypeSafe()
     */
    public @NotNull BooleanType booleanType() throws CastException {
        if (parsableBoolean()) return new BooleanType(castValue());
        return castTo(BooleanType.class);
    }

    /**
     * Attempts an <em>unsafe</em> cast to {@link NumberType} with this object's value
     *
     * @return a <code>NumberType</code> with the specified value if it succeeded
     * @throws CastException if the type of value contained by this object does not match to the values required by
     *                       <code>NumberType</code>
     * @see #numberType(boolean)
     * @see #numberTypeSafe()
     */
    public @NotNull NumberType numberType() throws CastException {
        return castTo(NumberType.class);
    }

    /**
     * Attempts an <em>unsafe</em> cast to {@link StringType} with the given object's value
     *
     * @param type the <code>GenericType</code> to cast
     * @return a <code>StringType</code> with the specified object's value if it succeeded
     * @throws CastException if the type of value contained by the given object does not match to the values required by
     *                       <code>StringType</code>
     * @see #stringType(GenericType, boolean)
     * @see #stringTypeSafe(GenericType)
     */
    public static @NotNull StringType stringType(@NotNull GenericType type) throws CastException {
        return type.castTo(StringType.class);
    }

    /**
     * Attempts an <em>unsafe</em> cast to {@link BooleanType} with the given object's value
     *
     * @param type the <code>GenericType</code> to cast
     * @return a <code>BooleanType</code> with the specified object's value if it succeeded
     * @throws CastException if the type of value contained by the given object does not match to the values required by
     *                       <code>BooleanType</code>
     * @see #booleanType(GenericType, boolean)
     * @see #booleanTypeSafe(GenericType)
     */
    public static @NotNull BooleanType booleanType(@NotNull GenericType type) throws CastException {
        return type.castTo(BooleanType.class);
    }

    /**
     * Attempts an <em>unsafe</em> cast to {@link NumberType} with the given object's value
     *
     * @param type the <code>GenericType</code> to cast
     * @return a <code>NumberType</code> with the specified object's value if it succeeded
     * @throws CastException if the type of value contained by the given object does not match to the values required by
     *                       <code>NumberType</code>
     * @see #numberType(GenericType, boolean)
     * @see #numberTypeSafe(GenericType)
     */
    public static @NotNull NumberType numberType(@NotNull GenericType type) throws CastException {
        return type.castTo(NumberType.class);
    }

    /**
     * A way to cast this object to a {@link StringType} with this object's value
     *
     * @param nullable allow for a {@code null} value to be returned if it failed
     * @return a <code>StringType</code> with the specified value if it succeeded {@code nullable} is not set, throws
     * an exception, if it is set, returns a {@code null} value
     * @see #stringType()
     * @see #stringTypeSafe()
     */
    public @Nullable StringType stringType(boolean nullable) {
        try {
            return stringType();
        } catch (CastException e) {
            if (nullable) return null;
            throw e;
        }
    }

    /**
     * A way to cast this object to a {@link BooleanType} with this object's value
     *
     * @param nullable allow for a {@code null} value to be returned if it failed
     * @return a <code>BooleanType</code> with the specified value if it succeeded {@code nullable} is not set, throws
     * an exception, if it is set, returns a {@code null} value
     * @see #booleanType()
     * @see #booleanTypeSafe()
     */
    public @Nullable BooleanType booleanType(boolean nullable) {
        try {
            return booleanType();
        } catch (CastException e) {
            if (nullable) return null;
            throw e;
        }
    }

    /**
     * A way to cast this object to a {@link NumberType} with this object's value
     *
     * @param nullable allow for a {@code null} value to be returned if it failed
     * @return a <code>NumberType</code> with the specified value if it succeeded {@code nullable} is not set, throws
     * an exception, if it is set, returns a {@code null} value
     * @see #numberType()
     * @see #numberTypeSafe()
     */
    public @Nullable NumberType numberType(boolean nullable) {
        try {
            return numberType();
        } catch (CastException e) {
            if (nullable) return null;
            throw e;
        }
    }

    /**
     * A way to cast the given object to a {@link StringType} with the given object's value
     *
     * @param nullable allow for a {@code null} value to be returned if it failed
     * @return a <code>StringType</code> with the specified value if it succeeded {@code nullable} is not set, throws
     * an exception, if it is set, returns a {@code null} value
     * @see #stringType(GenericType)
     * @see #stringTypeSafe(GenericType)
     */
    public static @Nullable StringType stringType(@NotNull GenericType type, boolean nullable) {
        return type.stringType(nullable);
    }

    /**
     * A way to cast the given object to a {@link BooleanType} with the given object's value
     *
     * @param nullable allow for a {@code null} value to be returned if it failed
     * @return a <code>BooleanType</code> with the specified value if it succeeded {@code nullable} is not set, throws
     * an exception, if it is set, returns a {@code null} value
     * @see #booleanType(GenericType)
     * @see #booleanTypeSafe(GenericType)
     */
    public static @Nullable BooleanType booleanType(@NotNull GenericType type, boolean nullable) {
        return type.booleanType(nullable);
    }

    /**
     * A way to cast the given object to a {@link NumberType} with the given object's value
     *
     * @param nullable allow for a {@code null} value to be returned if it failed
     * @return a <code>NumberType</code> with the specified value if it succeeded {@code nullable} is not set, throws
     * an exception, if it is set, returns a {@code null} value
     * @see #numberType(GenericType)
     * @see #numberTypeSafe(GenericType)
     */
    public static @Nullable NumberType numberType(@NotNull GenericType type, boolean nullable) {
        return type.numberType(nullable);
    }

    /**
     * A way to safely cast this object to a {@link StringType} with this object's value
     *
     * @return a <code>StringType</code> with this object's value if it succeeded, if it failed, returns {@code null}
     * @see #stringType()
     * @see #stringType(boolean)
     */
    public @Nullable StringType stringTypeSafe() {
        return stringType(true);
    }

    /**
     * A way to safely cast this object to a {@link BooleanType} with this object's value
     *
     * @return a <code>BooleanType</code> with this object's value if it succeeded, if it failed, returns {@code null}
     * @see #booleanType()
     * @see #booleanType(boolean)
     */
    public @Nullable BooleanType booleanTypeSafe() {
        return booleanType(true);
    }

    /**
     * A way to safely cast this object to a {@link NumberType} with this object's value
     *
     * @return a <code>NumberType</code> with this object's value if it succeeded, if it failed, returns {@code null}
     * @see #numberType()
     * @see #numberType(boolean)
     */
    public @Nullable NumberType numberTypeSafe() {
        return numberType(true);
    }

    /**
     * A way to safely cast the given object to a {@link StringType} with its value
     *
     * @return a <code>StringType</code> with the given object's value if it succeeded, if it failed, returns {@code null}
     * @see #stringType(GenericType)
     * @see #stringType(GenericType, boolean)
     */
    public static @Nullable StringType stringTypeSafe(GenericType type) {
        return stringType(type, true);
    }

    /**
     * A way to safely cast the given object to a {@link BooleanType} with its value
     *
     * @return a <code>BooleanType</code> with the given object's value if it succeeded, if it failed, returns {@code null}
     * @see #booleanType(GenericType)
     * @see #booleanType(GenericType, boolean)
     */
    public static @Nullable BooleanType booleanTypeSafe(GenericType type) {
        return booleanType(type, true);
    }

    /**
     * A way to safely cast the given object to a {@link NumberType} with its value
     *
     * @return a <code>NumberType</code> with the given object's value if it succeeded, if it failed, returns {@code null}
     * @see #numberType(GenericType)
     * @see #numberType(GenericType, boolean)
     */
    public static @Nullable NumberType numberTypeSafe(GenericType type) {
        return numberType(type, true);
    }

    private boolean parsableBoolean() {
        return (value instanceof Boolean || value instanceof String || value instanceof Number);
    }

    private boolean castValue() {
        if (value instanceof Boolean v) return v;
        else if (value instanceof Number n) return n.floatValue() > 0f;
        else if (value instanceof String s) return "true".equals(s);
        return false;
    }

    /**
     * Converts the given objects to <code>GenericType</code> and returns the array
     * @param objects an array of objects to convert
     * @return the array converted to an array of type <code>GenericObject</code>
     */
    public static GenericType @NotNull [] generics(Object @NotNull ... objects) {
        GenericType[] genericTypes = new GenericType[objects.length];
        for (int i = 0; i < objects.length; i++) genericTypes[i] = new GenericType(objects[i]);
        return genericTypes;
    }

    /**
     * Checks if this object is equal to another object, returns {@code null} if the given <code>Object</code>
     * is not a member of {@link AbstractType}
     *
     * @param other the object to compare, can be any member of <code>AbstractType</code>
     * @return whether the objects share the same value or not
     */
    public boolean equals(Object other) {
        if (other.getClass().getSuperclass() != AbstractType.class) return false;
        AbstractType<?> a = (AbstractType<?>) other;
        if (a.classOfT != classOfT) return false;
        return Objects.equals(value, a.value);

    }

    @Override
    public @NotNull GenericType clone() {
        try {
            GenericType g = (GenericType) super.clone();
            Object v = value;
            Class<?> classOfT = this.classOfT;

            g.value = v;
            g.classOfT = classOfT;

            return g;
        } catch (CloneNotSupportedException e) {
            // should really not trip this catch block, just to be sure, make it throw an error
            throw new InternalError();
        }
    }

    @Contract(" -> new")
    @Override
    public @NotNull GenericType copy() {
        Object v = value;
        return new GenericType(v);
    }

    private static Class<?> safeAssign(@Nullable Object value) {
        // safe null check
        if (value == null) return Object.class;

        Class<?> classOf = value.getClass();
        // Number check
        if (classOf.getSuperclass() == Number.class) return safeAssignNumber(value);
            // String check
        else if (safeAssignString(value) != null) return safeAssignString(value);
            // Boolean check
        else if (classOf == Boolean.class) return boolean.class;
        return Object.class;
    }

    private static Class<? extends Number> safeAssignNumber(@NotNull Object v) {
        String className = v.getClass().getSimpleName();
        return switch (className) {
            case "Byte" -> byte.class;
            case "Short" -> short.class;
            case "Integer" -> int.class;
            case "Long" -> long.class;
            case "Float" -> float.class;
            case "Double" -> double.class;
            default -> null;
        };
    }

    @Contract(pure = true)
    private static @Nullable Class<?> safeAssignString(@NotNull Object v) {
        if (v instanceof Character) return char.class;
        else if (v instanceof char[]) return char[].class;
        else if (v instanceof String) return String.class;
        else if (v instanceof StringBuilder) return StringBuilder.class;
        return null;
    }
}
