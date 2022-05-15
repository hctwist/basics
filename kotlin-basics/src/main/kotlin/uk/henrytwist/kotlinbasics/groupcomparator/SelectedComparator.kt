package uk.henrytwist.kotlinbasics.groupcomparator

/**
 * A comparator that compares items based on a selected value.
 * If [nullsFirst] is true, then null values will be sorted before any non-null values.
 * If [descending] is true, then elements will be sorted in descending order.
 */
internal class SelectedComparator<T, S>(private val nullsFirst: Boolean, private val descending: Boolean, private val selector: (T) -> S?) : Comparator<T> where S : Comparable<S> {
    override fun compare(a: T, b: T): Int {
        val selectedA = selector(a)
        val selectedB = selector(b)

        return if (selectedA == null && selectedB == null) {
            0
        }
        else if (selectedA != null && selectedB != null) {
            (if (descending) -1 else 1) * selectedA.compareTo(selectedB)
        }
        else {
            if ((selectedA == null) == nullsFirst) -1 else 1
        }
    }
}