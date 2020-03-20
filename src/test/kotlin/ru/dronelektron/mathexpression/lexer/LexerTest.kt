package ru.dronelektron.mathexpression.lexer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import ru.dronelektron.mathexpression.lexer.TokenType.*

@DisplayName("Lexer tests")
class LexerTest {
	@Nested
	@DisplayName("Good tokens")
	inner class GoodTokens {
		@Test
		@DisplayName("Empty input")
		fun emptyInput() {
			val expectedTokens = listOf(
				Token(EOF, "", 0)
			)

			scanGood("", expectedTokens)
		}

		@Test
		@DisplayName("Parentheses '(' and ')'")
		fun parentheses() {
			val expectedTokens = listOf(
				Token(LEFT_PAREN, "(", 0),
				Token(RIGHT_PAREN, ")", 1),
				Token(EOF, "", 2)
			)

			scanGood("()", expectedTokens)
		}

		@Test
		@DisplayName("Comma")
		fun comma() {
			val expectedTokens = listOf(
				Token(COMMA, ",", 0),
				Token(EOF, "", 1)
			)

			scanGood(",", expectedTokens)
		}

		@Test
		@DisplayName("Constant 2")
		fun constantWithoutPoint() {
			val expectedTokens = listOf(
				Token(CONSTANT, "2", 0),
				Token(EOF, "", 1)
			)

			scanGood("2", expectedTokens)
		}

		@Test
		@DisplayName("Constant 2.4")
		fun constantWithPoint() {
			val expectedTokens = listOf(
				Token(CONSTANT, "2.4", 0),
				Token(EOF, "", 3)
			)

			scanGood("2.4", expectedTokens)
		}

		@Test
		@DisplayName("Constant .4")
		fun constantWithImplicitZero() {
			val expectedTokens = listOf(
				Token(CONSTANT, ".4", 0),
				Token(EOF, "", 2)
			)

			scanGood(".4", expectedTokens)
		}

		@Test
		@DisplayName("Constants 2.4.3 should be recognized as 2.4 and .3")
		fun mergedConstants() {
			val expectedTokens = listOf(
				Token(CONSTANT, "2.4", 0),
				Token(CONSTANT, ".3", 3),
				Token(EOF, "", 5)
			)

			scanGood("2.4.3", expectedTokens)
		}

		@Test
		@DisplayName("Identifier 'eee_EEE_24'")
		fun identifier() {
			val expectedTokens = listOf(
				Token(IDENTIFIER, "eee_EEE_24", 0),
				Token(EOF, "", 10)
			)

			scanGood("eee_EEE_24", expectedTokens)
		}

		@Test
		@DisplayName("Operations '+', '-', '*', '/', '^'")
		fun operations() {
			val expectedTokens = listOf(
				Token(ADDITION, "+", 0),
				Token(SUBTRACTION, "-", 2),
				Token(MULTIPLICATION, "*", 4),
				Token(DIVISION, "/", 6),
				Token(EXPONENTIATION, "^", 8),
				Token(EOF, "", 9)
			)

			scanGood("+ - * / ^", expectedTokens)
		}

		private fun scanGood(expressionText: String, expectedTokens: List<Token>) {
			val lexer = Lexer(expressionText)

			assertEquals(expectedTokens, lexer.tokens)
		}
	}

	@Nested
	@DisplayName("Bad tokens")
	inner class BadTokens {
		@Test
		@DisplayName("Symbol '@'")
		fun atSign() {
			scanBad("@", Lexer.ERROR_UNKNOWN_TOKEN, 0)
		}

		@Test
		@DisplayName("Symbol '$'")
		fun dollar() {
			scanBad("2 + 3 - $", Lexer.ERROR_UNKNOWN_TOKEN, 8)
		}

		@Test
		@DisplayName("Symbol '!'")
		fun exclamationMark() {
			scanBad("1 * ! ^ 5", Lexer.ERROR_UNKNOWN_TOKEN, 4)
		}

		@Test
		@DisplayName("Invalid constant format 1.")
		fun invalidConstantFormat() {
			scanBad("1.", Lexer.ERROR_INVALID_FORMAT_FOR_CONSTANT, 0)
		}

		@Test
		@DisplayName("Incomplete constant .")
		fun incompleteConstant() {
			scanBad(".", Lexer.ERROR_UNKNOWN_TOKEN, 0)
		}

		private fun scanBad(expressionText: String, expectedMessage: String, expectedPosition: Int) {
			try {
				Lexer(expressionText)
				fail("Scanning should fail")
			} catch (ex: LexerException) {
				assertEquals(expectedMessage, ex.message)
				assertEquals(expectedPosition, ex.position)
			}
		}
	}
}
