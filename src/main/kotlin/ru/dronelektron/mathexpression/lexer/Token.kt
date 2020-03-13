package ru.dronelektron.mathexpression.lexer

data class Token(
	val type: TokenType,
	val lexeme: String,
	val position: Int
)
