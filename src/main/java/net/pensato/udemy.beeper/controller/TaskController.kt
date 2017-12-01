package net.pensato.udemy.beeper.controller

import net.pensato.udemy.beeper.domain.Task
import net.pensato.udemy.beeper.repository.TaskRepository
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(private val taskRepository: TaskRepository) {


    @GetMapping
    fun getTasks(): List<Task> = taskRepository.findAll().toList()

    @PostMapping
    fun addTask(@RequestBody task: Task): Task = taskRepository.save(task)

    @PutMapping("/{id}")
    fun editTask(@PathVariable id: Long, @RequestBody task: Task) {
        val existingTask = taskRepository.findOne(id)
        Assert.notNull(existingTask, "Task not found")
        existingTask.description = task.description
        taskRepository.save(existingTask)
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long) {
        taskRepository.delete(id)
    }
}
