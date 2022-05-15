package uk.henrytwist.kotlinbasics.groupcomparator

class GroupComparatorBuilder<T> {
    private val groups = mutableListOf<Group<T>>()

    fun group(predicate: (T) -> Boolean, build: ComparatorGroupBuilder<T>.() -> Unit = {}) {
        val builder = ComparatorGroupBuilder<T>()
        build(builder)
        groups.add(BasicPredicateGroup(predicate, builder.comparators))
    }

    fun remainderGroup(build: ComparatorGroupBuilder<T>.() -> Unit = {}) {
        val builder = ComparatorGroupBuilder<T>()
        build(builder)
        groups.add(Group.RemainderGroup(builder.comparators))
    }

    @ExperimentalNestedGroups
    fun groups(predicate: (T) -> Boolean, build: GroupComparatorBuilder<T>.() -> Unit) {
        val builder = GroupComparatorBuilder<T>()
        build(builder)
        groups.add(NestedGroup(predicate, builder.build()))
    }

    internal fun build() = GroupComparator(groups)
}

class ComparatorGroupBuilder<T> {
    internal val comparators = mutableListOf<Comparator<T>>()

    fun sortedBy(comparator: Comparator<T>) {
        comparators.add(comparator)
    }

    fun <S> sortedBy(nullsFirst: Boolean = false, descending: Boolean = false, selector: (T) -> S?) where S : Comparable<S> {
        sortedBy(SelectedComparator(nullsFirst, descending, selector))
    }
}