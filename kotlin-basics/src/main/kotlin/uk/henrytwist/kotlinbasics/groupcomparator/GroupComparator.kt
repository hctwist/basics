package uk.henrytwist.kotlinbasics.groupcomparator

import uk.henrytwist.kotlinbasics.indexOfFirstOrNull

/**
 * A comparator that can sort items by grouping them and sorting within the groups.
 */
internal class GroupComparator<T>(private val groups: List<Group<T>>) : Comparator<T> {
    /**
     * The index of the first remainder group in [groups]. If there isn't an explicit remainder group, then this is null.
     */
    private val remainderGroupIndex = groups.indexOfFirstOrNull { it is Group.RemainderGroup }

    override fun compare(o1: T, o2: T): Int {
        var o1PredicateGroupIndex: Int? = null
        var o2PredicateGroupIndex: Int? = null

        // Go through all the predicate groups
        for ((index, group) in groups.withIndex()) {
            if (group !is Group.PredicateGroup) {
                continue
            }

            // Check if we still need to find groups for either element
            if (o1PredicateGroupIndex == null && group.shouldContain(o1)) {
                o1PredicateGroupIndex = index
            }
            if (o2PredicateGroupIndex == null && group.shouldContain(o2)) {
                o2PredicateGroupIndex = index
            }

            if (o1PredicateGroupIndex != null && o2PredicateGroupIndex != null) break
        }

        // If the elements haven't found a group, then they should be placed in the remainder group
        val o1GroupIndex = o1PredicateGroupIndex ?: remainderGroupIndex
        val o2GroupIndex = o2PredicateGroupIndex ?: remainderGroupIndex

        return if (o1GroupIndex == null && o2GroupIndex == null) {
            // They must both be in the implicit remainder group
            0
        }
        else if (o1GroupIndex == null) {
            // o2 is in the implicit remainder group, so after o1
            1
        }
        else if (o2GroupIndex == null) {
            // o1 is in the implicit remainder group, so after o2
            -1
        }
        else if (o1GroupIndex == o2GroupIndex) {
            // They are in the same group, so the group should handle comparison
            groups[o1GroupIndex].compare(o1, o2)
        }
        else {
            // They're in different groups
            o1GroupIndex - o2GroupIndex
        }
    }
}

/**
 * Builds a [GroupComparator]. This can sort elements based on groups which all have their own sorting rules.
 */
fun <T> groupComparator(build: GroupComparatorBuilder<T>.() -> Unit): Comparator<T> {
    val builder = GroupComparatorBuilder<T>()
    build(builder)
    return builder.build()
}

/**
 * Returns a list of elements sorted according to a group comparator.
 * @see groupComparator
 * @sample GroupComparatorSample.sample
 */
fun <T> Iterable<T>.sortedWithGroupComparator(build: GroupComparatorBuilder<T>.() -> Unit): List<T> {
    return sortedWith(groupComparator(build))
}