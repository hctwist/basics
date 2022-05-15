package uk.henrytwist.kotlinbasics.outcomes

/**
 * An outcome that can either be pending, failed or successful.
 */
sealed class Outcome<out T> {
    /**
     * An outcome that has not yet been fulfilled.
     */
    object Pending : Outcome<Nothing>()

    /**
     * A successful outcome with the resulting data ([data]).
     */
    class Success<T>(val data: T) : Outcome<T>()

    /**
     * A failed outcome. This can be inherited from to provide more failure details.
     */
    open class Failure : Outcome<Nothing>()

    /**
     * Maps a successful outcome via [transform].
     * If this outcome is either [Outcome.Pending] or [Outcome.Failure] then the mapping has no effect.
     */
    inline fun <O> map(transform: (T) -> O): Outcome<O> {
        return when (this) {
            is Pending -> this
            is Success -> Success(transform(data))
            is Failure -> this
        }
    }

    /**
     * Switches a successful outcome to another via [transform].
     * If this outcome is either [Outcome.Pending] or [Outcome.Failure] then the switching has no effect.
     */
    inline fun <O> switchMap(transform: (T) -> Outcome<O>): Outcome<O> {
        return when (this) {
            is Pending -> this
            is Success -> transform(data)
            is Failure -> this
        }
    }

    /**
     * Performs an action if the outcome was successful.
     */
    inline fun ifSuccessful(then: (T) -> Unit) {
        if (this is Success) then(data)
    }
}