package uk.henrytwist.kotlinbasics

/**
 * An event containing [content] which tracks whether it has already been consumed.
 */
open class Event<T>(private val content: T) {
    /**
     * Whether this event has been consumed.
     */
    var consumed = false
        private set

    /**
     * Peeks the content of this event without consuming it.
     */
    fun peek() = content

    /**
     * Consumes this event.
     */
    fun consume(): T {
        consumed = true
        return content
    }
}

/**
 * An event with no content.
 */
class Trigger : Event<Unit>(Unit)