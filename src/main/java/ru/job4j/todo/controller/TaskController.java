package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.sevice.category.CategoryService;
import ru.job4j.todo.sevice.priority.PriorityService;
import ru.job4j.todo.sevice.task.TaskService;

import java.util.List;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    private final PriorityService priorityService;

    private final CategoryService categoryService;

    @GetMapping
    public String getAll(Model model, @SessionAttribute User user) {
        model.addAttribute("tasks", taskService.findAll(user));
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getNew(Model model, @SessionAttribute User user) {
        model.addAttribute("tasks", taskService.findByDone(false, user));
        return "tasks/list";
    }

    @GetMapping("/done")
    public String getDone(Model model, @SessionAttribute User user) {
        model.addAttribute("tasks", taskService.findByDone(true, user));
        return "tasks/list";
    }

    @GetMapping("/{id:\\d+}")
    public String get(Model model, @PathVariable int id,
                      @RequestParam(defaultValue = "false") boolean edit,
                      @SessionAttribute User user) {
        var task = taskService.findById(id, user);
        if (task.isEmpty()) {
            model.addAttribute("message",
                    "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("task", task.get());
        if (edit) {
            model.addAttribute("priorities", priorityService.findAll());
        }
        return edit ? "tasks/edit" : "tasks/one";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task,
                         @RequestParam List<Integer> categoryIds,
                         @SessionAttribute User user) {
        task.setCategories(categoryService.findById(categoryIds));
        task.setUser(user);
        taskService.save(task);
        return "redirect:/";
    }

    @GetMapping("/set-done/{id}")
    public String setDone(@PathVariable int id, Model model) {
        if (!taskService.updateDone(id, true)) {
            model.addAttribute("message",
                    "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/";
    }

    @PostMapping("/update")
    public String update(Model model, @ModelAttribute Task task) {
        if (!taskService.update(task)) {
            model.addAttribute("message",
                    "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        if (!taskService.deleteById(id)) {
            model.addAttribute("message",
                    "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/";
    }
}
