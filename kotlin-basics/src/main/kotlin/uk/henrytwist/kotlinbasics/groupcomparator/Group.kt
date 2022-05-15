package uk.henrytwist.kotlinbasics.groupcomparator

/**
 * A group comparator group.
 */
internal sealed class Group<T>: Comparator<T> {
    /**
     * Compares two elements by the comparators in [comparators].
     */
    protected fun compareWithComparators(o1: T, o2: T, comparators: List<Comparator<T>>): Int {
        comparators.forEach {
            val diff = it.compare(o1, o2)
            if (diff != 0) return diff
        }

        return 0
    }

    /**
     * A group with a predicate determining which elements it should contain.
     */
    abstract class PredicateGroup<T>(private val predicate: (T) -> Boolean): Group<T>() {
        /**
         * Determines whether an element should be contained in this group.
         */
        fun shouldContain(element: T) = predicate(element)
    }

    /**
     * A group that should collect any remaining elements that don't fit into any other groups.
     */
    class RemainderGroup<T>(private val comparators: List<Comparator<T>>): Group<T>() {
        override fun compare(o1: T, o2: T): Int {
            return compareWithComparators(o1, o2, comparators)
        }
    }
}

/**
 * A predicate group with a list of comparators.
 */
internal class BasicPredicateGroup<T>(predicate: (T) -> Boolean, private val comparators: List<Comparator<T>>): Group.PredicateGroup<T>(predicate) {
    override fun compare(o1: T, o2: T): Int {
        return compareWithComparators(o1, o2, comparators)
    }
}

/**
 * A predicate group which contains other (nested) groups.
 */
@ExperimentalNestedGroups
internal class NestedGroup<T>(predicate: (T) -> Boolean, private val groupComparator: GroupComparator<T>): Group.PredicateGroup<T>(predicate) {
    override fun compare(o1: T, o2: T): Int {
        return groupComparator.compare(o1, o2)
    }
}