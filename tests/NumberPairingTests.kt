package tests

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test

import kotlin.math.abs

import definitions.NumberPairingProblem

val largeProblemSize: Double = 900.0
val massiveProblemSize: Double = 9_000_000.0

class NumberPairingTests {

    private fun printWithOtherResults(size: Double = 8.0): NumberPairingProblem {
        val testProblem = NumberPairingProblem(size)
        testProblem.printAllResults()
        return testProblem
    }

    private fun printWithoutOtherResults(size: Double = 8.0): NumberPairingProblem {
        val testProblem = NumberPairingProblem(size, false)
        testProblem.printAllResults()
        return testProblem
    }

    @Test fun testResultForEightIsCorrect() {
        val testProblem = NumberPairingProblem(8.0, true)
        val bestResult = testProblem.bestResult
        val expectedResult = 49.26722297
        val marginOfError = 0.00000001
        val difference = abs(bestResult - expectedResult)
        val testAssumption = difference < marginOfError
        assertTrue(testAssumption, "Test failed because difference was expected to be below $marginOfError and was $difference")
    }

    @Test fun testThatRunCountIsUnderForty() {
        val maxRunCount = 40
        val problem = printWithOtherResults(massiveProblemSize)
        val runs = problem.runsToSolve
        val testAssumption = runs < maxRunCount
        assertTrue(testAssumption, "Run count was $runs, which is not less than the maximum runs allowed.")
    }

}