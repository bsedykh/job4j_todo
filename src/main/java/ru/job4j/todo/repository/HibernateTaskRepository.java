package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

@Repository
@AllArgsConstructor
public class HibernateTaskRepository implements TaskRepository {
    private final SessionFactory sf;

    @Override
    public Collection<Task> findAll() {
        return executeInSession(session -> session
                .createQuery("FROM Task", Task.class)
                .list());
    }

    @Override
    public Collection<Task> findByDone(boolean done) {
        return executeInSession(session -> session
                .createQuery("FROM Task i WHERE i.done = :done", Task.class)
                .setParameter("done", done)
                .list());
    }

    @Override
    public Optional<Task> findById(int id) {
        return executeInSession(session -> session
                .createQuery("FROM Task i WHERE i.id = :id", Task.class)
                .setParameter("id", id)
                .uniqueResultOptional());
    }

    @Override
    public Task save(Task task) {
        executeInTransaction(session -> session.save(task));
        return task;
    }

    @Override
    public boolean update(Task task) {
        return executeInTransaction(session -> session
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
        return executeInTransaction(session -> session
                .createQuery(
                        "DELETE Task WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate()) > 0;
    }

    private <T> T executeInTransaction(Function<Session, T> action) {
        return executeInSession(session -> {
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                var result = action.apply(session);
                tr.commit();
                return result;
            } catch (Exception e) {
                if (tr != null) {
                    tr.rollback();
                }
                throw e;
            }
        });
    }

    private <T> T executeInSession(Function<Session, T> action) {
        try (var session = sf.openSession()) {
            return action.apply(session);
        }
    }
}
