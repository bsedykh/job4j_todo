package ru.job4j.todo.sevice.task;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.task.TaskRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Optional;
import java.util.TimeZone;

@Service
@AllArgsConstructor
public class DefaultTaskService implements TaskService {
    private static final String TIME_ZONE = "UTC";

    private final TaskRepository taskRepository;

    @Override
    public Collection<Task> findAll(User user) {
        var result = taskRepository.findAll();
        result.forEach(task -> setDatesToUserTimeZone(task, user));
        return result;
    }

    @Override
    public Collection<Task> findByDone(boolean done, User user) {
        var result = taskRepository.findByDone(done);
        result.forEach(task -> setDatesToUserTimeZone(task, user));
        return result;
    }

    @Override
    public Optional<Task> findById(int id, User user) {
        var result = taskRepository.findById(id);
        result.ifPresent(task -> setDatesToUserTimeZone(task, user));
        return result;
    }

    @Override
    public Task save(Task task) {
        setDatesToServiceTimeZone(task);
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

    private void setDatesToUserTimeZone(Task task, User user) {
        task.setCreated(convertTimeZone(
                task.getCreated(),
                ZoneId.of(TIME_ZONE),
                ZoneId.of(getUserTimeZone(user))
        ));
    }

    private void setDatesToServiceTimeZone(Task task) {
        task.setCreated(convertTimeZone(
                task.getCreated(),
                ZoneId.of(getUserTimeZone(task.getUser())),
                ZoneId.of(TIME_ZONE)
        ));
    }

    private static String getUserTimeZone(User user) {
        return user.getTimezone() != null
                ? user.getTimezone()
                : TimeZone.getDefault().getID();
    }

    private static LocalDateTime convertTimeZone(LocalDateTime dateTime, ZoneId from, ZoneId to) {
        return dateTime.atZone(from).withZoneSameInstant(to).toLocalDateTime();
    }
}
