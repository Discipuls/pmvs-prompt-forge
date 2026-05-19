package com.example.promptforge

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.promptforge.model.Prompt
import com.example.promptforge.ui.PromptItem
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI-тесты Compose-компонента PromptItem (карточка промпта в истории).
 * Запускаются на устройстве/эмуляторе (в т.ч. в Firebase Test Lab).
 */
@RunWith(AndroidJUnit4::class)
class PromptItemUiTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun showsRawPromptText() {
        rule.setContent {
            MaterialTheme {
                PromptItem(
                    p = Prompt(id = 1, raw_prompt = "my test prompt", rating = 4),
                    onClick = {},
                    onDelete = {}
                )
            }
        }
        rule.onNodeWithText("my test prompt").assertIsDisplayed()
    }

    @Test
    fun showsRatingWhenAboveZero() {
        rule.setContent {
            MaterialTheme {
                PromptItem(
                    p = Prompt(raw_prompt = "x", rating = 5),
                    onClick = {},
                    onDelete = {}
                )
            }
        }
        rule.onNodeWithText("Rating: 5/5").assertIsDisplayed()
    }

    @Test
    fun clickingCard_triggersOnClick() {
        var clicked = false
        rule.setContent {
            MaterialTheme {
                PromptItem(
                    p = Prompt(raw_prompt = "click me"),
                    onClick = { clicked = true },
                    onDelete = {}
                )
            }
        }
        rule.onNodeWithText("click me").performClick()
        assertTrue(clicked)
    }
}
