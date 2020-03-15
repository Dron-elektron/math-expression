package ru.dronelektron.mathexpression.calculator.functions

import ru.dronelektron.mathexpression.calculator.Function
import kotlin.math.sin

object SinFunction : Function {
	override val arity = 1

	override fun call(arguments: List<Double>) = sin(arguments.first())
}
