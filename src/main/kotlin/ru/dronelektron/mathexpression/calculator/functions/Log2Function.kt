package ru.dronelektron.mathexpression.calculator.functions

import kotlin.math.log2

object Log2Function : Function {
	override val arity = 1

	override fun call(arguments: List<Double>) = log2(arguments.first())
}
