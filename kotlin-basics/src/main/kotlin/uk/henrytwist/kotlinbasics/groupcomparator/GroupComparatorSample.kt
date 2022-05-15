package uk.henrytwist.kotlinbasics.groupcomparator

internal object GroupComparatorSample {
    private data class Employee(val role: Role, val performanceScore: Int, val firstName: String, val lastName: String)
    private enum class Role {
        Manager, Trainee
    }

    private val employees: List<Employee>
        get() = throw NotImplementedError()

    private val performanceComparator: Comparator<Employee>
        get() = throw NotImplementedError()

    fun sample() {
        employees.sortedWithGroupComparator {
            // Sort the managers to appear first - ordered by an external performance metric
            group({ it.role == Role.Manager }) {
                sortedBy(performanceComparator)
            }

            // All other employees can fall into the middle - sorted by their first name, then last name
            remainderGroup {
                sortedBy { it.firstName }
                sortedBy { it.lastName }
            }

            // Group trainees at the bottom
            group({ it.role == Role.Trainee })
        }
    }
}