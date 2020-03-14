package ru.dronelektron.mathexpression.calculator.functions

import kotlin.math.log10

object LgFunction : Function {
	override val arity = 1

	override fun call(arguments: List<Double>) = log10(arguments.first())
}
