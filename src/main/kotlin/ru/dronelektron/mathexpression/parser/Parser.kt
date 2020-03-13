package ru.dronelektron.mathexpression.parser

import ru.dronelektron.mathexpression.lexer.Token
import ru.dronelektron.mathexpression.lexer.TokenType
import ru.dronelektron.mathexpression.lexer.TokenType.*

class Parser(private val tokens: List<Token>) {
	private var currentIndex = 0

	val root = parseAddition().also {
		consumeToken(EOF, ERROR_UNEXPECTED_TOKEN)
	}

	private fun parseAddition() = parseOperations(ADDITION, SUBTRACTION) {
		parseMultiplication()
	}

	private fun parseMultiplication() = parseOperations(MULTIPLICATION, DIVISION) {
		parseExponentiation()
	}

	private fun parseOperations(vararg tokenTypes: TokenType, parseSubRule: () -> ConcreteNode): ConcreteNode {
		var left = parseSubRule()

		while (match(*tokenTypes)) {
			left = ConcreteNode(previousToken(), left, parseSubRule())
		}

		return left
	}

	private fun parseExponentiation(): ConcreteNode {
		val left = parseUnary()

		return if (match(EXPONENTIATION)) {
			ConcreteNode(previousToken(), left, parseExponentiation())
		} else {
			left
		}
	}

	private fun parseUnary(): ConcreteNode = if (match(SUBTRACTION)) {
		ConcreteNode(previousToken(), parseUnary())
	} else {
		parsePrimary()
	}

	private fun parsePrimary() = when {
		match(CONSTANT) -> ConcreteNode(previousToken())
		match(IDENTIFIER) -> parseIdentifier()

		match(LEFT_PAREN) -> parseAddition().also {
			consumeToken(RIGHT_PAREN, ERROR_EXPECT_RIGHT_PAREN)
		}

		else -> error(ERROR_UNEXPECTED_TOKEN)
	}

	private fun parseIdentifier(): ConcreteNode {
		val token = previousToken()

		if (!match(LEFT_PAREN)) return ConcreteNode(token)

		return ConcreteNode(token, parseArguments()).also {
			consumeToken(RIGHT_PAREN, ERROR_INCOMPLETE_CALL)
		}
	}

	private fun parseArguments(): ConcreteNode {
		// TODO: Multiple arguments
		return parseAddition()
	}

	private fun match(vararg tokensTypes: TokenType): Boolean {
		val matched = tokensTypes.any { peekToken().type == it }

		if (matched) advance()

		return matched
	}

	private fun advance() {
		if (!isAtEnd()) currentIndex++
	}

	private fun consumeToken(tokenType: TokenType, message: String) {
		if (!match(tokenType)) error(message)
	}

	private fun isAtEnd() = currentIndex >= tokens.size

	private fun peekToken() = tokens[currentIndex]

	private fun previousToken() = tokens[currentIndex - 1]

	private fun error(message: String): Nothing = throw ParserException(message, peekToken())

	companion object {
		const val ERROR_EXPECT_RIGHT_PAREN = "Expect ')' after expression"
		const val ERROR_UNEXPECTED_TOKEN = "Unexpected token"
		const val ERROR_INCOMPLETE_CALL = "Incomplete function call"
	}
}
