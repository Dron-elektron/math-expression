package ru.dronelektron.mathexpression.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import ru.dronelektron.mathexpression.lexer.Lexer
import ru.dronelektron.mathexpression.lexer.Token
import ru.dronelektron.mathexpression.lexer.TokenType.*

@DisplayName("Parser tests")
class ParserTest {
	@Nested
	@DisplayName("Good syntax")
	inner class GoodSyntax {
		@Test
		@DisplayName("2.4")
		fun constant() {
			val expectedRoot = AstNode.Constant(2.4)

			parseGood("2.4", expectedRoot)
		}

		@Test
		@DisplayName("eee_EEE_24")
		fun identifier() {
			val expectedRoot = AstNode.Variable(Token(IDENTIFIER, "eee_EEE_24", 0))

			parseGood("eee_EEE_24", expectedRoot)
		}

		@Test
		@DisplayName("1.2 + 0.24")
		fun addition() {
			val expectedRoot = AstNode.Operation(
				Token(ADDITION, "+", 4),
				AstNode.Constant(1.2),
				AstNode.Constant(0.24)
			)

			parseGood("1.2 + 0.24", expectedRoot)
		}

		@Test
		@DisplayName("1.2 - 0.24")
		fun subtraction() {
			val expectedRoot = AstNode.Operation(
				Token(SUBTRACTION, "-", 4),
				AstNode.Constant(1.2),
				AstNode.Constant(0.24)
			)

			parseGood("1.2 - 0.24", expectedRoot)
		}

		@Test
		@DisplayName("1.2 * 0.24")
		fun multiplication() {
			val expectedRoot = AstNode.Operation(
				Token(MULTIPLICATION, "*", 4),
				AstNode.Constant(1.2),
				AstNode.Constant(0.24)
			)

			parseGood("1.2 * 0.24", expectedRoot)
		}

		@Test
		@DisplayName("1.2 / 0.24")
		fun division() {
			val expectedRoot = AstNode.Operation(
				Token(DIVISION, "/", 4),
				AstNode.Constant(1.2),
				AstNode.Constant(0.24)
			)

			parseGood("1.2 / 0.24", expectedRoot)
		}

		@Test
		@DisplayName("1.2 ^ 0.24")
		fun exponentiation() {
			val expectedRoot = AstNode.Operation(
				Token(EXPONENTIATION, "^", 4),
				AstNode.Constant(1.2),
				AstNode.Constant(0.24)
			)

			parseGood("1.2 ^ 0.24", expectedRoot)
		}

		@Test
		@DisplayName("1 + 2 - 3")
		fun additionAndSubtraction() {
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
		@DisplayName("1 * 2 / 3")
		fun multiplicationAndDivision() {
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
		@DisplayName("1 ^ 2 ^ 3")
		fun multipleExponentiation() {
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
		@DisplayName("-24")
		fun unaryConstant() {
			val expectedRoot = AstNode.Unary(
				AstNode.Constant(24.0)
			)

			parseGood("-24", expectedRoot)
		}

		@Test
		@DisplayName("--24")
		fun doubleUnaryConstant() {
			val expectedRoot = AstNode.Unary(
				AstNode.Unary(
					AstNode.Constant(24.0)
				)
			)

			parseGood("--24", expectedRoot)
		}

		@Test
		@DisplayName("2 + -3")
		fun unaryConstantAfterAddition() {
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
		@DisplayName("2 + -3")
		fun unaryConstantAfterSubtraction() {
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
		@DisplayName("sin(2 * x)")
		fun functionCall() {
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

		private fun parseGood(expressionText: String, expectedRoot: AstNode) {
			val lexer = Lexer(expressionText)
			val parser = Parser(lexer.tokens)

			assertEquals(expectedRoot, parser.root)
		}
	}

	@Nested
	@DisplayName("Bad syntax")
	inner class BadSyntax {
		@Test
		@DisplayName("Empty input")
		fun emptyInput() {
			parseBad("", Parser.ERROR_UNEXPECTED_TOKEN, Token(EOF, "", 0))
		}

		@Test
		@DisplayName("(")
		fun unbalancedRightParen() {
			parseBad("(", Parser.ERROR_UNEXPECTED_TOKEN, Token(EOF, "", 1))
		}

		@Test
		@DisplayName(")")
		fun unexpectedRightParen() {
			parseBad(")", Parser.ERROR_UNEXPECTED_TOKEN, Token(RIGHT_PAREN, ")", 0))
		}

		@Test
		@DisplayName("(2 + 3")
		fun expectedRightParenAfterExpression() {
			parseBad("(2 + 3", Parser.ERROR_EXPECT_RIGHT_PAREN, Token(EOF, "", 6))
		}

		@Test
		@DisplayName("sin(2 * x")
		fun incompleteFunctionCall() {
			parseBad("sin(2 * x", Parser.ERROR_INCOMPLETE_CALL, Token(EOF, "", 9))
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
}
