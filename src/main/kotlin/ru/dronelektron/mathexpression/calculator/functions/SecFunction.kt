package ru.dronelektron.mathexpression.calculator.functions

import kotlin.math.cos

object SecFunction : Function {
	override val arity = 1

	override fun call(arguments: List<Double>) = 1.0 / cos(arguments.first())
}
