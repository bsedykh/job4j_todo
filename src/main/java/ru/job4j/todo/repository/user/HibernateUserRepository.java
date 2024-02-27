package ru.job4j.todo.repository.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HibernateUserRepository implements UserRepository {
    private final CrudRepository crudRepository;

    @Override
    public Collection<User> findAll() {
        return crudRepository.query("FROM User", User.class);
    }

    @Override
    public Optional<User> save(User user) {
        var result = Optional.<User>empty();
        try {
            crudRepository.run(session -> session.save(user));
            result = Optional.of(user);
        } catch (HibernateException e) {
            log.error("Could not save user", e);
        }
        return result;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional("""
                        FROM User u
                        WHERE u.login = :login AND u.password = :password
                        """,
                User.class, Map.of("login", login, "password", password));
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.run("DELETE User WHERE id = :id",
                Map.of("id", id)) > 0;
    }
}
