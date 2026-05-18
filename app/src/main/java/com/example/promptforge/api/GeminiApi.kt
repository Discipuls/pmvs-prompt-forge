package com.example.promptforge.api

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

data class QuestionItem(val question: String, val options: List<String>)

class OpenRouterApi {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    // Replace with your real OpenRouter key.
    // Better: move this to BuildConfig or secure local config, not hardcoded source.
    private val key = "YOUR_OPENROUTER_API_KEY"

    // Free router model from OpenRouter.
    private val model = "openrouter/free"

    private val url = "https://openrouter.ai/api/v1/chat/completions"

    fun call(promptText: String): String {
        val message = JsonObject().apply {
            addProperty("role", "user")
            addProperty("content", promptText)
        }

        val messages = JsonArray().apply {
            add(message)
        }

        val bodyObj = JsonObject().apply {
            addProperty("model", model)
            add("messages", messages)
        }

        val req = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $key")
            .addHeader("Content-Type", "application/json")
            // Optional OpenRouter attribution headers:
            // .addHeader("HTTP-Referer", "https://your-site.example")
            // .addHeader("X-OpenRouter-Title", "PromptForge")
            .post(gson.toJson(bodyObj).toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(req).execute().use { resp ->
            val txt = resp.body?.string().orEmpty()

            if (!resp.isSuccessful) {
                throw RuntimeException("HTTP ${resp.code}: $txt")
            }

            return try {
                val obj = gson.fromJson(txt, JsonObject::class.java)
                obj.getAsJsonArray("choices")
                    ?.get(0)?.asJsonObject
                    ?.getAsJsonObject("message")
                    ?.get("content")?.asString
                    ?.trim()
                    ?: throw RuntimeException("No content in response: $txt")
            } catch (e: Exception) {
                throw RuntimeException("Failed to parse OpenRouter response: $txt", e)
            }
        }
    }

    fun getQuestions(raw: String, instructions: String): List<QuestionItem> {
        var p = "You help improve AI prompts.\n"
        if (instructions.isNotBlank()) {
            p += "User preferences: $instructions\n"
        }
        p += "User wants to improve this prompt: \"$raw\"\n"
        p += "Return ONLY a JSON array, no other text: " +
                "[{\"question\":\"...\",\"options\":[\"...\",\"...\",\"...\"]}]\n"
        p += "Give 2-3 short questions with 3 options each."

        val result = call(p)

        return try {
            val start = result.indexOf("[")
            val end = result.lastIndexOf("]") + 1
            if (start == -1 || end <= start) return emptyList()

            val json = result.substring(start, end)
            val type = object : TypeToken<List<QuestionItem>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getFinalPrompt(raw: String, answers: Map<Int, String>): String {
        val answersText = buildString {
            for (i in answers.keys.sorted()) {
                append("Q${i + 1}: ${answers[i]}\n")
            }
        }

        val p = "Improve this prompt: \"$raw\"\n" +
                "User answers to clarifying questions:\n$answersText\n" +
                "Return only the improved prompt text, nothing else."

        return call(p)
    }

    fun updateInstructions(old: String, prompt: String, comment: String): String {
        val p = "Current user preferences for AI prompt improvement: \"$old\"\n" +
                "User just reviewed a generated prompt: \"$prompt\"\n" +
                "User feedback: \"$comment\"\n" +
                "Update the preferences based on this feedback. " +
                "Return only the new preferences text, nothing else."

        return call(p)
    }
}