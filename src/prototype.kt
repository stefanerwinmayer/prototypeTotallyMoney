import Preferences.*
import java.time.DayOfWeek
import java.time.DayOfWeek.MONDAY
import java.time.LocalDate


sealed class InputTypes {
    object DateInput : InputTypes()
    object WeekDayInput : InputTypes()
    object NeverInput : InputTypes()
    object DailyInput : InputTypes()
    object SubmitInput : InputTypes()
    object InvalidInput : InputTypes()
}
class Input(val string: String, val type: InputTypes)

sealed class Preferences {
    class Date(val date: LocalDate) : Preferences()
    class Weekdays(val days: List<DayOfWeek>) : Preferences()
    object Never : Preferences()
    object EveryDay : Preferences()
}

//class Customer(val name: String, val preferences: Preferences)
//val cust1 = Customer("Joe", date)
//val cust2 = Customer("Hoe", weekday)
//val cust3 = Customer("Foe", never)
//val customers = listOf(cust1, cust2, cust3)


val dateInput = 2
val date: Preferences = Date(LocalDate.now().withDayOfMonth(dateInput))
val weekdays: Preferences = Weekdays(listOf(MONDAY))
val never: Preferences = Never

val alphabet = 'A'..'Z'
val preferences = listOf(date, weekdays, never)
val subscriptions = alphabet.zip(preferences)

class Customer(val tag: Char, val preferences: Preferences)

val customers = subscriptions.map {
    Customer(it.first, it.second)
}

class DailyReport(val date: LocalDate, val subscribers: List<Char>)
class Report(val dailyReports: List<DailyReport>)

fun findSubscribersForDate(
    date: LocalDate = LocalDate.now(),
    subscribers: List<Customer>
): List<Char> {

    val daysSubscribers: List<Char> = subscribers.mapNotNull { customer ->
        when (customer.preferences) {
            is Date -> if (date == customer.preferences.date) customer.tag else null
            is Weekdays -> if (date.dayOfWeek in customer.preferences.days) customer.tag else null
            is EveryDay -> customer.tag
            is Never -> null
        }
    }

    return daysSubscribers
}

fun programme() {
    //read input
    // build report
    // make report map <LocalDate, listOf<String>>
    // print report

}
