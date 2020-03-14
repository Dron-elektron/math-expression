package ru.dronelektron.mathexpression.calculator.functions

import kotlin.math.ln

object LnFunction : Function {
	override val arity = 1

	override fun call(arguments: List<Double>) = ln(arguments.first())
}
