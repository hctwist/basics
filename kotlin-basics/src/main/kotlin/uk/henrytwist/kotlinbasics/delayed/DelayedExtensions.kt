package uk.henrytwist.kotlinbasics.delayed

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Creates a [Delayed.Waiting] delayed.
 */
fun waiting() = Delayed.Waiting

/**
 * Creates a [Delayed.Completed] delayed with this data.
 */
fun <T> T.asCompleted() = Delayed.Completed(this)

/**
 * Transforms a flow to emit completed values.
 */
fun <T> Flow<T>.emitCompleted() = map { it.asCompleted() }