package ru.dronelektron.mathexpression.parser

import org.junit.Assert.*
import org.junit.Test
import ru.dronelektron.mathexpression.lexer.Lexer
import ru.dronelektron.mathexpression.lexer.Token
import ru.dronelektron.mathexpression.lexer.TokenType.*

class ParserTest {
	@Test
	fun root_whenConstant_shouldParse() {
		val expectedRoot = ConcreteNode(
			Token(CONSTANT, "2.4", 0)
		)

		parseGood("2.4", expectedRoot)
	}

	@Test
	fun root_whenIdentifier_shouldParse() {
		val expectedRoot = ConcreteNode(
			Token(IDENTIFIER, "eee_EEE_24", 0)
		)

		parseGood("eee_EEE_24", expectedRoot)
	}

	@Test
	fun root_whenAddition_shouldParse() {
		val expectedRoot = ConcreteNode(
			Token(ADDITION, "+", 4),
			ConcreteNode(Token(CONSTANT, "1.2", 0)),
			ConcreteNode(Token(CONSTANT, "0.24", 6))
		)

		parseGood("1.2 + 0.24", expectedRoot)
	}

	@Test
	fun root_whenSubtraction_shouldParse() {
		val expectedRoot = ConcreteNode(
			Token(SUBTRACTION, "-", 4),
			ConcreteNode(Token(CONSTANT, "1.2", 0)),
			ConcreteNode(Token(CONSTANT, "0.24", 6))
		)

		parseGood("1.2 - 0.24", expectedRoot)
	}

	@Test
	fun root_whenMultiplication_shouldParse() {
		val expectedRoot = ConcreteNode(
			Token(MULTIPLICATION, "*", 4),
			ConcreteNode(Token(CONSTANT, "1.2", 0)),
			ConcreteNode(Token(CONSTANT, "0.24", 6))
		)

		parseGood("1.2 * 0.24", expectedRoot)
	}

	@Test
	fun root_whenDivision_shouldParse() {
		val expectedRoot = ConcreteNode(
			Token(DIVISION, "/", 4),
			ConcreteNode(Token(CONSTANT, "1.2", 0)),
			ConcreteNode(Token(CONSTANT, "0.24", 6))
		)

		parseGood("1.2 / 0.24", expectedRoot)
	}

	@Test
	fun root_whenExponentiation_shouldParse() {
		val expectedRoot = ConcreteNode(
			Token(EXPONENTIATION, "^", 4),
			ConcreteNode(Token(CONSTANT, "1.2", 0)),
			ConcreteNode(Token(CONSTANT, "0.24", 6))
		)

		parseGood("1.2 ^ 0.24", expectedRoot)
	}

	@Test
	fun root_whenAdditionAndSubtraction_shouldParse() {
		val expectedRoot = ConcreteNode(
			Token(SUBTRACTION, "-", 6),
			ConcreteNode(
				Token(ADDITION, "+", 2),
				ConcreteNode(Token(CONSTANT, "1", 0)),
				ConcreteNode(Token(CONSTANT, "2", 4))
			),
			ConcreteNode(Token(CONSTANT, "3", 8))
		)

		parseGood("1 + 2 - 3", expectedRoot)
	}

	@Test
	fun root_whenMultiplicationAndDivision_shouldParse() {
		val expectedRoot = ConcreteNode(
			Token(DIVISION, "/", 6),
			ConcreteNode(
				Token(MULTIPLICATION, "*", 2),
				ConcreteNode(Token(CONSTANT, "1", 0)),
				ConcreteNode(Token(CONSTANT, "2", 4))
			),
			ConcreteNode(Token(CONSTANT, "3", 8))
		)

		parseGood("1 * 2 / 3", expectedRoot)
	}

	@Test
	fun root_whenMultipleExponentiation_shouldParse() {
		val expectedRoot = ConcreteNode(
			Token(EXPONENTIATION, "^", 2),
			ConcreteNode(Token(CONSTANT, "1", 0)),
			ConcreteNode(
				Token(EXPONENTIATION, "^", 6),
				ConcreteNode(Token(CONSTANT, "2", 4)),
				ConcreteNode(Token(CONSTANT, "3", 8))
			)
		)

		parseGood("1 ^ 2 ^ 3", expectedRoot)
	}

	@Test
	fun root_whenUnary_shouldParse() {
		val expectedRoot = ConcreteNode(
			Token(SUBTRACTION, "-", 0),
			ConcreteNode(Token(CONSTANT, "24", 1))
		)

		parseGood("-24", expectedRoot)
	}

	@Test
	fun root_whenDoubleUnary_shouldParse() {
		val expectedRoot = ConcreteNode(
			Token(SUBTRACTION, "-", 0),
			ConcreteNode(
				Token(SUBTRACTION, "-", 1),
				ConcreteNode(Token(CONSTANT, "24", 2))
			)
		)

		parseGood("--24", expectedRoot)
	}

	@Test
	fun root_whenUnaryAfterAddition_shouldParse() {
		val expectedRoot = ConcreteNode(
			Token(ADDITION, "+", 2),
			ConcreteNode(Token(CONSTANT, "2", 0)),
			ConcreteNode(
				Token(SUBTRACTION, "-", 4),
				ConcreteNode(Token(CONSTANT, "3", 5))
			)
		)

		parseGood("2 + -3", expectedRoot)
	}

	@Test
	fun root_whenUnaryAfterSubtraction_shouldParse() {
		val expectedRoot = ConcreteNode(
			Token(SUBTRACTION, "-", 2),
			ConcreteNode(Token(CONSTANT, "2", 0)),
			ConcreteNode(
				Token(SUBTRACTION, "-", 4),
				ConcreteNode(Token(CONSTANT, "3", 5))
			)
		)

		parseGood("2 - -3", expectedRoot)
	}

	@Test
	fun root_whenEmpty_throwsException() {
		parseBad("", Parser.ERROR_UNEXPECTED_TOKEN, eof(0))
	}

	@Test
	fun root_whenUnbalancedRightParen_throwsException() {
		parseBad("(", Parser.ERROR_UNEXPECTED_TOKEN, eof(1))
	}

	@Test
	fun root_whenUnexpectedRightParen_throwsException() {
		parseBad(")", Parser.ERROR_UNEXPECTED_TOKEN, Token(RIGHT_PAREN, ")", 0))
	}

	@Test
	fun root_whenExpectedRightParenAfterExpression_throwsException() {
		parseBad("(2 + 3", Parser.ERROR_EXPECT_RIGHT_PAREN, eof(6))
	}

	@Test
	fun root_whenIncompleteCall_throwsException() {
		parseBad("sin(2 * x", Parser.ERROR_INCOMPLETE_CALL, eof(9))
	}

	private fun parseGood(expressionText: String, expectedRoot: ConcreteNode) {
		val lexer = Lexer(expressionText)
		val parser = Parser(lexer.tokens)

		assertEquals(expectedRoot, parser.root)
	}

	private fun parseBad(expressionText: String, expectedMessage: String, expectedToken: Token) {
		try {
			val lexer = Lexer(expressionText)

			Parser(lexer.tokens)
			fail()
		} catch (ex: ParserException) {
			assertEquals(expectedMessage, ex.message)
			assertEquals(expectedToken, ex.token)
		}
	}

	private fun eof(position: Int) = Token(EOF, "", position)
}
