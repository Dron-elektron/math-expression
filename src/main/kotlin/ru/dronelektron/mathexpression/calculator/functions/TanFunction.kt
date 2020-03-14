package ru.dronelektron.mathexpression.calculator.functions

import kotlin.math.tan

object TanFunction : Function {
	override val arity = 1

	override fun call(arguments: List<Double>) = tan(arguments.first())
}
