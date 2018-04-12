import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun main(args: Array<String>) {

    println(greetings)

    val inputs: List<String> = readFromConsole()

    val preferences: List<Preference> = inputs.map { each -> convertToPreferences(each) }

    val customers: List<Customer> = makeCustomers(preferences)

    val subscriptions: List<DaySubscriptions> = subscribersForRangeOfDates(
        customers = customers,
        startDate = LocalDate.of(2018, 3, 31)
    )

    val report: String = compileReport(subscriptions)

    println(report)

}

val greetings: String =
    """
        |Welcome to Customer Preference Centre
        |
        |Please enter a series of subscription preferences - one for each customer.
        |
        |Each customer's input will be terminated by pressing [Enter].
        |Inputs not following conventions will be discarded.
        |The conventions for entering preferences for a single customer are:
        |
        |   1. One of [01-28] to subscribe for a given day each month (Mind the preceding '0' for single digits)
        |   2. One or more of [MON-SUN] to subscribe for one or more week days (separated by a space character)
        |   3. 'Daily' to subscribe for every day
        |   4. 'Never' to not subscribe at all
        |
        |
        |Press [Enter] without any input to produce the report.
        |
    """.trimMargin()


fun readFromConsole(
    result: List<String?> = emptyList()
): List<String> {

    print("Enter customer preference > ")

    return readLine().let { input: String? ->
        if (input == "") result.filterNotNull()
        else readFromConsole(result.plus(input))
    }
}

sealed class Preference {
    class DayOfMonth(val value: Int) : Preference()
    class Weekdays(val days: List<DayOfWeek>) : Preference()
    object Daily : Preference()
    object Never : Preference()
    object Unknown : Preference()
}

fun convertToPreferences(
    input: String,
    dayOfMonthPattern: Regex = "0[0-9]|1[0-9]|2[0-8]".toRegex(),
    weekDayPattern: Regex = "MON|TUE|WED|THU|FRI|SAT|SUN".toRegex(),
    dailyPattern: Regex = "Daily".toRegex(),
    neverPattern: Regex = "Never".toRegex()
): Preference =

    when {
        dayOfMonthPattern in input -> Preference.DayOfMonth(input.toInt())
        weekDayPattern in input -> input
            .split(delimiters = " ")
            .mapNotNull { day: String -> stringToDayOfWeek[day] }
            .let { days: List<DayOfWeek> -> Preference.Weekdays(days) }
        dailyPattern in input -> Preference.Daily
        neverPattern in input -> Preference.Never
        else -> Preference.Unknown
    }

val stringToDayOfWeek: Map<String, DayOfWeek> = mapOf(
    "MON" to DayOfWeek.MONDAY,
    "TUE" to DayOfWeek.TUESDAY,
    "WED" to DayOfWeek.WEDNESDAY,
    "THU" to DayOfWeek.THURSDAY,
    "FRI" to DayOfWeek.FRIDAY,
    "SAT" to DayOfWeek.SATURDAY,
    "SUN" to DayOfWeek.SUNDAY
)

fun makeCustomers(
    preferences: List<Preference>,
    customerIds: CharIterator = ('A'..'Z').iterator()
): List<Customer> =

    preferences
        .filterNot { preference -> preference === Preference.Unknown }
        .map { preference -> Customer(customerIds.nextChar(), preference) }


data class Customer(val id: Char, val preferences: Preference)


fun subscribersForRangeOfDates(
    customers: List<Customer>,
    startDate: LocalDate = LocalDate.now(),
    endDate: LocalDate = startDate.plusDays(90),
    result: List<DaySubscriptions> = emptyList()
): List<DaySubscriptions> =

    if (startDate == endDate) result
    else subscribersForRangeOfDates(
        customers = customers,
        startDate = startDate.plusDays(1),
        endDate = endDate,
        result = result.plus(
            subscribersForSingleDate(date = startDate.plusDays(1), customers = customers)
        )
    )


data class DaySubscriptions(val date: LocalDate, val subscribers: List<Customer>)


fun subscribersForSingleDate(
    date: LocalDate,
    customers: List<Customer>
): DaySubscriptions =

    DaySubscriptions(
        date = date,
        subscribers = customers.mapNotNull { customer ->

            when (customer.preferences) {

                is Preference.DayOfMonth ->
                    if (date.dayOfMonth == customer.preferences.value) customer
                    else null

                is Preference.Weekdays -> {
                    if (date.dayOfWeek in customer.preferences.days) customer
                    else null
                }
                is Preference.Daily -> customer
                is Preference.Never -> null
                is Preference.Unknown -> null
            }
        }
    )

fun compileReport(
    subscriptions: List<DaySubscriptions>,
    formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("E dd-MMMM-yyyy")
): String {

    val stringBuilder = StringBuilder()

    for (daySubscriptions in subscriptions) {
        val date = daySubscriptions.date.format(formatter)
        val subscriberIds = daySubscriptions.subscribers
            .map { subscriber -> subscriber.id }
            .toString()
            .trim('[', ']')

        stringBuilder.appendln("$date   $subscriberIds")
    }

    return stringBuilder.toString()
}