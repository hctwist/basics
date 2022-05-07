package uk.henrytwist.kotlinbasics

/**
 * Computes the fraction of [value] between [start] and [end].
 */
fun fractionBetween(value: Double, start: Double, end: Double): Double {
    return if (start < end) {
        (value - start) / (end - start)
    } else {
        1 - (value - end) / (start - end)
    }
}