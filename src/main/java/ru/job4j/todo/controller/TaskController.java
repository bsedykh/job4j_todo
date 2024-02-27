package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.sevice.task.TaskService;

import java.util.List;

@Controller
@RequestMapping("/")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getNew(Model model) {
        model.addAttribute("tasks", taskService.findByDone(false));
        return "tasks/list";
    }

    @GetMapping("/done")
    public String getDone(Model model) {
        model.addAttribute("tasks", taskService.findByDone(true));
        return "tasks/list";
    }

    @GetMapping("/{id:\\d+}")
    public String get(Model model, @PathVariable int id,
                      @RequestParam(defaultValue = "false") String edit) {
        if (!List.of("true", "false").contains(edit)) {
            model.addAttribute("message",
                    "Неверное значение параметра");
            return "errors/404";
        }
        var task = taskService.findById(id);
        if (task.isEmpty()) {
            model.addAttribute("message",
                    "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("task", task.get());
        return "false".equals(edit) ? "tasks/one" : "tasks/edit";
    }

    @GetMapping("/create")
    public String create() {
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, @SessionAttribute User user) {
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
