package ru.dronelektron.mathexpression.calculator.functions

import ru.dronelektron.mathexpression.calculator.Function
import kotlin.math.cos

object CosFunction : Function {
	override val arity = 1

	override fun call(arguments: List<Double>) = cos(arguments.first())
}
