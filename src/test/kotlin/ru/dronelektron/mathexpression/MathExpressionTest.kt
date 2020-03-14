package ru.dronelektron.mathexpression

import org.junit.Assert.*
import org.junit.Test
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
