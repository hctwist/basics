package uk.henrytwist.kotlinbasics.treecomparator

/**
 * Creates a tree comparator.
 */
inline fun <T> treeComparator(builder: TreeComparator<T>.() -> Unit): TreeComparator<T> {
    val comparator = TreeComparator<T>()
    builder(comparator)
    return comparator
}

/**
 * Sorts with a tree comparator.
 */
inline fun <T> Iterable<T>.sortedWithTreeComparator(builder: TreeComparator<T>.() -> Unit): List<T> {
    return sortedWith(treeComparator(builder))
}