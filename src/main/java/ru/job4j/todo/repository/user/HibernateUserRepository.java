package ru.job4j.todo.repository.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.RepositoryUtils;

import java.util.Collection;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HibernateUserRepository implements UserRepository {
    private final RepositoryUtils utils;

    @Override
    public Collection<User> findAll() {
        return utils.executeInSession(session -> session
                .createQuery("FROM User", User.class)
                .list());
    }

    @Override
    public Optional<User> save(User user) {
        var result = Optional.<User>empty();
        try {
            utils.executeInTransaction(session -> session.save(user));
            result = Optional.of(user);
        } catch (HibernateException e) {
            log.error("Could not save user", e);
        }
        return result;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return utils.executeInSession(session -> session
                .createQuery("""
                        FROM User u
                        WHERE u.login = :login AND u.password = :password
                        """, User.class)
                .setParameter("login", login)
                .setParameter("password", password)
                .uniqueResultOptional());
    }

    @Override
    public boolean deleteById(int id) {
        return utils.executeInTransaction(session -> session
                .createQuery(
                        "DELETE User WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate()) > 0;
    }
}
