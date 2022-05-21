package uk.henrytwist.kotlinbasics.groupcomparator

/**
 * A builder for a [GroupComparator].
 */
class GroupComparatorBuilder<T> {
    /**
     * The groups.
     */
    private val groups = mutableListOf<Group<T>>()

    /**
     * Adds a group.
     */
    fun group(predicate: (T) -> Boolean, build: ComparatorBuilder<T>.() -> Unit = {}) {
        val builder = ComparatorBuilder<T>()
        build(builder)
        groups.add(BasicPredicateGroup(predicate, builder.comparators))
    }

    /**
     * Adds a remainder group.
     */
    fun remainderGroup(build: ComparatorBuilder<T>.() -> Unit = {}) {
        val builder = ComparatorBuilder<T>()
        build(builder)
        groups.add(Group.RemainderGroup(builder.comparators))
    }

    /**
     * Adds a container for nested groups.
     */
    @ExperimentalNestedGroups
    fun groups(predicate: (T) -> Boolean, build: GroupComparatorBuilder<T>.() -> Unit) {
        val builder = GroupComparatorBuilder<T>()
        build(builder)
        groups.add(NestedGroup(predicate, builder.build()))
    }

    /**
     * Builds the [GroupComparator].
     */
    internal fun build() = GroupComparator(groups)
}

/**
 * A builder for multiple comparators.
 */
class ComparatorBuilder<T> {
    /**
     * The comparators.
     */
    internal val comparators = mutableListOf<Comparator<T>>()

    /**
     * Adds a comparator.
     */
    fun sortedBy(comparator: Comparator<T>) {
        comparators.add(comparator)
    }

    /**
     * Adds a comparator based on a selected property.
     */
    fun <S> sortedBy(nullsFirst: Boolean = false, descending: Boolean = false, selector: (T) -> S?) where S : Comparable<S> {
        sortedBy(SelectedComparator(nullsFirst, descending, selector))
    }
}