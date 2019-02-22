package definitions

import kotlin.math.abs
import utilities.roundNumberToString
import utilities.makeDoubleRange
import constants.defaultSum
import constants.lineMedium

/**
 * A class to define a problem by which takes two numbers that sum to a given amount (default to 8).
 * The problem must find the largest number combination (determined by multiplying the difference by the product of the two numbers)
 */
open class NumberPairingProblem(val sumOfNumberPairing: Double = defaultSum, collectOtherResults: Boolean = true) {
    var runsToSolve: Int = 0
    val introString: String
        get() {
            return "Problem:\nFind two numbers that add up to ${roundNumberToString(sumOfNumberPairing)}, such that the product multiplied by the difference produces the largest possible value.\n"
        }
    // Private property to store results
    private var results = object {
        var bestResult: Double? = null
        var bestNumberPairings: MutableSet<NumberPairing>? = null
        var otherNumberPairings: Array<NumberPairing>? = null
    }
    // Accesses best result
    val bestResult: Double
        get() {
            return results.bestResult ?: 0.0
        }
    // Accesses best result and output as a formatted string report
    val bestResultReport: String
        get() {
            return "Best Result:\n$bestResult\n$lineMedium\n"
        }
    // Accesses an array of winning number pairings
    val bestNumberPairings: MutableSet<NumberPairing>
        get() {
            return results.bestNumberPairings ?: mutableSetOf<NumberPairing>()
        }
    // Accesses an array of winning number pairings and outputs as formatted string report
    val bestNumberPairingsReport: String
        get() {
            var output = "Best Number Combination:"
            for (winner in bestNumberPairings) {
                output += "\n${winner.shortReport()}"
            }
            output += "\n$lineMedium\n"
            return output
        }
    // Accesses an array of other number pairings
    val otherNumberPairings: Array<NumberPairing>?
        get() {
            return results.otherNumberPairings
        }
    // Accesses an array of other number pairings and outputs as formatted string report
    var otherNumberPairingsReport: String? = null
        get() {
            val allOtherResults = otherNumberPairings ?: emptyArray()
            var output = "Other Top Results:"
            val maxResults = if (allOtherResults.count() > 10) 10 else allOtherResults.count()
            for (index in 0 until maxResults) {
                output += "\n${allOtherResults[index].shortReport()}"
            }
            if (maxResults < allOtherResults.count()) {
                output += "\n\u2026\n"
            }
            return output
        }

    init {
        getResults(collectOtherResults)
    }

    // Secondary convenience constructor
    constructor(): this(8.0, true)


    // Methods --------------------------------------------------------------- /

    /**
     * This method is called during initialization to get the results of the problem
     * Returns a tuple with the best result, an array of best result pairings and an array of other top pairings (sorted)
     * These values will be accessed by public getter properties
     */
    private fun getResults(collectOtherResults: Boolean = true) {

        // This is a NumberPairing instance that will always have a result of 0
        // We will use this as the initial high NumberPairing to beat
        val initialHighValue = NumberPairing(0.0, sumOfNumberPairing)

        // These constants for lower and upper bounds set the boundaries for numbers in the number pairing
        // We will use these to ensure we don't get a NumberPairing with a number outside of these bounds
        val lowerBounds = 0.0
        val upperBounds: Double = sumOfNumberPairing / 2

        // These variable will hold the current overall best result that the recursive function will compare to and set as needed
        // At the end, these values will be returned in a tuple
        var overallBestResult: NumberPairing = initialHighValue
        var bestResults: MutableSet<NumberPairing> = mutableSetOf()
        var otherResults: MutableSet<NumberPairing>? = if (collectOtherResults) mutableSetOf() else null

        // This is a fail-safe. Hopefully, we end recursion before we get here, but just in case, it sets a limit on recursion
        var runCount = 0
        var maxRuns = 40

        // This is a recursive function that will start with low precision, look for the max value,
        // then continue looking for higher max values (at a higher precision) around that max value.
        // When further recursion no longer finds a better value, recursion ends (as the max value has been found)
        fun getHighestResultOfSequence(lowValue: Double, highValue: Double, precision: Double) {

            // If we hit the max run count, we will return and stop recursion
            if (runCount >= maxRuns) { return }
            runCount += 1

            // We will set three local variables that will be for each recursive run... these will be compared to the overall variables for the method
            var bestResultFromSequence: NumberPairing = initialHighValue
            var bestResultsFromSequence: MutableSet<NumberPairing> = mutableSetOf()
            var otherResultsFromSequence: MutableSet<NumberPairing>? = if (collectOtherResults) mutableSetOf() else null

            // Closure to determine if we can add to the other sequence
            val canBeAddedToOther: (pairing: NumberPairing) -> Boolean = {
                it != initialHighValue && precision >= 0.01 && collectOtherResults
            }

            // Set the search range and loop through each value in it
            val searchRange = makeDoubleRange(lowValue, highValue, precision)
            for (number in searchRange) {

                // Create a new NumberPairing to evaluate
                val thisResult = NumberPairing(number, sumOfNumberPairing)

                if (thisResult > bestResultFromSequence) {
                    // If the new Result is better than any other in the sequence, it's the new max
                    // We'll set it to the best in sequence and move and previous best results to the other results array
                    // Then add the new result to the best results array
                    bestResultFromSequence = thisResult
                    for (result in bestResultsFromSequence) {
                        if (canBeAddedToOther(result)) {
                            otherResultsFromSequence?.add(result)
                        }
                    }
                    bestResultsFromSequence.clear()
                    bestResultsFromSequence.add(thisResult)

                } else if (thisResult == bestResultFromSequence) {

                    // If we found a NumberPairing that matches, but doesn't exceed, the existing best, we'll add it to the best results array
                    bestResultsFromSequence.add(thisResult)

                } else if (canBeAddedToOther(thisResult)) {

                    // Else, we'll just add it to the other results array
                    otherResultsFromSequence?.add(thisResult)

                }
            }

            // When the best result from the sequence is lower or equal to the overall result (or close enough), we found the max and can stop
            val conditionToEndRecursion = bestResultFromSequence <= overallBestResult || bestResultFromSequence.isEquivalentTo(overallBestResult)
            if (conditionToEndRecursion) {
                runsToSolve = runCount
                return
            }

            // In this case, the sequence produced a higher result than the previous, so we'll set it to the new overall best
            // We'll also move the previous best results from the best results array to the other results array
            // and add the new best results to the best results array
            overallBestResult = bestResultFromSequence
            for (result in bestResults) {
                if (canBeAddedToOther(result)) {
                    otherResults?.add(result)
                }
            }
            bestResults.clear()
            bestResults = bestResults.union(bestResultsFromSequence).toMutableSet()
            if (otherResultsFromSequence != null) {
                otherResults = otherResults?.union(otherResultsFromSequence)?.toMutableSet()
            }

            // This finds what the first number was from the best result. This the number we'll target when call the function again
            val bestNumberFromSequence: Double = bestResultFromSequence.firstNumber
            // We will run the function again with more precision...
            val newPrecision: Double = precision / (runCount * 4).toDouble()
            // We'll look to half the current precision on either side of the best value
            val marginToSearchAroundBestValue: Double = precision / 2
            // ... but we'll look in a smaller range. The new result will be the best number from the sequence minus the shrink amount
            var newLowValue = bestNumberFromSequence - marginToSearchAroundBestValue
            if (newLowValue < lowerBounds) {
                // If new start is lower than lower bounds, snap it to lower bounds
                newLowValue = lowerBounds
            }
            // ... and new end is the best number in the sequence plus the shrink amount
            var newHighValue = bestNumberFromSequence + marginToSearchAroundBestValue
            if (newHighValue > upperBounds) {
                // If new end is higher than upper bounds, snap it to upper bounds
                newHighValue = upperBounds
            }

            // Call recursive function again with narrower range as defined above (but higher precision)
            getHighestResultOfSequence(newLowValue, newHighValue, newPrecision)

        }

        // Call the recursive function defined above
        getHighestResultOfSequence(lowerBounds, upperBounds, sumOfNumberPairing / 4)

        // Sort the other results
        var othersSorted: Array<NumberPairing>? = null
        if (otherResults != null) {
            othersSorted = otherResults?.toTypedArray()
            othersSorted?.sortByDescending{it.result}
        }

        // Assign values to results members
        results.bestResult = overallBestResult.result
        results.bestNumberPairings = bestResults
        results.otherNumberPairings = othersSorted
    }

    // Prints all results
    fun printAllResults() {
        println("\n")
        println(introString)
        println(bestResultReport)
        println(bestNumberPairingsReport)
        if (otherNumberPairingsReport != null) {
            print(otherNumberPairingsReport)
        }
    }

    // Gets user input from command line or input (or uses default)
    companion object {
        fun getUserInput(args: Array<String>): Double {

            // Pulls argument from commandline if available or uses default
            val commandLineInput: String? = if (args.count() > 0) args[0] else null

            var userInput: Double
            userInput = if (commandLineInput != null) {
                // If an argument was entered, assign it as user input
                try {
                    commandLineInput.toDouble()
                } catch (exception: NumberFormatException) {
                    defaultSum
                }
            } else {
                // Else prompt the user for input
                println("Enter a positive number and press return (Default: $defaultSum):")
                val getInput: String? = readLine()

                if (getInput != null) {
                    // If input exits attempt to convert to a double
                    try {
                        getInput.toDouble()
                    } catch (exception: NumberFormatException) {
                        defaultSum
                    }
                } else {
                    defaultSum
                }
            }
            // Return user input (if it exists) or use default
            return abs(userInput)
        }

        // Looks for an optional second command line argument
        // If "no", returns false, else returns true
        fun lookForSecondCommand(args: Array<String>): Boolean {

            // Look for second command
            var secondCommand: String? = if (args.count() > 1) args[1] else null

            return if (secondCommand != null) {
                val lowercaseCommand = secondCommand.toLowerCase();
                !(lowercaseCommand == "no" || lowercaseCommand == "false")
            } else {
                // If no result, return true
                true
            }
        }
    }
}
