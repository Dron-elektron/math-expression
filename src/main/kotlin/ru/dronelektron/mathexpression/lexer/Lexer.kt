package ru.dronelektron.mathexpression.lexer

import ru.dronelektron.mathexpression.lexer.TokenType.*

class Lexer(private val text: String) {
	private val _tokens = mutableListOf<Token>()
	private var startIndex = 0
	private var currentIndex = 0

	val tokens: List<Token> = _tokens

	init {
		scanTokens()
	}

	private fun scanTokens() {
		while (isNotAtEnd()) scanToken()

		addToken(EOF, "", text.length)
	}

	private fun scanToken() {
		startIndex = currentIndex

		when (val ch = advance()) {
			'+' -> addToken(ADDITION)
			'-' -> addToken(SUBTRACTION)
			'*' -> addToken(MULTIPLICATION)
			'/' -> addToken(DIVISION)
			'^' -> addToken(EXPONENTIATION)
			'(' -> addToken(LEFT_PAREN)
			')' -> addToken(RIGHT_PAREN)
			'.' -> scanNumber(startsWithPoint = true)
			' ', '\t', '\n' -> {}

			else -> when {
				isDigit(ch) -> scanNumber(startsWithPoint = false)
				isLetter(ch) -> scanIdentifier()
				else -> error(ERROR_UNKNOWN_TOKEN)
			}
		}
	}

	private fun scanNumber(startsWithPoint: Boolean) {
		if (startsWithPoint && !isDigit(peekChar())) error(ERROR_UNKNOWN_TOKEN)

		while (isDigit(peekChar())) advance()

		if (peekChar() == '.' && !startsWithPoint) {
			advance()

			if (!isDigit(peekChar())) error(ERROR_INVALID_FORMAT_FOR_CONSTANT)

			while (isDigit(peekChar())) advance()
		}

		addToken(CONSTANT)
	}

	private fun scanIdentifier() {
		while (isLetterOrDigit(peekChar())) advance()

		addToken(IDENTIFIER)
	}

	private fun addToken(type: TokenType, lexeme: String = getLexeme(), position: Int = startIndex) {
		_tokens.add(Token(type, lexeme, position))
	}

	private fun advance(): Char {
		currentIndex++

		return text[currentIndex - 1]
	}

	private fun peekChar() = if (isNotAtEnd()) text[currentIndex] else NO_CHAR

	private fun getLexeme() = text.substring(startIndex until currentIndex)

	private fun isNotAtEnd() = currentIndex < text.length

	private fun isDigit(ch: Char) = ch in '0'..'9'

	private fun isLetter(ch: Char) = ch in 'a'..'z' || ch in 'A'..'Z' || ch == '_'

	private fun isLetterOrDigit(ch: Char) = isLetter(ch) || isDigit(ch)

	private fun error(message: String): Nothing = throw LexerException(message, startIndex)

	companion object {
		private const val NO_CHAR = '\u0000'

		const val ERROR_UNKNOWN_TOKEN = "Unknown token"
		const val ERROR_INVALID_FORMAT_FOR_CONSTANT = "Invalid format for constant"
	}
}
