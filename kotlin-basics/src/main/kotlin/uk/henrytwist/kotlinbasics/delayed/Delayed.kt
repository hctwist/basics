package uk.henrytwist.kotlinbasics.delayed

/**
 * A result which can be waiting.
 */
sealed class Delayed<out T> {
    /**
     * A delayed that is currently waiting.
     */
    object Waiting : Delayed<Nothing>()

    /**
     * A delayed that has completed with [data].
     */
    class Completed<T>(val data: T) : Delayed<T>()

    /**
     * Maps a completed delayed via [transform].
     * If this delayed is [Delayed.Waiting] then the mapping has no effect.
     */
    inline fun <O> map(transform: (T) -> O): Delayed<O> {
        return when (this) {
            is Waiting -> this
            is Completed -> Completed(transform(data))
        }
    }
}