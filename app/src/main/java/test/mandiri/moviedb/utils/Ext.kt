package test.mandiri.moviedb.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String?.tryParseAndFormatDate(): String {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date = LocalDate.parse(this.toString())
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
            date.format(formatter)
        }

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = formatter.parse(this.toString()) ?: throw Exception("error parsing date")
        SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        "-"
    }
}