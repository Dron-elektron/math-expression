package ru.dronelektron.mathexpression.intermediate

import org.junit.Assert.*
import org.junit.Test
import ru.dronelektron.mathexpression.lexer.Token
import ru.dronelektron.mathexpression.lexer.TokenType
import ru.dronelektron.mathexpression.lexer.TokenType.*
import ru.dronelektron.mathexpression.parser.CstNode

class CstToAstConverterTest {
	@Test
	fun astRoot_whenConstant_shouldConvert() {
		val cstRoot = CstNode(
			Token(CONSTANT, "2.4", 0)
		)

		val expectedAstRoot = AstNode.Constant(2.4)

		convert(cstRoot, expectedAstRoot)
	}

	@Test
	fun astRoot_whenIdentifier_shouldConvertToVariable() {
		val cstRoot = CstNode(
			Token(IDENTIFIER, "eee_EEE_24", 0)
		)

		val expectedAstRoot = AstNode.Variable("eee_EEE_24")

		convert(cstRoot, expectedAstRoot)
	}

	@Test
	fun astRoot_whenAddition_shouldConvert() {
		convertOperation(ADDITION, "+", OperationType.ADDITION)
	}

	@Test
	fun astRoot_whenSubtraction_shouldConvert() {
		convertOperation(SUBTRACTION, "-", OperationType.SUBTRACTION)
	}

	@Test
	fun astRoot_whenMultiplication_shouldConvert() {
		convertOperation(MULTIPLICATION, "*", OperationType.MULTIPLICATION)
	}

	@Test
	fun astRoot_whenDivision_shouldConvert() {
		convertOperation(DIVISION, "/", OperationType.DIVISION)
	}

	@Test
	fun astRoot_whenExponentiation_shouldConvert() {
		convertOperation(EXPONENTIATION, "^", OperationType.EXPONENTIATION)
	}

	@Test
	fun astRoot_whenAdditionAndSubtraction_shouldConvert() {
		val cstRoot = CstNode(
			Token(SUBTRACTION, "-", 6),
			CstNode(
				Token(ADDITION, "+", 2),
				CstNode(Token(CONSTANT, "1", 0)),
				CstNode(Token(CONSTANT, "2", 4))
			),
			CstNode(Token(CONSTANT, "3", 8))
		)

		val expectedAstRoot = AstNode.Operation(
			OperationType.SUBTRACTION,
			AstNode.Operation(
				OperationType.ADDITION,
				AstNode.Constant(1.0),
				AstNode.Constant(2.0)
			),
			AstNode.Constant(3.0)
		)

		convert(cstRoot, expectedAstRoot)
	}

	@Test
	fun astRoot_whenMultiplicationAndDivision_shouldConvert() {
		val cstRoot = CstNode(
			Token(DIVISION, "/", 6),
			CstNode(
				Token(MULTIPLICATION, "*", 2),
				CstNode(Token(CONSTANT, "1", 0)),
				CstNode(Token(CONSTANT, "2", 4))
			),
			CstNode(Token(CONSTANT, "3", 8))
		)

		val expectedAstRoot = AstNode.Operation(
			OperationType.DIVISION,
			AstNode.Operation(
				OperationType.MULTIPLICATION,
				AstNode.Constant(1.0),
				AstNode.Constant(2.0)
			),
			AstNode.Constant(3.0)
		)

		convert(cstRoot, expectedAstRoot)
	}

	@Test
	fun astRoot_whenMultipleExponentiation_shouldConvert() {
		val cstRoot = CstNode(
			Token(EXPONENTIATION, "^", 2),
			CstNode(Token(CONSTANT, "1", 0)),
			CstNode(
				Token(EXPONENTIATION, "^", 6),
				CstNode(Token(CONSTANT, "2", 4)),
				CstNode(Token(CONSTANT, "3", 8))
			)
		)

		val expectedAstRoot = AstNode.Operation(
			OperationType.EXPONENTIATION,
			AstNode.Constant(1.0),
			AstNode.Operation(
				OperationType.EXPONENTIATION,
				AstNode.Constant(2.0),
				AstNode.Constant(3.0)
			)
		)

		convert(cstRoot, expectedAstRoot)
	}

	@Test
	fun astRoot_whenUnary_shouldConvert() {
		val cstRoot = CstNode(
			Token(SUBTRACTION, "-", 0),
			CstNode(Token(CONSTANT, "24", 1))
		)

		val expectedAstRoot = AstNode.Unary(
			AstNode.Constant(24.0)
		)

		convert(cstRoot, expectedAstRoot)
	}

	@Test
	fun astRoot_whenDoubleUnary_shouldConvert() {
		val cstRoot = CstNode(
			Token(SUBTRACTION, "-", 0),
			CstNode(
				Token(SUBTRACTION, "-", 1),
				CstNode(Token(CONSTANT, "24", 2))
			)
		)

		val expectedAstRoot = AstNode.Unary(
			AstNode.Unary(
				AstNode.Constant(24.0)
			)
		)

		convert(cstRoot, expectedAstRoot)
	}

	@Test
	fun astRoot_whenUnaryAfterAddition_shouldConvert() {
		val cstRoot = CstNode(
			Token(ADDITION, "+", 2),
			CstNode(Token(CONSTANT, "2", 0)),
			CstNode(
				Token(SUBTRACTION, "-", 4),
				CstNode(Token(CONSTANT, "3", 5))
			)
		)

		val expectedAstRoot = AstNode.Operation(
			OperationType.ADDITION,
			AstNode.Constant(2.0),
			AstNode.Unary(
				AstNode.Constant(3.0)
			)
		)

		convert(cstRoot, expectedAstRoot)
	}

	@Test
	fun astRoot_whenUnaryAfterSubtraction_shouldConvert() {
		val cstRoot = CstNode(
			Token(SUBTRACTION, "-", 2),
			CstNode(Token(CONSTANT, "2", 0)),
			CstNode(
				Token(SUBTRACTION, "-", 4),
				CstNode(Token(CONSTANT, "3", 5))
			)
		)

		val expectedAstRoot = AstNode.Operation(
			OperationType.SUBTRACTION,
			AstNode.Constant(2.0),
			AstNode.Unary(
				AstNode.Constant(3.0)
			)
		)

		convert(cstRoot, expectedAstRoot)
	}

	@Test
	fun astRoot_whenFunctionCall_shouldConvert() {
		val cstRoot = CstNode(
			Token(IDENTIFIER, "sin", 0),
			CstNode(
				Token(MULTIPLICATION, "*", 6),
				CstNode(Token(CONSTANT, "2", 4)),
				CstNode(Token(IDENTIFIER, "x", 8))
			)
		)

		val expectedAstRoot = AstNode.Function(
			"sin",
			AstNode.Operation(
				OperationType.MULTIPLICATION,
				AstNode.Constant(2.0),
				AstNode.Variable("x")
			)
		)

		convert(cstRoot, expectedAstRoot)
	}

	private fun convertOperation(tokenType: TokenType, tokenLexeme: String, operationType: OperationType) {
		val cstRoot = CstNode(
			Token(tokenType, tokenLexeme, 4),
			CstNode(Token(CONSTANT, "1.2", 0)),
			CstNode(Token(CONSTANT, "0.24", 6))
		)

		val expectedAstRoot = AstNode.Operation(
			operationType,
			AstNode.Constant(1.2),
			AstNode.Constant(0.24)
		)

		convert(cstRoot, expectedAstRoot)
	}

	private fun convert(cstRoot: CstNode, expectedAstRoot: AstNode) {
		val cstToAstConverter = CstToAstConverter(cstRoot)

		assertEquals(expectedAstRoot, cstToAstConverter.astRoot)
	}
}
