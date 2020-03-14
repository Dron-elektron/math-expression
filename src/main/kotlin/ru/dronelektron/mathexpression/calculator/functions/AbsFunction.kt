package ru.dronelektron.mathexpression.calculator.functions

import kotlin.math.abs

object AbsFunction : Function {
	override val arity = 1

	override fun call(arguments: List<Double>) = abs(arguments.first())
}
