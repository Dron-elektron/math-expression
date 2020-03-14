package ru.dronelektron.mathexpression.calculator

import ru.dronelektron.mathexpression.calculator.functions.Function
import ru.dronelektron.mathexpression.lexer.Token
import ru.dronelektron.mathexpression.lexer.TokenType.*
import ru.dronelektron.mathexpression.parser.AstNode
import kotlin.math.pow

class Calculator(private val root: AstNode) {
	private val variables = mutableMapOf<String, Double>()
	private val functions = mutableMapOf<String, Function>()

	fun setVariable(name: String, value: Double) {
		variables[name] = value
	}

	fun addFunction(name: String, function: Function) {
		functions[name] = function
	}

	fun calculate() = calculate(root)

	private fun calculate(root: AstNode): Double = when (root) {
		is AstNode.Constant -> root.value
		is AstNode.Variable -> calculateVariable(root)
		is AstNode.Function -> calculateFunction(root)
		is AstNode.Unary -> -calculate(root.expression)

		is AstNode.Operation -> {
			val leftResult = calculate(root.left)
			val rightResult = calculate(root.right)

			when (root.token.type) {
				ADDITION -> leftResult + rightResult
				SUBTRACTION -> leftResult - rightResult
				MULTIPLICATION -> leftResult * rightResult
				DIVISION -> leftResult / rightResult
				EXPONENTIATION -> leftResult.pow(rightResult)
				else -> error(ERROR_UNSUPPORTED_OPERATION, root.token)
			}
		}
	}

	private fun calculateVariable(root: AstNode.Variable): Double {
		val token = root.token

		return variables[token.lexeme] ?: error(ERROR_UNDEFINED_VARIABLE, token)
	}

	private fun calculateFunction(root: AstNode.Function): Double {
		val token = root.token
		val function = functions[token.lexeme] ?: error(ERROR_UNDEFINED_FUNCTION, token)

		if (function.arity != root.arguments.size) error(ERROR_INVALID_ARGUMENTS_COUNT, token)

		val arguments = root.arguments.map { calculate(it) }

		return function.call(arguments)
	}

	private fun error(message: String, token: Token): Nothing = throw CalculatorException(message, token)

	companion object {
		const val ERROR_UNDEFINED_VARIABLE = "Undefined variable"
		const val ERROR_UNDEFINED_FUNCTION = "Undefined function"
		const val ERROR_INVALID_ARGUMENTS_COUNT = "Invalid arguments count for function call"
		const val ERROR_UNSUPPORTED_OPERATION = "Unsupported operation"
	}
}
