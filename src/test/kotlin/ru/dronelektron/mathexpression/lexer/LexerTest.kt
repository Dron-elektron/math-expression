package ru.dronelektron.mathexpression.lexer

import org.junit.Assert.*
import org.junit.Test
import ru.dronelektron.mathexpression.lexer.TokenType.*

class LexerTest {
	@Test
	fun tokens_whenEmptyInput_returnsEof() {
		val lexer = Lexer("")

		val expectedTokens = listOf(
			Token(EOF, "", 0)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenLeftParenAndRightParen_returnsBoth() {
		val lexer = Lexer("()")

		val expectedTokens = listOf(
			Token(LEFT_PAREN, "(", 0),
			Token(RIGHT_PAREN, ")", 1),
			Token(EOF, "", 2)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenConstantWithoutPoint_returnsIt() {
		val lexer = Lexer("2")

		val expectedTokens = listOf(
			Token(CONSTANT, "2", 0),
			Token(EOF, "", 1)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenConstantWithPoint_returnsIt() {
		val lexer = Lexer("2.4")

		val expectedTokens = listOf(
			Token(CONSTANT, "2.4", 0),
			Token(EOF, "", 3)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenConstantStartsWithPoint_returnsIt() {
		val lexer = Lexer(".4")

		val expectedTokens = listOf(
			Token(CONSTANT, ".4", 0),
			Token(EOF, "", 2)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenSecondPointAfterConstantIsDetected_shouldBeIgnored() {
		val lexer = Lexer("2.4.3")

		val expectedTokens = listOf(
			Token(CONSTANT, "2.4", 0),
			Token(CONSTANT, ".3", 3),
			Token(EOF, "", 5)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenIdentifier_returnsIt() {
		val lexer = Lexer("eee_EEE_24")

		val expectedTokens = listOf(
			Token(IDENTIFIER, "eee_EEE_24", 0),
			Token(EOF, "", 10)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenOperations_returnsAll() {
		val lexer = Lexer("+ - * / ^")

		val expectedTokens = listOf(
			Token(ADDITION, "+", 0),
			Token(SUBTRACTION, "-", 2),
			Token(MULTIPLICATION, "*", 4),
			Token(DIVISION, "/", 6),
			Token(EXPONENTIATION, "^", 8),
			Token(EOF, "", 9)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenUnknownTokenAtPositionZero_throwsException() {
		scanBad("@", Lexer.ERROR_UNKNOWN_TOKEN, 0)
	}

	@Test
	fun tokens_whenUnknownTokenAtTheEnd_throwsException() {
		scanBad("2 + 3 - $", Lexer.ERROR_UNKNOWN_TOKEN, 8)
	}

	@Test
	fun tokens_whenUnknownTokenAtPositionFour_throwsException() {
		scanBad("1 * ! ^ 5", Lexer.ERROR_UNKNOWN_TOKEN, 4)
	}

	@Test
	fun tokens_whenUnknownTokenAtPositionFourteen_throwsException() {
		scanBad("2 + x1 * kappa$123", Lexer.ERROR_UNKNOWN_TOKEN, 14)
	}

	@Test
	fun tokens_whenConstantWithRedundantPoint_throwsException() {
		scanBad("1.", Lexer.ERROR_INVALID_FORMAT_FOR_CONSTANT, 0)
	}

	@Test
	fun tokens_whenIncompleteConstant_throwsException() {
		scanBad(".", Lexer.ERROR_UNKNOWN_TOKEN, 0)
	}

	private fun scanBad(expressionText: String, expectedMessage: String, expectedPosition: Int) {
		try {
			Lexer(expressionText)
			fail()
		} catch (ex: LexerException) {
			assertEquals(expectedMessage, ex.message)
			assertEquals(expectedPosition, ex.position)
		}
	}
}
