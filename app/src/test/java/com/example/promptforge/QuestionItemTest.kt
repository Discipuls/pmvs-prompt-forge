package com.example.promptforge

import com.example.promptforge.api.QuestionItem
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit-тест модели QuestionItem (уточняющий вопрос от API).
 */
class QuestionItemTest {

    @Test
    fun questionItem_holdsQuestionAndOptions() {
        val q = QuestionItem("What tone?", listOf("Formal", "Casual", "Neutral"))
        assertEquals("What tone?", q.question)
        assertEquals(3, q.options.size)
        assertEquals("Formal", q.options[0])
    }

    @Test
    fun questionItem_canHaveEmptyOptions() {
        val q = QuestionItem("Any details?", emptyList())
        assertEquals(0, q.options.size)
    }
}
