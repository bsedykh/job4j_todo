package ru.job4j.todo.repository.task;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {
    Collection<Task> findAll();

    Collection<Task> findByDone(boolean done);

    Optional<Task> findById(int id);

    Task save(Task task);

    boolean update(Task task);

    boolean updateDone(int id, boolean done);

    boolean deleteById(int id);
}
