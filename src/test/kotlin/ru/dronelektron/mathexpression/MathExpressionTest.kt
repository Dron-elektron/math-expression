package ru.dronelektron.mathexpression

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.dronelektron.mathexpression.calculator.Function

class MathExpressionTest {
	@Test
	fun calculate_whenDiscriminantOfSixAndMinusThirteenAndTwo_returnsOneHundredTwentyOne() {
		testGood("b * b - 4 * a * c", 121.0) {
			setVariable("a", 6.0)
			setVariable("b", -13.0)
			setVariable("c", 2.0)
		}

		testGood("discriminant(6, -13, 2)", 121.0) {
			addFunction("discriminant", DiscriminantFunction)
		}
	}

	@Test
	fun calculate_whenSinSquaredPlusCosSquared_returnsOne() {
		testGood("sin(x) ^ 2 + cos(x) ^ 2", 1.0) {
			setVariable("x", 0.24)
		}
	}

	@Test
	fun calculate_whenSinDividedByCosMinusTan_returnsZero() {
		testGood("sin(x) / cos(x) - tan(x)", 0.0) {
			setVariable("x", "PI / 4")
		}
	}

	@Test
	fun calculate_whenLog2OfMultiplicationMinusSumOfLog2_returnsZero() {
		testGood("log2(2 * 4) - (log2(2) + log2(4))", 0.0)
	}

	@Test
	fun calculate_whenTwoDividedByZero_returnsInfinity() {
		testGood("2 / 0", Double.POSITIVE_INFINITY)
	}

	@Test
	fun calculate_whenZeroDividedByZero_returnsNan() {
		testGood("0 / 0", Double.NaN)
	}

	private fun testGood(
		expressionText: String,
		expectedResult: Double,
		callback: MathExpression.() -> Unit = {}
	) {
		val mathExpression = MathExpression(expressionText).apply(callback)

		assertEquals(expectedResult, mathExpression.calculate(), EPS)
	}

	private object DiscriminantFunction : Function {
		override val arity = 3

		override fun call(arguments: List<Double>): Double {
			val (a, b, c) = arguments

			return b * b - 4.0 * a * c
		}
	}

	companion object {
		private const val EPS = 1e-12
	}
}
