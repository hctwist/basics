package uk.henrytwist.kotlinbasics.outcomes

/* Creation */

/**
 * Creates a [Outcome.Pending] outcome.
 */
fun pending() = Outcome.Pending

/**
 * Creates a [Outcome.Failure] outcome.
 */
fun failure() = Outcome.Failure()

/**
 * Creates an [Outcome.Success] outcome with this data.
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
    return if (this is Outcome.Failure) transform() else this
}

/**
 * Maps each element in a successful outcome via [transform].
 */
inline fun <T, O> Outcome<Iterable<T>>.mapEach(transform: (T) -> O) = map { it.map(transform) }