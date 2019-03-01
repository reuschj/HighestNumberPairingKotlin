package tests

import kotlin.test.assertTrue
import kotlin.test.assertEquals
import org.junit.Test

import kotlin.math.abs

import definitions.NumberPairing
import kotlin.test.assertEquals

class NumberPairingUnitTests {

    @Test fun testThatNumberPairingEqualityWorks() {
        val twoAddsUpToEight = NumberPairing(2.0, 8.0)
        val twoAlsoAddsUpToEight = NumberPairing(2.0, 8.0)
        val sixAddsUpToEight = NumberPairing(6.0, 8.0)
        val threeAddsUpToEight = NumberPairing(3.0, 8.0)
        val threeAddsUpToTen = NumberPairing(3.0, 10.0)
        assertTrue(twoAddsUpToEight.isEqualTo(twoAlsoAddsUpToEight), "twoAddsUpToEight should equal twoAddsUpToEight")
        assertTrue(twoAddsUpToEight.isEqualTo(sixAddsUpToEight), "twoAddsUpToEight should equal twoAddsUpToEight")
        assertTrue(threeAddsUpToEight != sixAddsUpToEight, "threeAddsUpToEight should not equal sixAddsUpToEight")
        assertTrue(threeAddsUpToEight != threeAddsUpToTen, "threeAddsUpToEight should not equal threeAddsUpToTen")
    }

    @Test fun testThatComparisonWorks() {
        val twoAddsUpToEight = NumberPairing(2.0, 8.0)
        val sixAddsUpToEight = NumberPairing(6.0, 8.0)
        val threeAddsUpToEight = NumberPairing(3.0, 8.0)
        assertTrue(twoAddsUpToEight >= sixAddsUpToEight, "twoAddsUpToEight should be greater than or equal to (it's equal) sixAddsUpToEight")
        assertTrue(threeAddsUpToEight < sixAddsUpToEight, "threeAddsUpToEight should be less than sixAddsUpToEight")
        assertTrue(sixAddsUpToEight > threeAddsUpToEight, "sixAddsUpToEight should be greater than threeAddsUpToEight")
    }

    @Test fun testCreation() {
        val testNumberPairing = NumberPairing(2.0, 8.0)
        assertEquals(2.0, testNumberPairing.firstNumber, "The first number should be 2.0")
        assertEquals(6.0, testNumberPairing.secondNumber, "The second number should be 6.0")
        assertEquals(8.0, testNumberPairing.sumOfNumbers, "The sum should be 8.0")
    }

}