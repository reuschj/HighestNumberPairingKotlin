package utilities

import kotlin.math.round

// Drops the trailing zero doubles when outputing to string
// For example 16.0 -> "16" or 12.20 to "12.2"
fun roundNumberToString(number: Double, precision: Double = 1_000.00): String {
    val reduced = round((number * precision)) / precision
    return if (reduced == round(reduced)) {
        "%1.0f".format(reduced)
    } else {
        reduced.toString()
    }
}