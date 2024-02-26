package ru.job4j.todo.repository.user;

import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Collection<User> findAll();

    Optional<User> save(User user);

    Optional<User> findByLoginAndPassword(String login, String password);

    boolean deleteById(int id);
}
