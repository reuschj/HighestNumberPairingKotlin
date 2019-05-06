package constants

// The default version of this problem is two numbers that add to 8
const val defaultSum = 8.0

// The minimum level of precision we care about... beyond this point, we'll consider values equal
const val minimumPrecision = 0.0000000001

// Lines of various length
private const val lineChar = "-"
private const val lineBaseLength = 9
val lineShort = lineChar.repeat(lineBaseLength * 1)
val lineMedium = lineChar.repeat(lineBaseLength * 2)
val lineLong = lineChar.repeat(lineBaseLength * 3)