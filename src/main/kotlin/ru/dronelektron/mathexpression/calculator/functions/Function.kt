package ru.dronelektron.mathexpression.calculator.functions

interface Function {
	val arity: Int

	fun call(arguments: List<Double>): Double
}
