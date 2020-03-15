package ru.dronelektron.mathexpression.lexer

enum class TokenType {
	// Literals
	CONSTANT,
	IDENTIFIER,

	// Operations
	ADDITION,
	SUBTRACTION,
	MULTIPLICATION,
	DIVISION,
	EXPONENTIATION,

	// Parentheses
	LEFT_PAREN,
	RIGHT_PAREN,

	COMMA,
	EOF
}
