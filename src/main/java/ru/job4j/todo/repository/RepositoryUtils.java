package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@AllArgsConstructor
public class RepositoryUtils {
    private final SessionFactory sf;

    public <T> T executeInTransaction(Function<Session, T> action) {
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

    public <T> T executeInSession(Function<Session, T> action) {
        try (var session = sf.openSession()) {
            return action.apply(session);
        }
    }
}
