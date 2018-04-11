import java.time.DayOfWeek
import java.time.LocalDate


fun main(args: Array<String>) {
    println("Input: ")

    val inputs = readInputs()

    val validInputs = inputs
        .filterNotNull()
        .map { Input(it, getInputType(it)) }
        .filter { it.type != InputTypes.InvalidInput }

    val preferences: List<Preferences> = validInputs.map {
        when (it.type) {
            is InputTypes.DateInput -> Preferences.Date(LocalDate.now().withDayOfMonth(it.string.toInt()))
            is InputTypes.WeekDayInput -> it.string.split(" ")
                .map { DayOfWeek.valueOf(it) }
                .let { Preferences.Weekdays(it) }
            is InputTypes.NeverInput -> Preferences.Never
            is InputTypes.DailyInput -> Preferences.EveryDay
            else -> Preferences.Never
        }
    }

    val numberInput = "29"
    val datePattern = "^(2[08]|[12][0-9]|[1-9])\$".toRegex()
    val hasDatePattern = datePattern.containsMatchIn(numberInput)
    val number = numberInput.toInt()
    val datePreference = Preferences.Date(LocalDate.now().withDayOfMonth(number))

    val weekDayInput = "WEDNESDAY THURSDAY"
    val weekDayPattern = "MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY".toRegex()
    val hasWeekDayPattern = weekDayPattern.containsMatchIn(weekDayInput)
    val weekDayStrings = weekDayInput.split(" ")
    val listDayOfWeek = weekDayStrings.map {
        DayOfWeek.valueOf(it)
    }
    val weekDaysPreference = Preferences.Weekdays(listDayOfWeek)

    val neverInput = "Never"
    val neverPattern = "Never".toRegex()
    val hasNeverPattern = neverPattern.containsMatchIn(neverInput)

    val dailyInput = "Daily"
    val dailyPattern = "Daily".toRegex()
    val hasDailyPattern = dailyPattern.containsMatchIn(dailyInput)

    val allPreferences = listOf(datePreference, weekDaysPreference, Preferences.Never)

    val start = LocalDate.now()
    val secondDay = start.plusDays(1)
    val end = start.plusDays(90)
    val weekDay = start.dayOfWeek
}

fun readInputs(result: List<String?> = emptyList()): List<String?> =
    readLine().let { input ->
        if (input == "") result
        else readInputs(result.plus(input))
    }


//fun classifyInputs(
//    rawInputs: List<String?>,
//    datePattern: Regex = "[0-9]".toRegex(),
//    weekDayPattern: Regex = "MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY".toRegex(),
//    neverPattern: Regex = "Never".toRegex(),
//    dailyPattern: Regex = "Daily".toRegex()
//): List<String> =
//    rawInputs
//        .filterNotNull()
//        .map {
//            when (i
//        }

fun getInputType(
    input: String,
    datePattern: Regex = "[0-9]".toRegex(),
    weekDayPattern: Regex = "MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY".toRegex(),
    neverPattern: Regex = "Never".toRegex(),
    dailyPattern: Regex = "Daily".toRegex()
): InputTypes = when {

    input.contains(datePattern) -> InputTypes.DateInput
    input.contains(weekDayPattern) -> InputTypes.WeekDayInput
    input == "Never" -> InputTypes.NeverInput
    input == "Daily" -> InputTypes.DailyInput
    input == "" -> InputTypes.SubmitInput
    else -> InputTypes.InvalidInput
}

fun validInputs(
    rawInputs: List<String?>,
    datePattern: Regex = "[0-9]".toRegex(),
    weekDayPattern: Regex = "MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY".toRegex(),
    neverPattern: Regex = "Never".toRegex(),
    dailyPattern: Regex = "Daily".toRegex()
): List<String> =
    rawInputs
        .filterNotNull()
        .filter {
            it.contains(datePattern) || it.contains(weekDayPattern) || it.contains(neverPattern) || it.contains(
                dailyPattern
            )
        }


val datePattern = "^(2[08]|[12][0-9]|[1-9])\$".toRegex()
val weekDayPattern = "MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY".toRegex()
val neverPattern = "Never".toRegex()
val dailyPattern = "Daily".toRegex()


//fun preference(input: List<String>): List<Preferences> =


//fun verifyInput(input: String): Input =
//    when (getInputType(input)) {
//        InputTypes.DateInput, InputTypes.WeekDayInput, InputTypes.NeverInput, InputTypes.DailyInput -> Input.Valid
//        InputTypes.SubmitInput -> Input.Submit
//        InputTypes.InvalidInput -> Input.Invalid
//    }





