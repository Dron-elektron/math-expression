package ru.dronelektron.mathexpression.parser

import ru.dronelektron.mathexpression.lexer.Token

data class ConcreteNode(
	val token: Token,
	val left: ConcreteNode? = null,
	val right: ConcreteNode? = null
)
