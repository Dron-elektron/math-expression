package ru.dronelektron.mathexpression.intermediate

import ru.dronelektron.mathexpression.lexer.TokenType
import ru.dronelektron.mathexpression.parser.CstNode

class CstToAstConverter(cstRoot: CstNode) {
	val astRoot = convert(cstRoot)

	private fun convert(root: CstNode) = when (root.token.type) {
		TokenType.CONSTANT -> AstNode.Constant(root.token.lexeme.toDouble())
		TokenType.IDENTIFIER -> convertIdentifier(root)
		in tokenTypeToOperation -> convertOperation(root)
		else -> throw Exception("Invalid CstNode")
	}

	private fun convertIdentifier(root: CstNode): AstNode {
		val left = root.left
		val lexeme = root.token.lexeme

		return if (left == null) {
			AstNode.Variable(lexeme)
		} else {
			AstNode.Function(lexeme, convert(left))
		}
	}

	private fun convertOperation(root: CstNode): AstNode {
		val left = root.left ?: throw Exception("No left child for operation")
		val right = root.right

		return if (right == null) {
			AstNode.Unary(convert(left))
		} else {
			AstNode.Operation(root.token.type.toOperation(), convert(left), convert(right))
		}
	}

	private fun TokenType.toOperation() = tokenTypeToOperation[this] ?: throw Exception("Unsupported token type")

	companion object {
		private val tokenTypeToOperation = mapOf(
			TokenType.ADDITION to OperationType.ADDITION,
			TokenType.SUBTRACTION to OperationType.SUBTRACTION,
			TokenType.MULTIPLICATION to OperationType.MULTIPLICATION,
			TokenType.DIVISION to OperationType.DIVISION,
			TokenType.EXPONENTIATION to OperationType.EXPONENTIATION
		)
	}
}
