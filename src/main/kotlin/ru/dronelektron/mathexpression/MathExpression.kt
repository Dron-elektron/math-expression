package ru.dronelektron.mathexpression

import ru.dronelektron.mathexpression.calculator.Calculator
import ru.dronelektron.mathexpression.calculator.functions.*
import ru.dronelektron.mathexpression.calculator.Function
import ru.dronelektron.mathexpression.lexer.Lexer
import ru.dronelektron.mathexpression.parser.Parser
import kotlin.math.E
import kotlin.math.PI

class MathExpression(text: String) {
	private val calculator: Calculator

	init {
		val lexer = Lexer(text)
		val parser = Parser(lexer.tokens)

		calculator = Calculator(parser.root).apply {
			addBaseConstants()
			addBaseFunctions()
		}
	}

	fun setVariable(name: String, value: Double) {
		calculator.setVariable(name, value)
	}

	fun setVariable(name: String, expressionText: String) {
		val mathExpression = MathExpression(expressionText)
		val value = mathExpression.calculate()

		calculator.setVariable(name, value)
	}

	fun addFunction(name: String, function: Function) {
		calculator.addFunction(name, function)
	}

	fun calculate() = calculator.calculate()

	private fun Calculator.addBaseConstants() {
		setVariable("PI", PI)
		setVariable("E", E)
	}

	private fun Calculator.addBaseFunctions() {
		addFunction("abs", AbsFunction)
		addFunction("cos", CosFunction)
		addFunction("cot", CotFunction)
		addFunction("csc", CscFunction)
		addFunction("lg", LgFunction)
		addFunction("ln", LnFunction)
		addFunction("log2", Log2Function)
		addFunction("sec", SecFunction)
		addFunction("sin", SinFunction)
		addFunction("sqrt", SqrtFunction)
		addFunction("tan", TanFunction)
	}
}
