package ru.dronelektron.mathexpression.calculator.functions

import ru.dronelektron.mathexpression.calculator.Function
import kotlin.math.ln

object LnFunction : Function {
	override val arity = 1

	override fun call(arguments: List<Double>) = ln(arguments.first())
}
