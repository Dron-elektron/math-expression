package ru.dronelektron.mathexpression.parser

import ru.dronelektron.mathexpression.lexer.Token

sealed class AstNode {
	data class Operation(val token: Token, val left: AstNode, val right: AstNode) : AstNode()
	data class Unary(val expression: AstNode) : AstNode()
	data class Constant(val value: Double) : AstNode()
	data class Variable(val token: Token) : AstNode()
	data class Function(val token: Token, val arguments: List<AstNode>) : AstNode()
}
