package ru.job4j.todo.repository.task;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.RepositoryUtils;

import java.util.Collection;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateTaskRepository implements TaskRepository {
    private final RepositoryUtils utils;

    @Override
    public Collection<Task> findAll() {
        return utils.executeInSession(session -> session
                .createQuery("FROM Task", Task.class)
                .list());
    }

    @Override
    public Collection<Task> findByDone(boolean done) {
        return utils.executeInSession(session -> session
                .createQuery("FROM Task i WHERE i.done = :done", Task.class)
                .setParameter("done", done)
                .list());
    }

    @Override
    public Optional<Task> findById(int id) {
        return utils.executeInSession(session -> session
                .createQuery("FROM Task i WHERE i.id = :id", Task.class)
                .setParameter("id", id)
                .uniqueResultOptional());
    }

    @Override
    public Task save(Task task) {
        utils.executeInTransaction(session -> session.save(task));
        return task;
    }

    @Override
    public boolean update(Task task) {
        return utils.executeInTransaction(session -> session
                .createQuery("""
                UPDATE Task SET
                    description = :description,
                    created = :created,
                    done = :done
                WHERE id = :id
                """)
                .setParameter("description", task.getDescription())
                .setParameter("created", task.getCreated())
                .setParameter("done", task.isDone())
                .setParameter("id", task.getId())
                .executeUpdate() > 0);
    }

    @Override
    public boolean deleteById(int id) {
        return utils.executeInTransaction(session -> session
                .createQuery(
                        "DELETE Task WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate()) > 0;
    }
}
