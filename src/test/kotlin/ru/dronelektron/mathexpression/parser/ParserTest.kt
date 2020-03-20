package ru.dronelektron.mathexpression.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import ru.dronelektron.mathexpression.lexer.Lexer
import ru.dronelektron.mathexpression.lexer.Token
import ru.dronelektron.mathexpression.lexer.TokenType.*

class ParserTest {
	@Test
	fun root_whenConstant_shouldParse() {
		val expectedRoot = AstNode.Constant(2.4)

		parseGood("2.4", expectedRoot)
	}

	@Test
	fun root_whenIdentifier_shouldParse() {
		val expectedRoot = AstNode.Variable(Token(IDENTIFIER, "eee_EEE_24", 0))

		parseGood("eee_EEE_24", expectedRoot)
	}

	@Test
	fun root_whenAddition_shouldParse() {
		val expectedRoot = AstNode.Operation(
			Token(ADDITION, "+", 4),
			AstNode.Constant(1.2),
			AstNode.Constant(0.24)
		)

		parseGood("1.2 + 0.24", expectedRoot)
	}

	@Test
	fun root_whenSubtraction_shouldParse() {
		val expectedRoot = AstNode.Operation(
			Token(SUBTRACTION, "-", 4),
			AstNode.Constant(1.2),
			AstNode.Constant(0.24)
		)

		parseGood("1.2 - 0.24", expectedRoot)
	}

	@Test
	fun root_whenMultiplication_shouldParse() {
		val expectedRoot = AstNode.Operation(
			Token(MULTIPLICATION, "*", 4),
			AstNode.Constant(1.2),
			AstNode.Constant(0.24)
		)

		parseGood("1.2 * 0.24", expectedRoot)
	}

	@Test
	fun root_whenDivision_shouldParse() {
		val expectedRoot = AstNode.Operation(
			Token(DIVISION, "/", 4),
			AstNode.Constant(1.2),
			AstNode.Constant(0.24)
		)

		parseGood("1.2 / 0.24", expectedRoot)
	}

	@Test
	fun root_whenExponentiation_shouldParse() {
		val expectedRoot = AstNode.Operation(
			Token(EXPONENTIATION, "^", 4),
			AstNode.Constant(1.2),
			AstNode.Constant(0.24)
		)

		parseGood("1.2 ^ 0.24", expectedRoot)
	}

	@Test
	fun root_whenAdditionAndSubtraction_shouldParse() {
		val expectedRoot = AstNode.Operation(
			Token(SUBTRACTION, "-", 6),
			AstNode.Operation(
				Token(ADDITION, "+", 2),
				AstNode.Constant(1.0),
				AstNode.Constant(2.0)
			),
			AstNode.Constant(3.0)
		)

		parseGood("1 + 2 - 3", expectedRoot)
	}

	@Test
	fun root_whenMultiplicationAndDivision_shouldParse() {
		val expectedRoot = AstNode.Operation(
			Token(DIVISION, "/", 6),
			AstNode.Operation(
				Token(MULTIPLICATION, "*", 2),
				AstNode.Constant(1.0),
				AstNode.Constant(2.0)
			),
			AstNode.Constant(3.0)
		)

		parseGood("1 * 2 / 3", expectedRoot)
	}

	@Test
	fun root_whenMultipleExponentiation_shouldParse() {
		val expectedRoot = AstNode.Operation(
			Token(EXPONENTIATION, "^", 2),
			AstNode.Constant(1.0),
			AstNode.Operation(
				Token(EXPONENTIATION, "^", 6),
				AstNode.Constant(2.0),
				AstNode.Constant(3.0)
			)
		)

		parseGood("1 ^ 2 ^ 3", expectedRoot)
	}

	@Test
	fun root_whenUnary_shouldParse() {
		val expectedRoot = AstNode.Unary(
			AstNode.Constant(24.0)
		)

		parseGood("-24", expectedRoot)
	}

	@Test
	fun root_whenDoubleUnary_shouldParse() {
		val expectedRoot = AstNode.Unary(
			AstNode.Unary(
				AstNode.Constant(24.0)
			)
		)

		parseGood("--24", expectedRoot)
	}

	@Test
	fun root_whenUnaryAfterAddition_shouldParse() {
		val expectedRoot = AstNode.Operation(
			Token(ADDITION, "+", 2),
			AstNode.Constant(2.0),
			AstNode.Unary(
				AstNode.Constant(3.0)
			)
		)

		parseGood("2 + -3", expectedRoot)
	}

	@Test
	fun root_whenUnaryAfterSubtraction_shouldParse() {
		val expectedRoot = AstNode.Operation(
			Token(SUBTRACTION, "-", 2),
			AstNode.Constant(2.0),
			AstNode.Unary(
				AstNode.Constant(3.0)
			)
		)

		parseGood("2 - -3", expectedRoot)
	}

	@Test
	fun root_whenFunctionCall_shouldParse() {
		val expectedTree = AstNode.Function(
			Token(IDENTIFIER, "sin", 0),
			listOf(
				AstNode.Operation(
					Token(MULTIPLICATION, "*", 6),
					AstNode.Constant(2.0),
					AstNode.Variable(Token(IDENTIFIER, "x", 8))
				)
			)
		)

		parseGood("sin(2 * x)", expectedTree)
	}

	@Test
	fun root_whenEmpty_throwsException() {
		parseBad("", Parser.ERROR_UNEXPECTED_TOKEN, Token(EOF, "", 0))
	}

	@Test
	fun root_whenUnbalancedRightParen_throwsException() {
		parseBad("(", Parser.ERROR_UNEXPECTED_TOKEN, Token(EOF, "", 1))
	}

	@Test
	fun root_whenUnexpectedRightParen_throwsException() {
		parseBad(")", Parser.ERROR_UNEXPECTED_TOKEN, Token(RIGHT_PAREN, ")", 0))
	}

	@Test
	fun root_whenExpectedRightParenAfterExpression_throwsException() {
		parseBad("(2 + 3", Parser.ERROR_EXPECT_RIGHT_PAREN, Token(EOF, "", 6))
	}

	@Test
	fun root_whenIncompleteCall_throwsException() {
		parseBad("sin(2 * x", Parser.ERROR_INCOMPLETE_CALL, Token(EOF, "", 9))
	}

	private fun parseGood(expressionText: String, expectedRoot: AstNode) {
		val lexer = Lexer(expressionText)
		val parser = Parser(lexer.tokens)

		assertEquals(expectedRoot, parser.root)
	}

	private fun parseBad(expressionText: String, expectedMessage: String, expectedToken: Token) {
		try {
			val lexer = Lexer(expressionText)

			Parser(lexer.tokens)
			fail("Parsing should fail")
		} catch (ex: ParserException) {
			assertEquals(expectedMessage, ex.message)
			assertEquals(expectedToken, ex.token)
		}
	}
}
