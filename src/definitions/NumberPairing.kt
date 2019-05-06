package definitions

import kotlin.math.abs
import constants.minimumPrecision
import constants.lineMedium
import utilities.roundNumberToString

/**
 * Stores two numbers that sum to a given amount.
 * Finds the product, the difference and the result of multiplying the difference and the product.
 */
data class NumberPairing(private var oneNumber: Double, val sumOfNumbers: Double = 8.0): Comparable<NumberPairing> {

    private val anchor: Double
        get() = validateAndCorrectNumberInput(oneNumber)

    var firstNumber: Double
        get() = anchor
        set(newValue) {
            oneNumber = validateAndCorrectNumberInput(newValue)
        }

    var secondNumber: Double
        get() = sumOfNumbers - anchor
        set(newValue) {
            oneNumber = sumOfNumbers - validateAndCorrectNumberInput(newValue)
        }

    val product: Double
        get() = anchor * secondNumber

    val difference: Double
        get() = abs(anchor - secondNumber)

    val result: Double
        get() = product * difference

    val hashCode: Double
        get() = anchor + sumOfNumbers + result

    // Secondary constructor with default sum
    constructor(oneNumber: Double) : this(oneNumber, sumOfNumbers = 8.0)

    // Methods --------------------------------------------------------------- /

    // Finds the differnce between two NumberPairings
    fun difference(anotherNumberPairing: NumberPairing): Double =
        abs(result - anotherNumberPairing.result)

    // This will test if two results are close enough to be considered equal to each other
    // The two NumberPairings may still be !=
    fun isEquivalentTo(anotherNumberPairing: NumberPairing): Boolean =
        difference(anotherNumberPairing) < minimumPrecision

    // Creates a long report with both numbers, the product, difference and the result
    fun longReport(): String {
        val firstRounded = roundNumberToString(firstNumber, 100_000.00)
        val secondRounded = roundNumberToString(secondNumber, 100_000.00)
        val productRounded = roundNumberToString(product, 100_000.00)
        val differenceRounded = roundNumberToString(difference, 100_000.00)
        val resultRounded = roundNumberToString(result, 100_000.00)
        return """
        Numbers: $firstRounded and $secondRounded
        $lineMedium
        Product: $productRounded
        Difference: $differenceRounded
        $lineMedium
        Result: $resultRounded\n
        """
    }

    // Creates a short report with both numbers and the result
    fun shortReport(): String {
        val firstRounded = roundNumberToString(firstNumber, 100_000.00)
        val secondRounded = roundNumberToString(secondNumber, 100_000.00)
        val resultRounded = roundNumberToString(result, 100_000.00)
        return "$firstRounded and $secondRounded -> $resultRounded"
    }

    // Private Methods ------------------------------------------------------- /

    // This will set a bound to ensures that the number is positive and not more than the sum
    private fun validateAndCorrectNumberInput(userInput: Double): Double {
        val nonNegative = abs(userInput)
        return if (nonNegative > sumOfNumbers) sumOfNumbers else nonNegative
    }

    // Static Methods -------------------------------------------------------- /

    // Method for checking equality
    fun equals(other: NumberPairing?): Boolean {
        val sumsAreEqual = sumOfNumbers == other?.sumOfNumbers
        val anchorsAreEqual = anchor == other?.anchor
        val anchorIsEqualToInverse = anchor == other?.secondNumber
        return (sumsAreEqual && anchorsAreEqual) || (sumsAreEqual && anchorIsEqualToInverse)
    }

    // Method for comparison
    override operator fun compareTo(other: NumberPairing): Int {
        if (this.result > other.result) return 1
        if (this.result < other.result) return -1
        return 0
    }

}
