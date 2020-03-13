package ru.dronelektron.mathexpression.intermediate

sealed class AstNode {
	data class Operation(val type: OperationType, val left: AstNode, val right: AstNode) : AstNode()
	data class Unary(val expression: AstNode) : AstNode()
	data class Constant(val value: Double) : AstNode()
	data class Variable(val name: String) : AstNode()
	data class Function(val name: String, val argument: AstNode) : AstNode()
}
