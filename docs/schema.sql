-- Схема базы данных приложения PromptForge
-- СУБД: SQLite (через библиотеку Room)
-- Имя файла базы: promptforge.db

-- Таблица истории промптов
CREATE TABLE prompts (
    id              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  -- идентификатор
    raw_prompt      TEXT    NOT NULL DEFAULT '',                 -- исходный текст промпта
    improved_prompt TEXT    NOT NULL DEFAULT '',                 -- улучшенный текст промпта
    rating          INTEGER NOT NULL DEFAULT 0,                  -- оценка пользователя (0–5)
    timestamp       INTEGER NOT NULL DEFAULT 0                   -- дата создания (Unix-время, мс)
);

-- Таблица пользовательских предпочтений (профиль обучения).
-- Всегда содержит одну запись с id = 1.
CREATE TABLE user_preferences (
    id                  INTEGER NOT NULL PRIMARY KEY,            -- всегда = 1
    custom_instructions TEXT    NOT NULL DEFAULT ''              -- текущие инструкции для ИИ
);

-- Начальная запись предпочтений
INSERT INTO user_preferences (id, custom_instructions) VALUES (1, '');
