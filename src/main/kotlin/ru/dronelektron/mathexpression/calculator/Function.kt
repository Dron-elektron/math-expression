package ru.dronelektron.mathexpression.calculator

interface Function {
	val arity: Int

	fun call(arguments: List<Double>): Double
}
