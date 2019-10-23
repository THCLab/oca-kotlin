package com.thehumancolossuslab.odca.kotlin

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object AClassSpec: Spek({
    val calculator by memoized { AClass(3) }
    println("this is the root")

    describe("Calculator") {
        it("3 == 3") {
            println(calculator.x)
            assertEquals(3, calculator.x)
        }
    }
})