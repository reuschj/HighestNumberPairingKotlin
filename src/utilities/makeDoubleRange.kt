package utilities

/**
 * Utility to make a range of doubles
 */
fun makeDoubleRange(start: Double, end: Double, step: Double = 1.0, includeEnd: Boolean = true): List<Double> {
    var continueAdding = { currentValue: Double -> if (includeEnd) currentValue <= end else currentValue < end }
    var values = mutableListOf<Double>()
    var valueToAdd = start
    while (continueAdding(valueToAdd)) {
        values.add(valueToAdd)
        valueToAdd += step
    }
    return values.toList()
}