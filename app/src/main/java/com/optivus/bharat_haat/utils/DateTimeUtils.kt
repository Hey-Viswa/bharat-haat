package com.optivus.bharat_haat.utils

import com.optivus.bharat_haat.constants.AppConstants
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateTimeUtils {

    private val displayDateFormat = SimpleDateFormat(AppConstants.DATE_FORMAT_DISPLAY, Locale.getDefault())
    private val apiDateFormat = SimpleDateFormat(AppConstants.DATE_FORMAT_API, Locale.getDefault())
    private val time12Format = SimpleDateFormat(AppConstants.TIME_FORMAT_12, Locale.getDefault())
    private val time24Format = SimpleDateFormat(AppConstants.TIME_FORMAT_24, Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat(AppConstants.DATETIME_FORMAT, Locale.getDefault())
    private val isoDateFormat = SimpleDateFormat(AppConstants.ISO_DATE_FORMAT, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    /**
     * Current date/time
     */
    fun getCurrentTimestamp(): Long = System.currentTimeMillis()

    fun getCurrentDate(): Date = Date()

    fun getCurrentDateString(): String = displayDateFormat.format(getCurrentDate())

    fun getCurrentTimeString(): String = time12Format.format(getCurrentDate())

    fun getCurrentDateTimeString(): String = dateTimeFormat.format(getCurrentDate())

    /**
     * Date formatting
     */
    fun formatDate(date: Date): String = displayDateFormat.format(date)

    fun formatDate(timestamp: Long): String = displayDateFormat.format(Date(timestamp))

    fun formatTime(date: Date): String = time12Format.format(date)

    fun formatTime(timestamp: Long): String = time12Format.format(Date(timestamp))

    fun formatDateTime(date: Date): String = dateTimeFormat.format(date)

    fun formatDateTime(timestamp: Long): String = dateTimeFormat.format(Date(timestamp))

    fun formatForAPI(date: Date): String = apiDateFormat.format(date)

    fun formatToISO(date: Date): String = isoDateFormat.format(date)

    /**
     * Date parsing
     */
    fun parseDate(dateString: String): Date? {
        return try {
            displayDateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    fun parseAPIDate(dateString: String): Date? {
        return try {
            apiDateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    fun parseISODate(dateString: String): Date? {
        return try {
            isoDateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Date calculations
     */
    fun addDays(date: Date, days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, days)
        return calendar.time
    }

    fun addHours(date: Date, hours: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR_OF_DAY, hours)
        return calendar.time
    }

    fun addMinutes(date: Date, minutes: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, minutes)
        return calendar.time
    }

    fun subtractDays(date: Date, days: Int): Date = addDays(date, -days)

    fun getDaysBetween(startDate: Date, endDate: Date): Long {
        val diffInMillis = endDate.time - startDate.time
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
    }

    fun getHoursBetween(startDate: Date, endDate: Date): Long {
        val diffInMillis = endDate.time - startDate.time
        return TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS)
    }

    fun getMinutesBetween(startDate: Date, endDate: Date): Long {
        val diffInMillis = endDate.time - startDate.time
        return TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS)
    }

    /**
     * Date comparisons
     */
    fun isToday(date: Date): Boolean {
        val today = Calendar.getInstance()
        val checkDate = Calendar.getInstance()
        checkDate.time = date

        return today.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == checkDate.get(Calendar.DAY_OF_YEAR)
    }

    fun isYesterday(date: Date): Boolean {
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_MONTH, -1)
        val checkDate = Calendar.getInstance()
        checkDate.time = date

        return yesterday.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
                yesterday.get(Calendar.DAY_OF_YEAR) == checkDate.get(Calendar.DAY_OF_YEAR)
    }

    fun isTomorrow(date: Date): Boolean {
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DAY_OF_MONTH, 1)
        val checkDate = Calendar.getInstance()
        checkDate.time = date

        return tomorrow.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
                tomorrow.get(Calendar.DAY_OF_YEAR) == checkDate.get(Calendar.DAY_OF_YEAR)
    }

    fun isThisWeek(date: Date): Boolean {
        val now = Calendar.getInstance()
        val checkDate = Calendar.getInstance()
        checkDate.time = date

        return now.get(Calendar.WEEK_OF_YEAR) == checkDate.get(Calendar.WEEK_OF_YEAR) &&
                now.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR)
    }

    fun isThisMonth(date: Date): Boolean {
        val now = Calendar.getInstance()
        val checkDate = Calendar.getInstance()
        checkDate.time = date

        return now.get(Calendar.MONTH) == checkDate.get(Calendar.MONTH) &&
                now.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR)
    }

    /**
     * Relative time
     */
    fun getRelativeTimeString(date: Date): String {
        val now = getCurrentDate()
        val diffInMillis = now.time - date.time

        return when {
            diffInMillis < TimeUnit.MINUTES.toMillis(1) -> "Just now"
            diffInMillis < TimeUnit.HOURS.toMillis(1) -> {
                val minutes = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS)
                "${minutes}m ago"
            }
            diffInMillis < TimeUnit.DAYS.toMillis(1) -> {
                val hours = TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS)
                "${hours}h ago"
            }
            diffInMillis < TimeUnit.DAYS.toMillis(7) -> {
                val days = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
                "${days}d ago"
            }
            diffInMillis < TimeUnit.DAYS.toMillis(30) -> {
                val weeks = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) / 7
                "${weeks}w ago"
            }
            diffInMillis < TimeUnit.DAYS.toMillis(365) -> {
                val months = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) / 30
                "${months}mo ago"
            }
            else -> {
                val years = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) / 365
                "${years}y ago"
            }
        }
    }

    fun getTimeUntilString(date: Date): String {
        val now = getCurrentDate()
        val diffInMillis = date.time - now.time

        if (diffInMillis <= 0) return "Expired"

        return when {
            diffInMillis < TimeUnit.MINUTES.toMillis(1) -> "Less than 1 minute"
            diffInMillis < TimeUnit.HOURS.toMillis(1) -> {
                val minutes = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS)
                "$minutes minutes"
            }
            diffInMillis < TimeUnit.DAYS.toMillis(1) -> {
                val hours = TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS)
                "$hours hours"
            }
            diffInMillis < TimeUnit.DAYS.toMillis(7) -> {
                val days = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
                "$days days"
            }
            else -> formatDate(date)
        }
    }

    /**
     * Business days calculations
     */
    fun addBusinessDays(date: Date, businessDays: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date

        var addedDays = 0
        while (addedDays < businessDays) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                addedDays++
            }
        }

        return calendar.time
    }

    fun getBusinessDaysBetween(startDate: Date, endDate: Date): Int {
        val start = Calendar.getInstance()
        start.time = startDate
        val end = Calendar.getInstance()
        end.time = endDate

        var businessDays = 0
        while (start.before(end)) {
            val dayOfWeek = start.get(Calendar.DAY_OF_WEEK)
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                businessDays++
            }
            start.add(Calendar.DAY_OF_MONTH, 1)
        }

        return businessDays
    }

    /**
     * Delivery date calculations
     */
    fun getEstimatedDeliveryDate(orderDate: Date, deliveryDays: Int = 3): Date {
        return addBusinessDays(orderDate, deliveryDays)
    }

    fun getDeliveryDateRange(orderDate: Date, minDays: Int = 3, maxDays: Int = 5): Pair<Date, Date> {
        val minDate = addBusinessDays(orderDate, minDays)
        val maxDate = addBusinessDays(orderDate, maxDays)
        return Pair(minDate, maxDate)
    }

    /**
     * Age calculations
     */
    fun calculateAge(birthDate: Date): Int {
        val birth = Calendar.getInstance()
        birth.time = birthDate
        val now = Calendar.getInstance()

        var age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR)

        if (now.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }

    /**
     * Time slots for delivery
     */
    fun getDeliveryTimeSlots(): List<TimeSlot> {
        return listOf(
            TimeSlot("09:00", "12:00", "Morning"),
            TimeSlot("12:00", "15:00", "Afternoon"),
            TimeSlot("15:00", "18:00", "Evening"),
            TimeSlot("18:00", "21:00", "Night")
        )
    }

    fun isValidDeliveryTime(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        // Delivery available between 9 AM to 9 PM
        return hour in 9..21
    }
}

data class TimeSlot(
    val startTime: String,
    val endTime: String,
    val label: String
)
