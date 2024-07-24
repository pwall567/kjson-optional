/*
 * @(#) OptTest.kt
 *
 * kjson-optional  Optional property for JSON object
 * Copyright (c) 2023, 2024 Peter Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.kjson.optional

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.expect

class OptTest {

    @Test fun `should create Opt of Int`() {
        val opt = Opt.of(123)
        expect(123) { opt.value }
        assertTrue(opt.isSet)
        assertFalse(opt.isUnset)
    }

    @Test fun `should create Opt of String`() {
        val opt = Opt.of("Hello!")
        expect("Hello!") { opt.value }
        assertTrue(opt.isSet)
        assertFalse(opt.isUnset)
    }

    @Test fun `should create unset Opt`() {
        val opt: Opt<Int> = Opt.unset()
        assertFalse(opt.isSet)
        assertTrue(opt.isUnset)
        assertFailsWith<IllegalStateException> { opt.value }
        assertFailsWith<Opt.NotSetException> { opt.value }
    }

    @Test fun `should create Opt using ofNullable`() {
        val i: Int? = intOrNull(123)
        val opt = Opt.ofNullable(i)
        expect(123) { opt.value }
        assertTrue(opt.isSet)
    }

    @Test fun `should create unset Opt using ofNullable`() {
        val i: Int? = intOrNull(-123)
        val opt: Opt<Int> = Opt.ofNullable(i)
        assertTrue(opt.isUnset)
        assertFailsWith<Opt.NotSetException> { opt.value }
    }

    private fun intOrNull(x: Int): Int? {
        // this contrivance is to avoid compiler warnings
        return if (x >= 0) x else null
    }

    @Test fun `should allow reference to constant UNSET`() {
        val opt = Opt.UNSET
        assertFalse(opt.isSet)
        assertTrue(opt.isUnset)
    }

    @Test fun `should conditionally execute ifSet`() {
        val list = mutableListOf<Int>()
        val opt = Opt.of(123)
        opt.ifSet { list.add(it) }
        expect(1) { list.size }
        expect(123) { list[0] }
    }

    @Test fun `should not execute ifSet when not set`() {
        val list = mutableListOf<Int>()
        val opt = Opt.unset<Int>()
        opt.ifSet { list.add(it) }
        expect(0) { list.size }
    }

    @Test fun `should get value using orNull`() {
        val opt = Opt.of(123)
        expect(123) { opt.orNull }
    }

    @Test fun `should get null value using orNull`() {
        val opt = Opt.unset<Int>()
        assertNull(opt.orNull)
    }

    @Test fun `should get value using orElse`() {
        val opt = Opt.of(123)
        expect(123) { opt.orElse { 456 } }
    }

    @Test fun `should get substitute value using orElse`() {
        val opt = Opt.unset<Int>()
        expect(456) { opt.orElse { 456 } }
    }

    @Test fun `should throw exception using orElse`() {
        val opt = Opt.unset<Int>()
        assertFailsWith<NullPointerException> { opt.orElse { throw NullPointerException() } }
    }

    @Test fun `should convert value using toString`() {
        val opt = Opt.of(123)
        expect("Opt(123)") { opt.toString() }
    }

    @Test fun `should convert null using toString`() {
        val opt: Opt<Int?> = Opt.of(null)
        expect("Opt(null)") { opt.toString() }
    }

    @Test fun `should convert unset value using toString`() {
        val opt = Opt.unset<Int>()
        expect("Opt(UNSET)") { opt.toString() }
    }

    @Test fun `should perform equality check correctly`() {
        val opt1 = Opt.of(123)
        val opt2 = Opt.of(123)
        assertEquals(opt1, opt2)
        assertEquals(opt2, opt1)
        val opt3 = Opt.of(456)
        assertNotEquals(opt1, opt3)
        val opt4 = Opt.unset<Int>()
        assertNotEquals(opt4, opt1)
        val opt5 = Opt.unset<Int>()
        assertEquals(opt4, opt5)
    }

    @Test fun `should create hashCode consistent with equality`() {
        val opt1 = Opt.of(123)
        val opt2 = Opt.of(123)
        assertEquals(opt1.hashCode(), opt2.hashCode())
        val opt4 = Opt.unset<Int>()
        val opt5 = Opt.unset<Int>()
        assertEquals(opt4.hashCode(), opt5.hashCode())
    }

}
