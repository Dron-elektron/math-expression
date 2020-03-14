package ru.dronelektron.mathexpression.calculator.functions

import kotlin.math.sin

object CscFunction : Function {
	override val arity = 1

	override fun call(arguments: List<Double>) = 1.0 / sin(arguments.first())
}
