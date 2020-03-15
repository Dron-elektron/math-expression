package ru.dronelektron.mathexpression.calculator.functions

import ru.dronelektron.mathexpression.calculator.Function
import kotlin.math.tan

object CotFunction : Function {
	override val arity = 1

	override fun call(arguments: List<Double>) = 1.0 / tan(arguments.first())
}
