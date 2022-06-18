package uk.henrytwist.kotlinbasics.delayed

/**
 * Creates a [Delayed.Waiting] delayed.
 */
fun waiting() = Delayed.Waiting

/**
 * Creates a [Delayed.Completed] delayed with this data.
 */
fun <T> T.asCompleted() = Delayed.Completed(this)