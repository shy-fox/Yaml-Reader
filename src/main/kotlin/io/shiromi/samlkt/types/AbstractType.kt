package io.shiromi.samlkt.types

import org.jetbrains.annotations.NotNull
import java.util.Objects
import kotlin.reflect.KClass

/**
 * Instances of the class `AbstractType` represent a type value assignable to a `YamlElement`'s value,
 * each separate type has its own methods and implementation, however they can all be cast to <a href="GenericType.kt">`GenericType`</a> as it has the
 * parameter `<T>` set to `Object`, therefore each subtype of this class can be cast to it.
 * <p></p>
 * Subtypes of this class behave a lot like their Java counterparts, e.g. {@link StringType} behaves similar to `String`,
 * this class itself only exists for generalization and unification of types throughout, as well as basic methods
 * implemented by each subclass.
 *
 * @author Shiromi
 * @since 2.0-11923-J_KT
 */
abstract class AbstractType<T : Any>
/**
 * @param v the value this type carries
 */ protected constructor(
    /**
     * The value of the type, usually set via the constructor, but allowed to be set via using the `set` method
     * @see set(T)
     */
    protected open var v: T,
) {

    /**
     * The `KClass` of the value, used mostly is `GenericType`
     */
    protected open var classOfT: KClass<out T> = v::class

    /**
     * Updates the field `v` to the new value
     * @param [newV] the value to update with
     * @see v
     */
    fun set(newV: T) {
        this.v = newV
    }

    /**
     * Retrieves the value of this instance
     * @return the value
     */
    fun getValue(): T {
        return this.v
    }

    /**
     * Returns a `String` representation of this object, carrying only the `simple name` of the class and value
     *
     * @return a `String` representation of this object
     */
    override fun toString(): String {
        return String.format("%s { v: %s }", this::class.simpleName, this.v)
    }

    /**
     * Returns a `String` representation of this object's value
     *
     * @return a `String` representation of this object's value
     * @see v
     */
    fun vToString(): String {
        return this.v.toString()
    }

    /**
     * Returns the hash code of this object
     *
     * @return the hash code of this object
     */
    override fun hashCode(): Int {
        return 31 * (this.classOfT.simpleName?.length ?: 0)
    }

    /**
     * Returns a hexadecimal representation of this object's hash code value returned by `hashCode`
     *
     * @return a hexadecimal representation of this object's hash code value
     * @see hashCode
     */
    fun hex(): String {
        return this.hex(false)
    }

    /**
     * Returns a hexadecimal representation starting with `0x`, if specified by setting `doPrefix` to true,
     * otherwise returns the default hexadecimal representation of this object's hash code value returned by {@link #hashCode()}
     *
     * @return a hexadecimal representation starting with `0x`, if specified by setting `doPrefix` to true,
     * otherwise returns the default hexadecimal representation of this object's hash code value
     * @see hex
     */
    fun hex(doPrefix: Boolean): String {
        val hex = this.toHexString(this.hashCode())
        return if (doPrefix) "0x$hex"
        else hex
    }

    fun hex(length: Int): String {
        val buf = java.lang.StringBuilder()
        val hex = this.hex()
        while (buf.length + hex.length < length) buf.append('0')
        return buf.append(hex).toString()
    }

    /**
     * Creates a deep copy of this object
     *
     * @return a deep copy of this object
     * @implNote Check class specifications for info
     */
    abstract fun copy(): AbstractType<T>

    /**
     * Returns the value of the specified object parsed to a char[]
     *
     * @return the value of the specified object parsed to a char[]
     * @since 2.0-11923-J
     */
    abstract fun toBuffer(): Array<Char>

    @NotNull
    private fun toHexString(i: Int): String {
        var j = i
        if (i < 0) j = -i
        var r: Int
        val sb = java.lang.StringBuilder()
        val hex = arrayOf(
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'A', 'B',
            'C', 'D', 'E', 'F'
        )

        while (j > 0) {
            r = j % 16
            sb.append(hex[r])
            j /= 16
        }

        return sb.toString()
    }

    /**
     * Checks if the given object is equal to this one
     *
     * @param other the object to compare
     * @return whether they are equal or not
     */
    @Suppress("UNCHECKED_CAST")
    override fun equals(other: Any?): Boolean {
        if (other == null || other::class.java.superclass != AbstractType::class.java) return false
        val a = other as AbstractType<*>

        val otherClassOfT = a.classOfT
        val classOfT = this.classOfT

        if (otherClassOfT != classOfT) return false

        val otherV: T = a.v as T

        return Objects.equals(otherV, this.v) && this.hashCode() == a.hashCode()
    }


}