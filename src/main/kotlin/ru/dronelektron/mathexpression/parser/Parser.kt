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

	private fun parseOperations(vararg tokenTypes: TokenType, parseSubRule: () -> AstNode): AstNode {
		var left = parseSubRule()

		while (match(*tokenTypes)) {
			val token = previousToken()
			val right = parseSubRule()

			left = AstNode.Operation(token, left, right)
		}

		return left
	}

	private fun parseExponentiation(): AstNode {
		val left = parseUnary()

		return if (match(EXPONENTIATION)) {
			val token = previousToken()
			val right = parseExponentiation()

			AstNode.Operation(token, left, right)
		} else {
			left
		}
	}

	private fun parseUnary(): AstNode = if (match(SUBTRACTION)) {
		val expression = parseUnary()

		AstNode.Unary(expression)
	} else {
		parsePrimary()
	}

	private fun parsePrimary() = when {
		match(CONSTANT) -> AstNode.Constant(previousToken().lexeme.toDouble())
		match(IDENTIFIER) -> parseIdentifier()

		match(LEFT_PAREN) -> parseAddition().also {
			consumeToken(RIGHT_PAREN, ERROR_EXPECT_RIGHT_PAREN)
		}

		else -> error(ERROR_UNEXPECTED_TOKEN)
	}

	private fun parseIdentifier(): AstNode {
		val token = previousToken()

		if (!match(LEFT_PAREN)) return AstNode.Variable(token)

		val argument = parseArgument()

		return AstNode.Function(token, argument).also {
			consumeToken(RIGHT_PAREN, ERROR_INCOMPLETE_CALL)
		}
	}

	private fun parseArgument(): AstNode {
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
