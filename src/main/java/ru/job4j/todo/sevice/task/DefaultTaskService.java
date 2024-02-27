package ru.job4j.todo.sevice.task;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.task.TaskRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DefaultTaskService implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public Collection<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Collection<Task> findByDone(boolean done) {
        return taskRepository.findByDone(done);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public boolean update(Task task) {
        return taskRepository.update(task);
    }

    @Override
    public boolean updateDone(int id, boolean done) {
        return taskRepository.updateDone(id, done);
    }

    @Override
    public boolean deleteById(int id) {
        return taskRepository.deleteById(id);
    }
}
