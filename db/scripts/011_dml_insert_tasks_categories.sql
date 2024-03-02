INSERT INTO tasks_categories (task_id, category_id)
SELECT
    t.id,
    c.id
FROM tasks t
    JOIN categories c
        ON c.name = 'Прочее';
