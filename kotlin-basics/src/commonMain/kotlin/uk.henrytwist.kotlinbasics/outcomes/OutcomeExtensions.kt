package uk.henrytwist.kotlinbasics.outcomes

/* Creation */

/**
 * Creates a waiting outcome.
 */
fun waiting() = Outcome.Waiting

/**
 * Creates a failure outcome.
 */
fun failure() = Outcome.Failure()

/**
 * Creates a successful outcome with this data.
 */
fun <T> T.asSuccess() = Outcome.Success(this)


/* Mapping */

/**
 * Unboxes a successful outcome, or returns null.
 */
fun <T> Outcome<T>?.successOrNull() = if (this is Outcome.Success) data else null

/**
 * Switches a failed outcome to the outcome generated from [transform].
 * If this outcome is not a [Outcome.Failure] then it will not be switched.
 */
inline fun <T> Outcome<T>.switchFailure(transform: () -> Outcome<T>): Outcome<T> {
    return if (this is Outcome.Success) this else transform()
}

/**
 * Maps each element in a successful outcome via [transform].
 */
fun <T, O> Outcome<Iterable<T>>.mapEach(transform: (T) -> O) = map { it.map(transform) }