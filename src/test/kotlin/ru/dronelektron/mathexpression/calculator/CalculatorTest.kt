package ru.dronelektron.mathexpression.calculator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
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

@DisplayName("Calculator tests")
class CalculatorTest {
	@Nested
	@DisplayName("Good calculations")
	inner class GoodCalculations {
		@Test
		@DisplayName("2 returns 2")
		fun two() {
			calculateGood("2", 2.0)
		}

		@Test
		@DisplayName("-2 returns -2")
		fun minusTwo() {
			calculateGood("-2", -2.0)
		}

		@Test
		@DisplayName("--2 returns 2")
		fun minusMinusTwo() {
			calculateGood("--2", 2.0)
		}

		@Test
		@DisplayName("2 + -3 returns -1")
		fun twoPlusMinusThree() {
			calculateGood("2 + -3", -1.0)
		}

		@Test
		@DisplayName("2 - -3 returns 5")
		fun twoMinusMinusThree() {
			calculateGood("2 - -3", 5.0)
		}

		@Test
		@DisplayName("2 - --3 returns -1")
		fun twoMinusMinusMinusThree() {
			calculateGood("2 - --3", -1.0)
		}

		@Test
		@DisplayName("2 + 3 returns 5")
		fun twoPlusThree() {
			calculateGood("2 + 3", 5.0)
		}

		@Test
		@DisplayName("2 - 3 returns -1")
		fun twoMinusThree() {
			calculateGood("2 - 3", -1.0)
		}

		@Test
		@DisplayName("2 * 3 returns 6")
		fun twoMultipliedByThree() {
			calculateGood("2 * 3", 6.0)
		}

		@Test
		@DisplayName("2 / 3 returns 0.666666666666666")
		fun twoDividedByThree() {
			calculateGood("2 / 3", 0.666666666666666)
		}

		@Test
		@DisplayName("2 ^ 3 returns 8")
		fun twoRaisedToThree() {
			calculateGood("2 ^ 3", 8.0)
		}

		@Test
		@DisplayName("2 + 2 - 3 returns 1")
		fun twoPlusTwoMinusThree() {
			calculateGood("2 + 2 - 3", 1.0)
		}

		@Test
		@DisplayName("2 * 2 / 3 returns 1.333333333333333")
		fun twoMultiplyByTwoDividedByThree() {
			calculateGood("2 * 2 / 3", 1.333333333333333)
		}

		@Test
		@DisplayName("2 ^ 2 ^ 3 returns 256")
		fun twoRaisedToTwoRaisedToThree() {
			calculateGood("2 ^ 2 ^ 3", 256.0)
		}

		@Test
		@DisplayName("x returns 2 when x = 2")
		fun xSetToTwo() {
			calculateGood("x", 2.0) {
				setVariable("x", 2.0)
			}
		}

		@Test
		@DisplayName("sin(0) returns 0")
		fun sinOfZero() {
			calculateGood("sin(0)", 0.0) {
				addFunction("sin", SinFunction)
			}
		}

		@Test
		@DisplayName("sin(PI / 2) returns 1")
		fun sinOfPiDividedByTwo() {
			calculateGood("sin(PI / 2)", 1.0) {
				setVariable("PI", PI)
				addFunction("sin", SinFunction)
			}
		}

		@Test
		@DisplayName("cos(0) returns 1")
		fun cosOfZero() {
			calculateGood("cos(0)", 1.0) {
				addFunction("cos", CosFunction)
			}
		}

		@Test
		@DisplayName("cos(PI / 2) returns 0")
		fun cosOfPiDividedByTwo() {
			calculateGood("cos(PI / 2)", 0.0) {
				setVariable("PI", PI)
				addFunction("cos", CosFunction)
			}
		}

		@Test
		@DisplayName("tan(0) returns 0")
		fun tanOfZero() {
			calculateGood("tan(0)", 0.0) {
				addFunction("tan", TanFunction)
			}
		}

		@Test
		@DisplayName("tan(PI / 4) returns 1")
		fun tanOfPiDividedByFour() {
			calculateGood("tan(PI / 4)", 1.0) {
				setVariable("PI", PI)
				addFunction("tan", TanFunction)
			}
		}

		@Test
		@DisplayName("cot(PI / 2) returns 0")
		fun cotOfPiDividedByTwo() {
			calculateGood("cot(PI / 2)", 0.0) {
				setVariable("PI", PI)
				addFunction("cot", CotFunction)
			}
		}

		@Test
		@DisplayName("cot(PI / 6) returns sqrt(3)")
		fun cotOfPiDividedBySix() {
			calculateGood("cot(PI / 6)", sqrt(3.0)) {
				setVariable("PI", PI)
				addFunction("cot", CotFunction)
			}
		}

		@Test
		@DisplayName("sqrt(16) returns 4")
		fun sqrtOfSixteen() {
			calculateGood("sqrt(16)", 4.0) {
				addFunction("sqrt", SqrtFunction)
			}
		}

		@Test
		@DisplayName("ln(E) returns 1")
		fun lnOfE() {
			calculateGood("ln(E)", 1.0) {
				setVariable("E", E)
				addFunction("ln", LnFunction)
			}
		}

		@Test
		@DisplayName("lg(100) returns 2")
		fun lgOfOneHundred() {
			calculateGood("lg(100)", 2.0) {
				addFunction("lg", LgFunction)
			}
		}

		@Test
		@DisplayName("log2(8) returns 3")
		fun log2OfEight() {
			calculateGood("log2(8)", 3.0) {
				addFunction("log2", Log2Function)
			}
		}

		@Test
		@DisplayName("abs(-2) returns 2")
		fun absOfMinusTwo() {
			calculateGood("abs(-2)", 2.0) {
				addFunction("abs", AbsFunction)
			}
		}

		@Test
		@DisplayName("sec(0) returns 1")
		fun secOfZero() {
			calculateGood("sec(0)", 1.0) {
				addFunction("sec", SecFunction)
			}
		}

		@Test
		@DisplayName("sec(PI / 3) returns 2")
		fun secOfPiDividedByThree() {
			calculateGood("sec(PI / 3)", 2.0) {
				setVariable("PI", PI)
				addFunction("sec", SecFunction)
			}
		}

		@Test
		@DisplayName("csc(PI / 6) returns 2")
		fun cscOfPiDividedBySix() {
			calculateGood("csc(PI / 6)", 2.0) {
				setVariable("PI", PI)
				addFunction("csc", CscFunction)
			}
		}

		@Test
		@DisplayName("csc(PI / 2) returns 1")
		fun ecOfPiDividedByTwo() {
			calculateGood("csc(PI / 2)", 1.0) {
				setVariable("PI", PI)
				addFunction("csc", CscFunction)
			}
		}

		@Test
		@DisplayName("foo() returns 13.37 when foo() = 13.37")
		fun fooWithoutArguments() {
			calculateGood("foo()", 13.37) {
				addFunction("foo", ZeroArgFunction)
			}
		}

		@Test
		@DisplayName("bar(2, 4) returns 6 when bar(x, y) = x + y")
		fun barOfTwoAndFour() {
			calculateGood("bar(2, 4)", 6.0) {
				addFunction("bar", TwoArgFunction)
			}
		}

		@Test
		@DisplayName("bi + bi(2, 3) returns 6 when bi = 1 and bi(x, y) = x + y")
		fun sameNameForVariableAndFunction() {
			calculateGood("bi + bi(2, 3)", 6.0) {
				setVariable("bi", 1.0)
				addFunction("bi", TwoArgFunction)
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
	}

	@Nested
	@DisplayName("Bad calculations")
	inner class BadCalculations {
		@Test
		@DisplayName("x + y throws exception with message 'Undefined variable' and token 'y'")
		fun undefinedVariable() {
			calculateBad("x + y", Calculator.ERROR_UNDEFINED_VARIABLE, Token(IDENTIFIER, "y", 4)) {
				setVariable("x", 2.4)
			}
		}

		@Test
		@DisplayName("0.2 * kappa(4) throws exception with message 'Undefined function' and token 'kappa")
		fun undefinedFunction() {
			calculateBad("0.2 * kappa(4)", Calculator.ERROR_UNDEFINED_FUNCTION, Token(IDENTIFIER, "kappa", 6))
		}

		@Test
		@DisplayName("sin() throws exception with message 'Invalid arguments count for function call' and token 'sin'")
		fun sinWithZeroArguments() {
			calculateBad("sin()", Calculator.ERROR_INVALID_ARGUMENTS_COUNT, Token(IDENTIFIER, "sin", 0)) {
				addFunction("sin", SinFunction)
			}
		}

		@Test
		@DisplayName("sin(1, 2) throws exception with message 'Invalid arguments count for function call' and token 'sin'")
		fun sinWithTwoArguments() {
			calculateBad("sin(1, 2)", Calculator.ERROR_INVALID_ARGUMENTS_COUNT, Token(IDENTIFIER, "sin", 0)) {
				addFunction("sin", SinFunction)
			}
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
		private const val EPS = 1e-15
	}
}
