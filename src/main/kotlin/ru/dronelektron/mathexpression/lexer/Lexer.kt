package ru.dronelektron.mathexpression.lexer

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

		addToken(TokenType.EOF, "", text.length)
	}

	private fun scanToken() {
		startIndex = currentIndex

		when (val ch = advance()) {
			'+' -> addToken(TokenType.ADDITION)
			'-' -> addToken(TokenType.SUBTRACTION)
			'*' -> addToken(TokenType.MULTIPLICATION)
			'/' -> addToken(TokenType.DIVISION)
			'^' -> addToken(TokenType.EXPONENTIATION)
			'(' -> addToken(TokenType.LEFT_PAREN)
			')' -> addToken(TokenType.RIGHT_PAREN)
			'.' -> scanNumber(startsWithPoint = true)
			' ', '\t', '\n' -> {}

			else -> when {
				isDigit(ch) -> scanNumber(startsWithPoint = false)
				isLetter(ch) -> scanIdentifier()
				else -> throw LexerException(ERROR_UNKNOWN_TOKEN, startIndex)
			}
		}
	}

	private fun scanNumber(startsWithPoint: Boolean) {
		while (isDigit(peekChar())) advance()

		if (peekChar() == '.' && !startsWithPoint) {
			advance()

			while (isDigit(peekChar())) advance()
		}

		addToken(TokenType.CONSTANT)
	}

	private fun scanIdentifier() {
		while (isLetterOrDigit(peekChar())) advance()

		addToken(TokenType.IDENTIFIER)
	}

	private fun addToken(type: TokenType, lexeme: String = getLexeme(), position: Int = startIndex) {
		_tokens.add(Token(type, lexeme, position))
	}

	private fun advance(): Char {
		currentIndex++

		return text[currentIndex - 1]
	}

	private fun peekChar() = if (isNotAtEnd()) text[currentIndex] else EOF

	private fun getLexeme() = text.substring(startIndex until currentIndex)

	private fun isNotAtEnd() = currentIndex < text.length

	private fun isDigit(ch: Char) = ch in '0'..'9'

	private fun isLetter(ch: Char) = ch in 'a'..'z' || ch in 'A'..'Z' || ch == '_'

	private fun isLetterOrDigit(ch: Char) = isLetter(ch) || isDigit(ch)

	companion object {
		private const val EOF = '\u0000'

		const val ERROR_UNKNOWN_TOKEN = "Unknown token"
	}
}
