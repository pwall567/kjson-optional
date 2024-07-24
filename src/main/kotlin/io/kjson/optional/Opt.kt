/*
 * @(#) Opt.kt
 *
 * kjson-optional  Optional property for kjson
 * Copyright (c) 2023 Peter Wall
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

/**
 * A class to represent an optional property in a JSON object.
 *
 * @author  Peter Wall
 */
class Opt<T> internal constructor(private val content: Any?) {

    val isSet: Boolean
        get() = content !== Unset

    val isUnset: Boolean
        get() = content === Unset

    val value: T
        @Suppress("unchecked_cast")
        get() = if (isSet) content as T else throw NotSetException()

    val orNull: T?
        @Suppress("unchecked_cast")
        get() = if (isSet) content as T else null

    @Suppress("unchecked_cast")
    fun orElse(block: () -> T): T = if (isSet) content as T else block()

    fun ifSet(block: (T) -> Unit) {
        if (isSet)
            @Suppress("unchecked_cast")
            block(content as T)
    }

    override fun toString(): String = "Opt($content)"

    override fun equals(other: Any?): Boolean = this === other || other is Opt<*> && content == other.content

    override fun hashCode(): Int = content.hashCode()

    internal object Unset {

        override fun toString(): String = "UNSET"

    }

    class NotSetException : IllegalStateException("Value not set")

    companion object {

        val UNSET = Opt<Any?>(Unset)

        fun <TT> of(value: TT): Opt<TT> = Opt(value)

        fun <TT: Any> ofNullable(value: TT?): Opt<TT> = if (value == null) unset() else Opt(value)

        @Suppress("unchecked_cast")
        fun <TT> unset(): Opt<TT> = UNSET as Opt<TT>

    }

}
