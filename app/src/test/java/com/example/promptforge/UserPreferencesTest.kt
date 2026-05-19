package com.example.promptforge

import com.example.promptforge.model.UserPreferences
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit-тесты модели UserPreferences.
 */
class UserPreferencesTest {

    @Test
    fun defaultPreferences_haveIdOneAndEmptyInstructions() {
        val prefs = UserPreferences()
        assertEquals(1, prefs.id)
        assertEquals("", prefs.custom_instructions)
    }

    @Test
    fun preferences_storeInstructions() {
        val prefs = UserPreferences(1, "always be concise")
        assertEquals("always be concise", prefs.custom_instructions)
    }

    @Test
    fun copy_updatesInstructions() {
        val prefs = UserPreferences()
        val updated = prefs.copy(custom_instructions = "use formal tone")
        assertEquals("use formal tone", updated.custom_instructions)
        assertEquals("", prefs.custom_instructions)
    }
}
