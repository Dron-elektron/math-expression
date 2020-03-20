package ru.dronelektron.mathexpression

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.dronelektron.mathexpression.calculator.Function

@DisplayName("Math expression tests")
class MathExpressionTest {
	@Test
	@DisplayName("b * b - 4 * a * c returns 121 when a = 6, b = -13 and c = 2")
	fun discriminantAsVariables() {
		testGood("b * b - 4 * a * c", 121.0) {
			setVariable("a", 6.0)
			setVariable("b", -13.0)
			setVariable("c", 2.0)
		}
	}

	@Test
	@DisplayName("discriminant(6, -13, 2) returns 121 when discriminant(a, b, c) = b * b - 4.0 * a * c")
	fun discriminantAsFunction() {
		testGood("discriminant(6, -13, 2)", 121.0) {
			addFunction("discriminant", DiscriminantFunction)
		}
	}

	@Test
	@DisplayName("sin(x) ^ 2 + cos(x) ^ 2 returns 1 when x = 0.24")
	fun sinSquaredPlusCosSquared() {
		testGood("sin(x) ^ 2 + cos(x) ^ 2", 1.0) {
			setVariable("x", 0.24)
		}
	}

	@Test
	@DisplayName("sin(x) / cos(x) - tan(x) returns 0 when x = PI / 4")
	fun sinDividedByCosMinusTan() {
		testGood("sin(x) / cos(x) - tan(x)", 0.0) {
			setVariable("x", "PI / 4")
		}
	}

	@Test
	@DisplayName("log2(2 * 4) - (log2(2) + log2(4)) returns 0")
	fun log2OfMultiplicationMinusSumOfLog2() {
		testGood("log2(2 * 4) - (log2(2) + log2(4))", 0.0)
	}

	@Test
	@DisplayName("2 / 0 returns +inf")
	fun twoDividedByZero() {
		testGood("2 / 0", Double.POSITIVE_INFINITY)
	}

	@Test
	@DisplayName("0 / 0 returns NaN")
	fun zeroDividedByZero() {
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
		private const val EPS = 1e-15
	}
}
