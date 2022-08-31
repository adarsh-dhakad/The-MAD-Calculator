package com.flytbase.calculator.viewmodel

import com.flytbase.calculator.utils.Operation
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class MainActivityViewModelTest{
   private lateinit var operation: Operation

    @Before
    fun setup(){
        operation = Operation()
    }

    @Test
    fun `test evaluate function`(){
       val value = operation.evaluate("2+3*2")
        assertThat(value).isEqualTo(8)
    }

    @Test
    fun `test 2 evaluate function`(){
        val value = operation.evaluate("2+3*2+9/9/1+1")
        assertThat(value).isEqualTo(10)
    }
}
