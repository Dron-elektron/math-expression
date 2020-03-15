package ru.dronelektron.mathexpression.calculator

import ru.dronelektron.mathexpression.lexer.Token

class CalculatorException(message: String, val token: Token) : Exception(message)
