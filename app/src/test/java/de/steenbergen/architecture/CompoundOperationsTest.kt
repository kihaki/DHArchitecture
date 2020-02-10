package de.steenbergen.architecture

import de.steenbergen.architecture.async.contract.NoInputNoOutputOperation
import de.steenbergen.architecture.async.contract.Operation
import de.steenbergen.architecture.async.contract.plus
import org.junit.Assert
import org.junit.Test

class CompoundOperationsTest {

    @Test
    fun addingTwoOperationsWillCallThemInCorrectOrder() {
        val results = mutableListOf<Int>()
        val op1: NoInputNoOutputOperation = { results.add(1) }
        val op2: NoInputNoOutputOperation = { results.add(2) }

        val combined = op1 + op2

        Assert.assertTrue(results.isEmpty())

        combined(Unit)

        Assert.assertTrue(results.size == 2)
        Assert.assertTrue(results[0] == 1)
        Assert.assertTrue(results[1] == 2)
    }

    @Test
    fun twoOperationsWillPassValue() {
        val op1: Operation<Int, Int> = { input -> input * 2 }
        val op2: Operation<Int, Int> = { input -> input * 3 }

        val combined = op1 + op2

        val result = combined(5)

        Assert.assertEquals(5 * 2 * 3, result)
    }

    @Test
    fun twoOperationsWillPassValueInCorrectOrder() {
        val op1: Operation<String, String> = { input -> input + "b" }
        val op2: Operation<String, String> = { input -> input + "c" }

        Assert.assertEquals("abc", (op1 + op2)("a"))
        Assert.assertEquals("acb", (op2 + op1)("a"))
    }

    @Test
    fun operationsCanBeChainedFurtherThanTwoAndAreChainedCorrectly() {
        val op1: Operation<String, String> = { input -> input + "b" }
        val op2: Operation<String, String> = { input -> input + "c" }

        Assert.assertEquals("ab", op1("a"))
        Assert.assertEquals("ac", op2("a"))
        Assert.assertEquals("abc", (op1 + op2)("a"))
        Assert.assertEquals("acb", (op2 + op1)("a"))
        Assert.assertEquals("abcc", (op1 + op2 + op2)("a"))
        Assert.assertEquals("abcbc", (op1 + op2 + op1 + op2)("a"))
    }
}
