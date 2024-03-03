package ru.job4j.todo.sevice.task;

import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {
    Collection<Task> findAll(User user);

    Collection<Task> findByDone(boolean done, User user);

    Optional<Task> findById(int id, User user);

    Task save(Task task);

    boolean update(Task task);

    boolean updateDone(int id, boolean done);

    boolean deleteById(int id);
}
