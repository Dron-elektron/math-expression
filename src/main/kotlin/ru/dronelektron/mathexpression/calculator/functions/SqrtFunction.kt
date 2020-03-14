package ru.dronelektron.mathexpression.calculator.functions

import kotlin.math.sqrt

object SqrtFunction : Function {
	override val arity = 1

	override fun call(arguments: List<Double>) = sqrt(arguments.first())
}
