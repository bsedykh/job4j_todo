package ru.job4j.todo.repository.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.CrudRepository;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class HibernateCategoryRepository implements CategoryRepository {
    private final CrudRepository crudRepository;

    @Override
    public List<Category> findAll() {
        return crudRepository.query("""
                FROM Category c
                """, Category.class);
    }

    @Override
    public List<Category> findById(List<Integer> ids) {
        return crudRepository.query("""
                FROM Category c
                WHERE c.id IN :ids
                """, Category.class, Map.of("ids", ids));
    }
}
