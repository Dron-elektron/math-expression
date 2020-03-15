package ru.dronelektron.mathexpression.parser

import ru.dronelektron.mathexpression.lexer.Token

class ParserException(message: String, val token: Token) : Exception(message)
