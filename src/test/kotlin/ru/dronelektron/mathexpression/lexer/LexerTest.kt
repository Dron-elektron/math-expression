package ru.dronelektron.mathexpression.lexer

import org.junit.Assert.*
import org.junit.Test

class LexerTest {
	@Test
	fun tokens_whenEmptyInput_returnsEof() {
		val lexer = Lexer("")

		val expectedTokens = listOf(
			Token(TokenType.EOF, "", 0)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenLeftParenAndRightParen_returnsBoth() {
		val lexer = Lexer("()")

		val expectedTokens = listOf(
			Token(TokenType.LEFT_PAREN, "(", 0),
			Token(TokenType.RIGHT_PAREN, ")", 1),
			Token(TokenType.EOF, "", 2)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenConstantWithoutPoint_returnsIt() {
		val lexer = Lexer("2")

		val expectedTokens = listOf(
			Token(TokenType.CONSTANT, "2", 0),
			Token(TokenType.EOF, "", 1)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenConstantWithPoint_returnsIt() {
		val lexer = Lexer("2.4")

		val expectedTokens = listOf(
			Token(TokenType.CONSTANT, "2.4", 0),
			Token(TokenType.EOF, "", 3)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenConstantStartsWithPoint_returnsIt() {
		val lexer = Lexer(".4")

		val expectedTokens = listOf(
			Token(TokenType.CONSTANT, ".4", 0),
			Token(TokenType.EOF, "", 2)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenSecondPointAfterConstantIsDetected_shouldBeIgnored() {
		val lexer = Lexer("2.4.3")

		val expectedTokens = listOf(
			Token(TokenType.CONSTANT, "2.4", 0),
			Token(TokenType.CONSTANT, ".3", 3),
			Token(TokenType.EOF, "", 5)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenIdentifier_returnsIt() {
		val lexer = Lexer("eee_EEE_24")

		val expectedTokens = listOf(
			Token(TokenType.IDENTIFIER, "eee_EEE_24", 0),
			Token(TokenType.EOF, "", 10)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenOperations_returnsAll() {
		val lexer = Lexer("+ - * / ^")

		val expectedTokens = listOf(
			Token(TokenType.ADDITION, "+", 0),
			Token(TokenType.SUBTRACTION, "-", 2),
			Token(TokenType.MULTIPLICATION, "*", 4),
			Token(TokenType.DIVISION, "/", 6),
			Token(TokenType.EXPONENTIATION, "^", 8),
			Token(TokenType.EOF, "", 9)
		)

		assertEquals(expectedTokens, lexer.tokens)
	}

	@Test
	fun tokens_whenUnknownTokenAtPositionZero_throwsException() {
		scanBad("@", 0)
	}

	@Test
	fun tokens_whenUnknownTokenAtTheEnd_throwsException() {
		scanBad("2 + 3 - $", 8)
	}

	@Test
	fun tokens_whenUnknownTokenAtPositionFour_throwsException() {
		scanBad("1 * ! ^ 5", 4)
	}

	@Test
	fun tokens_whenUnknownTokenAtPositionFourteen_throwsException() {
		scanBad("2 + x1 * kappa$123", 14)
	}

	private fun scanBad(expressionText: String, expectedPosition: Int) {
		try {
			Lexer(expressionText)
			fail()
		} catch (ex: LexerException) {
			assertEquals(Lexer.ERROR_UNKNOWN_TOKEN, ex.message)
			assertEquals(expectedPosition, ex.position)
		}
	}
}
