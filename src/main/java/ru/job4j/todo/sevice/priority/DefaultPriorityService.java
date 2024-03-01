package ru.job4j.todo.sevice.priority;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.priority.PriorityRepository;

import java.util.Collection;

@Service
@AllArgsConstructor
public class DefaultPriorityService implements PriorityService {
    private final PriorityRepository priorityRepository;

    @Override
    public Collection<Priority> findAll() {
        return priorityRepository.findAll();
    }
}
