package uk.henrytwist.kotlinbasics

import uk.henrytwist.kotlinbasics.groupcomparator.sortedWithGroupComparator
import kotlin.test.Test
import kotlin.test.assertContentEquals

class GroupComparatorTests {
    @Test
    fun generalTest() {
        data class Employee(val isManager: Boolean, val isTrainee: Boolean, val firstName: String, val lastName: String, val income: Int)

        val manager1 = Employee(true, false, "Henry", "Twist", 100_000)
        val manager2 = Employee(true, false, "Abigail", "Hruzik", 40_000)

        val employee1 = Employee(false, false, "John", "Doe", 25_000)
        val employee2 = Employee(false, false, "John", "Smith", 10_000)

        val trainee = Employee(false, true, "Jake", "Bailey", 5_000)

        val expected = listOf(
            manager1,
            manager2,
            employee1,
            employee2,
            trainee
        )

        val unsorted = listOf(
            employee1,
            trainee,
            employee2,
            manager2,
            manager1
        )

        val sortedEmployees = unsorted.sortedWithGroupComparator {
            group({ it.isManager }) {
                sortedBy(descending = true) { it.income }
                sortedBy { it.firstName }
                sortedBy { it.lastName }
            }

            remainderGroup {
                sortedBy { it.firstName }
                sortedBy { it.lastName }
            }

            group({ it.isTrainee }) {
                sortedBy { it.firstName }
                sortedBy { it.lastName }
            }
        }

        assertContentEquals(expected, sortedEmployees)
    }

    @Test
    fun implicitRemainderGroupTest() {
        val expected = listOf(
            "Meerkat",
            "Rabbit",
            "Otter",
            "Jaguar",
            "Panther",
            "Dog",
            "Fish",
            "Cat"
        )

        val unsorted = listOf(
            "Meerkat",
            "Dog",
            "Rabbit",
            "Otter",
            "Fish",
            "Jaguar",
            "Panther",
            "Cat"
        )

        val sorted = unsorted.sortedWithGroupComparator {
            group({ it.length >= 5 })
        }

        assertContentEquals(expected, sorted)
    }
}