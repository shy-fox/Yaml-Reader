package io.shiromi.samlkt

import org.jetbrains.annotations.Nullable

class DuplicateEntryError : Error {

    /**
     * Constructs a new `DuplicateEntryError` with no detail message given
     */
    constructor() : super()

    /**
     * Constructs a new `DuplicateEntryError` with the given detail message
     * @param s the detail message.
     */
    constructor(s: String) : super(s)

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * <p>Note that the detail message associated with {@code cause} is
     * *not* automatically incorporated in this exception's detail
     * message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the `Throwable#getMessage()` method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                `Throwable#getCause()` method).  (A `null` value
     *                is permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    constructor(message: String, @Nullable cause: Throwable) : super(message, cause)

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of `(cause==null ? null : cause.toString())` (which
     * typically contains the class and detail message of `cause`).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              `Throwable#getCause()` method).  (A `null` value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     */
    constructor(@Nullable cause: Throwable) : super(cause)
}