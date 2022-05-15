package uk.henrytwist.kotlinbasics

/**
 * Returns index of the first element matching the given [predicate], or null if the list does not contain such element.
 * @see indexOfFirst
 */
inline fun <T> Iterable<T>.indexOfFirstOrNull(predicate: (T) -> Boolean) = indexOfFirst(predicate).takeIf { it >= 0 }

fun testLib(): Int {
    return 5;
}