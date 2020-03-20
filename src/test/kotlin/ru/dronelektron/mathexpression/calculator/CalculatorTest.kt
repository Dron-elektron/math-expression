package ru.dronelektron.mathexpression.calculator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
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
		calculateGood("2", 2.0)
	}

	@Test
	fun calculate_whenMinusTwo_returnsMinusTwo() {
		calculateGood("-2", -2.0)
	}

	@Test
	fun calculate_whenMinusMinusTwo_returnsTwo() {
		calculateGood("--2", 2.0)
	}

	@Test
	fun calculate_whenTwoPlusMinusThree_returnsMinusOne() {
		calculateGood("2 + -3", -1.0)
	}

	@Test
	fun calculate_whenTwoMinusMinusThree_returnsFive() {
		calculateGood("2 - -3", 5.0)
	}

	@Test
	fun calculate_whenTwoMinusMinusMinusThree_returnsMinusOne() {
		calculateGood("2 - --3", -1.0)
	}

	@Test
	fun calculate_whenTwoPlusThree_returnsFive() {
		calculateGood("2 + 3", 5.0)
	}

	@Test
	fun calculate_whenTwoMinusThree_returnsMinusOne() {
		calculateGood("2 - 3", -1.0)
	}

	@Test
	fun calculate_whenTwoMultiplyByThree_returnsSix() {
		calculateGood("2 * 3", 6.0)
	}

	@Test
	fun calculate_whenTwoDividedByThree_returnsZeroPointSixInPeriod() {
		calculateGood("2 / 3", 0.666666666666)
	}

	@Test
	fun calculate_whenTwoRaisedToThree_returnsEight() {
		calculateGood("2 ^ 3", 8.0)
	}

	@Test
	fun calculate_whenTwoPlusTwoMinusThree_returnsOne() {
		calculateGood("2 + 2 - 3", 1.0)
	}

	@Test
	fun calculate_whenTwoMultiplyByTwoDividedByThree_returnsOnePointThreeInPeriod() {
		calculateGood("2 * 2 / 3", 1.333333333333)
	}

	@Test
	fun calculate_whenTwoRaisedToTwoRaisedToThree_returnsTwoHundredFiftySix() {
		calculateGood("2 ^ 2 ^ 3", 256.0)
	}

	@Test
	fun calculate_whenSetXToOne_returnsOne() {
		calculateGood("x", 2.0) {
			setVariable("x", 2.0)
		}
	}

	@Test
	fun calculate_whenSinOfZero_returnsZero() {
		calculateGood("sin(0)", 0.0) {
			addFunction("sin", SinFunction)
		}
	}

	@Test
	fun calculate_whenSinOfPiDividedByTwo_returnsOne() {
		calculateGood("sin(PI / 2)", 1.0) {
			setVariable("PI", PI)
			addFunction("sin", SinFunction)
		}
	}

	@Test
	fun calculate_whenCosOfZero_returnsOne() {
		calculateGood("cos(0)", 1.0) {
			addFunction("cos", CosFunction)
		}
	}

	@Test
	fun calculate_whenCosOfPiDividedByTwo_returnsZero() {
		calculateGood("cos(PI / 2)", 0.0) {
			setVariable("PI", PI)
			addFunction("cos", CosFunction)
		}
	}

	@Test
	fun calculate_whenTanOfZero_returnsZero() {
		calculateGood("tan(0)", 0.0) {
			addFunction("tan", TanFunction)
		}
	}

	@Test
	fun calculate_whenTanOfPiDividedByFour_returnsOne() {
		calculateGood("tan(PI / 4)", 1.0) {
			setVariable("PI", PI)
			addFunction("tan", TanFunction)
		}
	}

	@Test
	fun calculate_whenCotOfPiDividedByTwo_returnsZero() {
		calculateGood("cot(PI / 2)", 0.0) {
			setVariable("PI", PI)
			addFunction("cot", CotFunction)
		}
	}

	@Test
	fun calculate_whenCotOfPiDividedBySix_returnsSqrtOfThree() {
		calculateGood("cot(PI / 6)", sqrt(3.0)) {
			setVariable("PI", PI)
			addFunction("cot", CotFunction)
		}
	}

	@Test
	fun calculate_whenSqrtOfSixteen_returnsFour() {
		calculateGood("sqrt(16)", 4.0) {
			addFunction("sqrt", SqrtFunction)
		}
	}

	@Test
	fun calculate_whenLnOfE_returnsOne() {
		calculateGood("ln(E)", 1.0) {
			setVariable("E", E)
			addFunction("ln", LnFunction)
		}
	}

	@Test
	fun calculate_whenLgOfOneHundred_returnsTwo() {
		calculateGood("lg(100)", 2.0) {
			addFunction("lg", LgFunction)
		}
	}

	@Test
	fun calculate_whenLog2OfEight_returnsThree() {
		calculateGood("log2(8)", 3.0) {
			addFunction("log2", Log2Function)
		}
	}

	@Test
	fun calculate_whenAbsOfMinusTwo_returnsTwo() {
		calculateGood("abs(-2)", 2.0) {
			addFunction("abs", AbsFunction)
		}
	}

	@Test
	fun calculate_whenSecOfZero_returnsOne() {
		calculateGood("sec(0)", 1.0) {
			addFunction("sec", SecFunction)
		}
	}

	@Test
	fun calculate_whenSecOfPiDividedByThree_returnsTwo() {
		calculateGood("sec(PI / 3)", 2.0) {
			setVariable("PI", PI)
			addFunction("sec", SecFunction)
		}
	}

	@Test
	fun calculate_whenCscOfPiDividedBySix_returnsTwo() {
		calculateGood("csc(PI / 6)", 2.0) {
			setVariable("PI", PI)
			addFunction("csc", CscFunction)
		}
	}

	@Test
	fun calculate_whenSecOfPiDividedByTwo_returnsOne() {
		calculateGood("csc(PI / 2)", 1.0) {
			setVariable("PI", PI)
			addFunction("csc", CscFunction)
		}
	}

	@Test
	fun calculate_whenFooWithoutArguments_returnsThirteenPointThirtySeven() {
		calculateGood("foo()", 13.37) {
			addFunction("foo", ZeroArgFunction)
		}
	}

	@Test
	fun calculate_whenBarOfTwoAndFour_returnsSix() {
		calculateGood("bar(2, 4)", 6.0) {
			addFunction("bar", TwoArgFunction)
		}
	}

	@Test
	fun calculate_whenSameNameForVariableAndFunction_returnsSix() {
		calculateGood("bi + bi(2, 3)", 6.0) {
			setVariable("bi", 1.0)
			addFunction("bi", TwoArgFunction)
		}
	}

	@Test
	fun calculate_whenUndefinedVariable_throwsException() {
		calculateBad("x + y", Calculator.ERROR_UNDEFINED_VARIABLE, Token(IDENTIFIER, "y", 4)) {
			setVariable("x", 2.4)
		}
	}

	@Test
	fun calculate_whenUndefinedFunction_throwsException() {
		calculateBad("0.2 * kappa(4)", Calculator.ERROR_UNDEFINED_FUNCTION, Token(IDENTIFIER, "kappa", 6))
	}

	@Test
	fun calculate_whenSinWithoutArguments_throwsException() {
		calculateBad("sin()", Calculator.ERROR_INVALID_ARGUMENTS_COUNT, Token(IDENTIFIER, "sin", 0)) {
			addFunction("sin", SinFunction)
		}
	}

	@Test
	fun calculate_whenSinOfMultipleArguments_throwsException() {
		calculateBad("sin(1, 2)", Calculator.ERROR_INVALID_ARGUMENTS_COUNT, Token(IDENTIFIER, "sin", 0)) {
			addFunction("sin", SinFunction)
		}
	}

	private fun calculateGood(
		expressionText: String,
		expectedResult: Double,
		callback: Calculator.() -> Unit = {}
	) {
		val calculator = createCalculator(expressionText, callback)

		assertEquals(expectedResult, calculator.calculate(), EPS)
	}

	private fun calculateBad(
		expressionText: String,
		expectedMessage: String,
		expectedToken: Token,
		callback: Calculator.() -> Unit = {}
	) {
		val calculator = createCalculator(expressionText, callback)

		try {
			calculator.calculate()
			fail("Calculation should fail")
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

	private object ZeroArgFunction : Function {
		override val arity = 0

		override fun call(arguments: List<Double>) = 13.37
	}

	private object TwoArgFunction : Function {
		override val arity = 2

		override fun call(arguments: List<Double>) = arguments[0] + arguments[1]
	}

	companion object {
		private const val EPS = 1e-12
	}
}
