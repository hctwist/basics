package uk.henrytwist.kotlinbasics.treecomparator

/**
 * A comparator which can be built up of multiple nested levels of comparison.
 * For example, consider a list of employees which should be sorted with managers first (sorted by rank), then standard employees (sorted by first then last name).
 * A sort for this scenario with a [TreeComparator] could look like this:
 * ```
 * val comparator = TreeComparator<Employee>()
 * comparator.group {
 *  predicate { it.isManager }
 *  sort { it.rank }
 * }
 * comparator.sort { it.firstName }
 * comparator.sort { it.lastName }
 * ```
 */
open class TreeComparator<T> : Comparator<T> {
    /**
     * The comparators.
     */
    private val comparators = mutableListOf<Comparator<T>>()

    /**
     * Adds a comparator.
     */
    fun sort(comparator: Comparator<T>) {
        comparators.add(comparator)
    }

    /**
     * Adds a comparator from a selector.
     * If [nullsFirst] is true, then null values will be sorted before any non-null values.
     * If [descending] is true, then elements will be sorted in descending order.
     */
    fun <S> sort(nullsFirst: Boolean = false, descending: Boolean = false, selector: (T) -> S?) where S : Comparable<S> {
        comparators.add(SelectedComparator(nullsFirst, descending, selector))
    }

    /**
     * Adds a group to this comparison.
     * This group will be sorted separately.
     */
    fun group(builder: GroupComparator<T>.() -> Unit) {
        val groupComparator = GroupComparator<T>()
        builder(groupComparator)
        comparators.add(groupComparator)
    }

    override fun compare(a: T, b: T): Int {
        comparators.forEach {
            val diff = it.compare(a, b)
            if (diff != 0) return diff
        }

        return 0
    }
}

/**
 * A comparator that compares items based on a selected value.
 * If [nullsFirst] is true, then null values will be sorted before any non-null values.
 * If [descending] is true, then elements will be sorted in descending order.
 */
class SelectedComparator<T, S>(private val nullsFirst: Boolean, private val descending: Boolean, private val selector: (T) -> S?) : Comparator<T> where S : Comparable<S>  {
    override fun compare(a: T, b: T): Int {

        val selectedA = selector(a)
        val selectedB = selector(b)

        return if (selectedA == null && selectedB == null) {
            0
        } else if (selectedA != null && selectedB != null) {
            (if (descending) -1 else 1) * selectedA.compareTo(selectedB)
        } else {
            if ((selectedA == null) == nullsFirst) -1 else 1
        }
    }
}

/**
 * A group which considers elements based on a predicate.
 */
class GroupComparator<T> : TreeComparator<T>(), Comparator<T> {
    /**
     * The predicate.
     */
    private var predicate: (T) -> Boolean = { false }

    /**
     * Sets the predicate for this group. If no predicate is specified, then this group will have no effect.
     */
    fun predicate(p: (T) -> Boolean) {
        predicate = p
    }

    override fun compare(a: T, b: T): Int {
        val aInGroup = predicate(a)
        val bInGroup = predicate(b)

        return if (aInGroup != bInGroup) {
            if (aInGroup) -1 else 1
        } else if (!aInGroup && !bInGroup) {
            0
        } else {
            super.compare(a, b)
        }
    }
}