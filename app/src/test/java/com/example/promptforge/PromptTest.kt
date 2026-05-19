package com.example.promptforge

import com.example.promptforge.model.Prompt
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit-тесты (запускаются на JVM, без устройства/эмулятора).
 * Проверяют модель данных Prompt.
 */
class PromptTest {

    @Test
    fun newPrompt_hasDefaultValues() {
        val p = Prompt()
        assertEquals(0, p.id)
        assertEquals("", p.raw_prompt)
        assertEquals("", p.improved_prompt)
        assertEquals(0, p.rating)
        assertEquals(0L, p.timestamp)
    }

    @Test
    fun prompt_keepsGivenValues() {
        val p = Prompt(
            id = 5,
            raw_prompt = "write email",
            improved_prompt = "Please write a formal email",
            rating = 4,
            timestamp = 1000L
        )
        assertEquals(5, p.id)
        assertEquals("write email", p.raw_prompt)
        assertEquals("Please write a formal email", p.improved_prompt)
        assertEquals(4, p.rating)
        assertEquals(1000L, p.timestamp)
    }

    @Test
    fun copy_changesOnlyOneField() {
        val original = Prompt(raw_prompt = "a", rating = 1)
        val updated = original.copy(rating = 5)

        assertEquals("a", updated.raw_prompt)
        assertEquals(5, updated.rating)
        // оригинал не изменился
        assertEquals(1, original.rating)
    }

    @Test
    fun equalPrompts_areEqual() {
        val a = Prompt(id = 1, raw_prompt = "x", rating = 3, timestamp = 10L)
        val b = Prompt(id = 1, raw_prompt = "x", rating = 3, timestamp = 10L)
        assertEquals(a, b)
    }
}
