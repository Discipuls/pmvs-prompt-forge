package com.example.promptforge

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.promptforge.dao.PromptDao
import com.example.promptforge.model.Prompt
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Инструментальные тесты базы данных Room.
 * Запускаются на устройстве/эмуляторе (в т.ч. в Firebase Test Lab).
 * Используется in-memory база, чтобы не трогать реальные данные.
 */
@RunWith(AndroidJUnit4::class)
class PromptDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: PromptDao

    @Before
    fun setUp() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.promptDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndGetById() = runBlocking {
        val id = dao.insert(Prompt(raw_prompt = "hello", rating = 3))
        val loaded = dao.getById(id.toInt())
        assertEquals("hello", loaded?.raw_prompt)
        assertEquals(3, loaded?.rating)
    }

    @Test
    fun getAll_returnsPromptsOrderedByTimestampDesc() = runBlocking {
        dao.insert(Prompt(raw_prompt = "first", timestamp = 1L))
        dao.insert(Prompt(raw_prompt = "second", timestamp = 2L))

        val all = dao.getAll().first()

        assertEquals(2, all.size)
        // запрос сортирует по timestamp DESC -> новый сверху
        assertEquals("second", all[0].raw_prompt)
        assertEquals("first", all[1].raw_prompt)
    }

    @Test
    fun delete_removesPrompt() = runBlocking {
        val id = dao.insert(Prompt(raw_prompt = "to delete"))
        val saved = dao.getById(id.toInt())!!

        dao.delete(saved)

        assertNull(dao.getById(id.toInt()))
    }
}
