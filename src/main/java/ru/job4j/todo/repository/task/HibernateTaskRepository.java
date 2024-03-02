package ru.job4j.todo.repository.task;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.CrudRepository;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateTaskRepository implements TaskRepository {
    private final CrudRepository crudRepository;

    @Override
    public Collection<Task> findAll() {
        return new LinkedHashSet<>(
                crudRepository.query("""
                        FROM Task t
                            JOIN FETCH t.priority
                            JOIN FETCH t.user
                            JOIN FETCH t.categories
                        """, Task.class)
        );
    }

    @Override
    public Collection<Task> findByDone(boolean done) {
        return new LinkedHashSet<>(
                crudRepository.query("""
                        FROM Task t
                            JOIN FETCH t.priority
                            JOIN FETCH t.user
                            JOIN FETCH t.categories
                        WHERE t.done = :done
                        """, Task.class, Map.of("done", done))
        );
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional("""
                FROM Task t
                    JOIN FETCH t.priority
                    JOIN FETCH t.user
                    JOIN FETCH t.categories
                WHERE t.id = :id
                """, Task.class, Map.of("id", id));
    }

    @Override
    public Task save(Task task) {
        crudRepository.run(session -> session.save(task));
        return task;
    }

    @Override
    public boolean update(Task task) {
        return crudRepository.run("""
                UPDATE Task SET
                    description = :description,
                    priority = :priority
                WHERE id = :id
                """, Map.of(
                "description", task.getDescription(),
                "priority", task.getPriority(),
                "id", task.getId())) > 0;
    }

    @Override
    public boolean updateDone(int id, boolean done) {
        return crudRepository.run("""
                UPDATE Task SET
                    done = :done
                WHERE id = :id
                """, Map.of("done", done, "id", id)) > 0;
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.run("DELETE Task WHERE id = :id",
                Map.of("id", id)) > 0;
    }
}
