package ru.dronelektron.mathexpression.calculator

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import ru.dronelektron.mathexpression.calculator.functions.*
import ru.dronelektron.mathexpression.lexer.Lexer
import ru.dronelektron.mathexpression.lexer.Token
import ru.dronelektron.mathexpression.lexer.TokenType.IDENTIFIER
import ru.dronelektron.mathexpression.parser.Parser
import kotlin.math.E
import kotlin.math.PI
import kotlin.math.sqrt

class CalculatorTest {
	@Test
	fun calculate_whenTwo_returnsTwo() {
		testGood("2", 2.0)
	}

	@Test
	fun calculate_whenMinusTwo_returnsMinusTwo() {
		testGood("-2", -2.0)
	}

	@Test
	fun calculate_whenMinusMinusTwo_returnsTwo() {
		testGood("--2", 2.0)
	}

	@Test
	fun calculate_whenTwoPlusMinusThree_returnsMinusOne() {
		testGood("2 + -3", -1.0)
	}

	@Test
	fun calculate_whenTwoMinusMinusThree_returnsFive() {
		testGood("2 - -3", 5.0)
	}

	@Test
	fun calculate_whenTwoMinusMinusMinusThree_returnsMinusOne() {
		testGood("2 - --3", -1.0)
	}

	@Test
	fun calculate_whenTwoPlusThree_returnsFive() {
		testGood("2 + 3", 5.0)
	}

	@Test
	fun calculate_whenTwoMinusThree_returnsMinusOne() {
		testGood("2 - 3", -1.0)
	}

	@Test
	fun calculate_whenTwoMultiplyByThree_returnsSix() {
		testGood("2 * 3", 6.0)
	}

	@Test
	fun calculate_whenTwoDividedByThree_returnsZeroPointSixInPeriod() {
		testGood("2 / 3", 0.666666666666)
	}

	@Test
	fun calculate_whenTwoRaisedToThree_returnsEight() {
		testGood("2 ^ 3", 8.0)
	}

	@Test
	fun calculate_whenTwoPlusTwoMinusThree_returnsOne() {
		testGood("2 + 2 - 3", 1.0)
	}

	@Test
	fun calculate_whenTwoMultiplyByTwoDividedByThree_returnsOnePointThreeInPeriod() {
		testGood("2 * 2 / 3", 1.333333333333)
	}

	@Test
	fun calculate_whenTwoRaisedToTwoRaisedToThree_returnsTwoHundredFiftySix() {
		testGood("2 ^ 2 ^ 3", 256.0)
	}

	@Test
	fun calculate_whenSetXToOne_returnsOne() {
		testGood("x", 2.0) {
			setVariable("x", 2.0)
		}
	}

	@Test
	fun calculate_whenSinOfZero_returnsZero() {
		testGood("sin(0)", 0.0) {
			addFunction("sin", SinFunction)
		}
	}

	@Test
	fun calculate_whenSinOfPiDividedByTwo_returnsOne() {
		testGood("sin(PI / 2)", 1.0) {
			setVariable("PI", PI)
			addFunction("sin", SinFunction)
		}
	}

	@Test
	fun calculate_whenCosOfZero_returnsOne() {
		testGood("cos(0)", 1.0) {
			addFunction("cos", CosFunction)
		}
	}

	@Test
	fun calculate_whenCosOfPiDividedByTwo_returnsZero() {
		testGood("cos(PI / 2)", 0.0) {
			setVariable("PI", PI)
			addFunction("cos", CosFunction)
		}
	}

	@Test
	fun calculate_whenTanOfZero_returnsZero() {
		testGood("tan(0)", 0.0) {
			addFunction("tan", TanFunction)
		}
	}

	@Test
	fun calculate_whenTanOfPiDividedByFour_returnsOne() {
		testGood("tan(PI / 4)", 1.0) {
			setVariable("PI", PI)
			addFunction("tan", TanFunction)
		}
	}

	@Test
	fun calculate_whenCotOfPiDividedByTwo_returnsZero() {
		testGood("cot(PI / 2)", 0.0) {
			setVariable("PI", PI)
			addFunction("cot", CotFunction)
		}
	}

	@Test
	fun calculate_whenCotOfPiDividedBySix_returnsSqrtOfThree() {
		testGood("cot(PI / 6)", sqrt(3.0)) {
			setVariable("PI", PI)
			addFunction("cot", CotFunction)
		}
	}

	@Test
	fun calculate_whenSqrtOfSixteen_returnsFour() {
		testGood("sqrt(16)", 4.0) {
			addFunction("sqrt", SqrtFunction)
		}
	}

	@Test
	fun calculate_whenLnOfE_returnsOne() {
		testGood("ln(E)", 1.0) {
			setVariable("E", E)
			addFunction("ln", LnFunction)
		}
	}

	@Test
	fun calculate_whenLgOfOneHundred_returnsTwo() {
		testGood("lg(100)", 2.0) {
			addFunction("lg", LgFunction)
		}
	}

	@Test
	fun calculate_whenLog2OfEight_returnsThree() {
		testGood("log2(8)", 3.0) {
			addFunction("log2", Log2Function)
		}
	}

	@Test
	fun calculate_whenAbsOfMinusTwo_returnsTwo() {
		testGood("abs(-2)", 2.0) {
			addFunction("abs", AbsFunction)
		}
	}

	@Test
	fun calculate_whenSecOfZero_returnsOne() {
		testGood("sec(0)", 1.0) {
			addFunction("sec", SecFunction)
		}
	}

	@Test
	fun calculate_whenSecOfPiDividedByThree_returnsTwo() {
		testGood("sec(PI / 3)", 2.0) {
			setVariable("PI", PI)
			addFunction("sec", SecFunction)
		}
	}

	@Test
	fun calculate_whenCscOfPiDividedBySix_returnsTwo() {
		testGood("csc(PI / 6)", 2.0) {
			setVariable("PI", PI)
			addFunction("csc", CscFunction)
		}
	}

	@Test
	fun calculate_whenSecOfPiDividedByTwo_returnsOne() {
		testGood("csc(PI / 2)", 1.0) {
			setVariable("PI", PI)
			addFunction("csc", CscFunction)
		}
	}

	@Test
	fun calculate_whenUndefinedVariable_throwsException() {
		testBad("x + y", Calculator.ERROR_UNDEFINED_VARIABLE, Token(IDENTIFIER, "y", 4)) {
			setVariable("x", 2.4)
		}
	}

	@Test
	fun calculate_whenUndefinedFunction_throwsException() {
		testBad("0.2 * kappa(4)", Calculator.ERROR_UNDEFINED_FUNCTION, Token(IDENTIFIER, "kappa", 6))
	}

	@Test
	fun calculate_whenSinWithoutArguments_throwsException() {
		testBad("sin()", Calculator.ERROR_INVALID_ARGUMENTS_COUNT, Token(IDENTIFIER, "sin", 0)) {
			addFunction("sin", SinFunction)
		}
	}

	@Test
	fun calculate_whenSinOfMultipleArguments_throwsException() {
		testBad("sin(1, 2)", Calculator.ERROR_INVALID_ARGUMENTS_COUNT, Token(IDENTIFIER, "sin", 0)) {
			addFunction("sin", SinFunction)
		}
	}

	private fun testGood(
		expressionText: String,
		expectedResult: Double,
		callback: Calculator.() -> Unit = {}
	) {
		val calculator = createCalculator(expressionText, callback)

		assertEquals(expectedResult, calculator.calculate(), EPS)
	}

	private fun testBad(
		expressionText: String,
		expectedMessage: String,
		expectedToken: Token,
		callback: Calculator.() -> Unit = {}
	) {
		val calculator = createCalculator(expressionText, callback)

		try {
			calculator.calculate()
			fail()
		} catch (ex: CalculatorException) {
			assertEquals(expectedMessage, ex.message)
			assertEquals(expectedToken, ex.token)
		}
	}

	private fun createCalculator(
		expressionText: String,
		callback: Calculator.() -> Unit
	): Calculator {
		val lexer = Lexer(expressionText)
		val parser = Parser(lexer.tokens)

		return Calculator(parser.root).also { callback(it) }
	}

	companion object {
		private const val EPS = 1e-12
	}
}
