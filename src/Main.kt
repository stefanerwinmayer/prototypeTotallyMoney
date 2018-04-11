import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter


sealed class Preference {
    class DayOfMonth(val value: Int) : Preference()
    class Weekdays(val days: List<DayOfWeek>) : Preference()
    object Daily : Preference()
    object Never : Preference()
    object Unknown : Preference()
}

val dayOfWeekMap = mapOf(
    "MON" to DayOfWeek.MONDAY,
    "TUE" to DayOfWeek.TUESDAY,
    "WED" to DayOfWeek.WEDNESDAY,
    "THU" to DayOfWeek.THURSDAY,
    "FRI" to DayOfWeek.FRIDAY,
    "SAT" to DayOfWeek.SATURDAY,
    "SUN" to DayOfWeek.SUNDAY
)

data class Customer(val tag: Char, val preferences: Preference)
data class DailyReport(val date: LocalDate, val subscribers: List<Customer>)

fun main(args: Array<String>) {

    greet()

    val inputs = readFromConsole()

    val preferences = inputs
        .filterNotNull()
        .map { input -> getPreferences(input) }

    val alphabet = ('A'..'Z').iterator()

    val customers = preferences
        .filterNot { it === Preference.Unknown }
        .map { preference -> Customer(alphabet.nextChar(), preference) }

    val startDate = LocalDate.of(2018, 3, 31)
    val fullReport = produceFullReport(startDate = startDate, customers = customers)

    printReport(fullReport)

}

fun greet() {
    println(
        """
        |Welcome to Customer Preference Centre
        |
        |Please enter a series of subscription getPreferences - one for each customer.
        |You can enter:
        |   1. [1-28] for date of month based subscriptions
        |   2. [MONDAY-S
        |   3. 'Daily' for every day subscriptions
        |   4. 'Never' to no subscription
        |
        |Each customer's input will be terminated by pressing [Enter].
        |Press [Enter] without any input to produce the report.
        |Input not following either will be ignored.
        |
        |
        |
    """.trimMargin()
    )
}

fun printReport(report: List<DailyReport>) {

    for (daily in report) {
        println(
            "${daily.date.format(DateTimeFormatter.ofPattern("E dd-MMMM-yyyy"))}" +
                    "   ${daily.subscribers
                        .map { it.tag }
                        .toString()
                        .trim('[', ']')
                    }"
        )
    }
}

fun produceFullReport(
    startDate: LocalDate = LocalDate.now(),
    endDate: LocalDate = startDate.plusDays(90),
    customers: List<Customer>,
    result: List<DailyReport> = emptyList()
): List<DailyReport> =

    if (startDate == endDate) result
    else produceFullReport(
        startDate = startDate.plusDays(1),
        endDate = endDate,
        customers = customers,
        result = result.plus(
            producDailyReport(
                date = startDate.plusDays(1),
                customers = customers
            )
        )
    )


fun producDailyReport(
    date: LocalDate,
    customers: List<Customer>
): DailyReport =

    DailyReport(
        date = date,
        subscribers = customers.mapNotNull { customer ->
            when (customer.preferences) {
                is Preference.DayOfMonth -> if (date.dayOfMonth == customer.preferences.value) customer else null
                is Preference.Weekdays -> if (date.dayOfWeek in customer.preferences.days) customer else null
                is Preference.Daily -> customer
                is Preference.Never -> null
                is Preference.Unknown -> null
            }
        }
    )


fun getPreferences(
    input: String,
    dayOfMonthPattern: Regex = "[0-9]".toRegex(),
    weekDayPattern: Regex = "MON|TUE|WED|THU|FRI|SAT|SUN".toRegex(),
    dailyPattern: Regex = "Daily".toRegex(),
    neverPattern: Regex = "Never".toRegex()
): Preference =

    when {
        dayOfMonthPattern in input -> Preference.DayOfMonth(input.toInt())
        weekDayPattern in input -> input
            .split(" ")
            .mapNotNull { day -> dayOfWeekMap[day] }
            .let { days -> Preference.Weekdays(days) }
        dailyPattern in input -> Preference.Daily
        neverPattern in input -> Preference.Never
        else -> Preference.Unknown
    }

fun readFromConsole(result: List<String?> = emptyList()): List<String?> =
    readLine().let { input ->
        if (input == "") result
        else readFromConsole(result.plus(input))
    }







