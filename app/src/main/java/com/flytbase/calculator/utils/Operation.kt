package com.flytbase.calculator.utils

import java.util.*

class Operation {

 @Throws(Exception::class)
 fun evaluate(expression: String): Float {
        val tokens = expression.toCharArray()


        val values: Stack<Float> = Stack<Float>()

        val ops: Stack<Char> = Stack<Char>()
        var i = 0
        while (i < tokens.size) {


            // Current token is a
            // whitespace, skip it
            if (tokens[i] == ' ') {
                i++
                continue
            }

            // Current token is a number,
            // push it to stack for numbers
            if (tokens[i] in '0'..'9'
            ) {
                val sbuf = StringBuffer()

                // There may be more than one
                // digits in number
                while (i < tokens.size && tokens[i] >= '0' && tokens[i] <= '9') sbuf.append(tokens[i++])
                values.push(sbuf.toString().toFloat())

                // right now the i points to
                // the character next to the digit,
                // since the for loop also increases
                // the i, we would skip one
                //  token position; we need to
                // decrease the value of i by 1 to
                // correct the offset.
                i--
            } else if (tokens[i] == '(') ops.push(tokens[i]) else if (tokens[i] == ')') {
                while (ops.peek() !== '(') values.push(
                    applyOp(
                        ops.pop(),
                        values.pop(),
                        values.pop()
                    )
                )
                ops.pop()
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                // While top of 'ops' has same
                // or greater precedence to current
                // token, which is an operator.
                // Apply operator on top of 'ops'
                // to top two elements in values stack
                while (!ops.empty() &&
                    hasPrecedence(
                        tokens[i],
                        ops.peek()
                    )
                ) values.push(
                    applyOp(
                        ops.pop(),
                        values.pop(),
                        values.pop()
                    )
                )

                // Push current token to 'ops'.
                ops.push(tokens[i])
            }
            i++
        }

        // Entire expression has been
        // parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty()) values.push(
            applyOp(
                ops.pop(),
                values.pop(),
                values.pop()
            )
        )

        // Top of 'values' contains
        // result, return it
        return values.pop()
    }

    // Returns true if 'op2' has higher
    // or same precedence as 'op1',
    // otherwise returns false.
    private fun hasPrecedence(
        op1: Char, op2: Char
    ): Boolean {
        if (op2 == '(' || op2 == ')') return false
        return !((op1 == '*' || op1 == '/') &&
                (op2 == '+' || op2 == '-'))
    }

    // A utility method to apply an
    // operator 'op' on operands 'a'
    // and 'b'. Return the result.
    private fun applyOp(
        op: Char,
        b: Float, a: Float
    ): Float? {
        when (op) {
            '+' -> return a + b
            '-' -> return a - b
            '*' -> return a * b
            '/' -> {
                if (b == 0F) throw UnsupportedOperationException(
                    "Cannot divide by zero"
                )
                return a / b
            }
        }
        return 0F
    }

}