# Math Expression

A library for parsing math expressions.

## Features

* Variables definition using values or a math expressions
* Custom functions support with any number of parameters
* Pre-defined constants
* Pre-defined functions

## Operators

| Name           | Operator |
|----------------|----------|
| Addition       | +        |
| Subtraction    | -        |
| Multiplication | *        |
| Division       | /        |
| Exponentiation | ^        |

## Pre-defined variables

| Variable | Value             |
|----------|-------------------|
| PI       | 3.141592653589793 |
| E        | 2.718281828459045 |

## Pre-defined functions

| Function | Description                                            |
|----------|--------------------------------------------------------|
| abs(x)   | Returns the absolute value of the given value x        |
| cos(x)   | Computes the cosine of the angle x given in radians    |
| cot(x)   | Computes the cotangent of the angle x given in radians |
| csc(x)   | Computes the cosecant of the angle x given in radians  |
| lg(x)    | Computes the common logarithm (base 10) of the value x |
| ln(x)    | Computes the natural logarithm (base E) of the value x |
| log2(x)  | Computes the binary logarithm (base 2) of the value x  |
| sec(x)   | Computes the secant of the angle x given in radians    |
| sin(x)   | Computes the sine of the angle x given in radians      |
| sqrt(x)  | Computes the positive square root of the value x       |
| tan(x)   | Computes the tangent of the angle x given in radians   |

## Examples

```kotlin
val mathExpression = MathExpression("(2 + 3) * 4")

println(mathExpression.calulate()) // Returns 20
```

Using variables: a = 6, b = -13, c = 2

```kotlin
val mathExpression = MathExpression("b * b - 4 * a * c").apply {
    setVariable("a", 6.0)
    setVariable("b", -13.0)
    setVariable("c", 2.0)
}

println(mathExpression.calulate()) // Returns 121
```

Using custom function

```kotlin
object DiscriminantFunction : Function {
    override val arity = 3

    override fun call(arguments: List<Double>): Double {
        val (a, b, c) = arguments

        return b * b - 4.0 * a * c
    }
}

val mathExpression = MathExpression("discriminant(2, 5, -7)")

println(mathExpression.calulate()) // Returns 81
```

Using pre-defined variables

```kotlin
val mathExpression = MathExpression("PI")

println(mathExpression.calulate()) // Returns 3.141592653589793
```

```kotlin
val mathExpression = MathExpression("E")

println(mathExpression.calulate()) // Returns 2.718281828459045
```

Using pre-defined functions

```kotlin
val mathExpression = MathExpression("sin(x) ^ 2 + cos(x) ^ 2").apply {
    setVariable("x", 1.23)
}

println(mathExpression.calulate()) // Returns 1
```
