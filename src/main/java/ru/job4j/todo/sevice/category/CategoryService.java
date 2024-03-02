package ru.job4j.todo.sevice.category;

import ru.job4j.todo.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    List<Category> findById(List<Integer> ids);
}
