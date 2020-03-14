package ru.dronelektron.mathexpression.calculator.functions

import kotlin.math.sin

object SinFunction : Function {
	override val arity = 1

	override fun call(arguments: List<Double>) = sin(arguments.first())
}
