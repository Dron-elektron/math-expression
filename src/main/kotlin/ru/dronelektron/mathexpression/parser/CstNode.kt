package ru.dronelektron.mathexpression.parser

import ru.dronelektron.mathexpression.lexer.Token

data class CstNode(
	val token: Token,
	val left: CstNode? = null,
	val right: CstNode? = null
)
