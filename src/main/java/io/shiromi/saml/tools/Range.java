package io.shiromi.saml.tools;

/**
 * Instances of the <em>record class</em> <code>Range</code> store
 * the range of a <code>String</code> match inside <code>StringType</code>,
 * this class also is used heavily inside {@link io.shiromi.saml.types.StringType#split(String, int) StringType.split(String, int)},
 * allowing splitting using a <code>String</code> delimiter.
 *
 * @param start the start index of the match
 * @param end   the end index of the match
 * @author Shiromi
 * @version 1.0
 * @see io.shiromi.saml.types.StringType StringType
 */
public record Range(int start, int end) {
    /**
     * Returns the start of the match
     *
     * @return the start of the match
     */
    public int start() {
        return start;
    }

    /**
     * Returns the end of the match
     *
     * @return the end of the match
     */
    public int end() {
        return end;
    }

    /**
     * Returns the length of the match, equal to either {@code end - start} or
     * <em>the length of the matched string</em>
     *
     * @return the length of the match
     */
    public int range() {
        return end - start;
    }

    /**
     * Returns a string representation of this object
     *
     * @return a string representation of this object
     */
    public String toString() {
        return String.format("Range { start: %s, end: %s; range: %s }", start, end, range());
    }
}
