package my.ai.smart_task_manager.controller;


import my.ai.smart_task_manager.dto.TaskDTO;
import my.ai.smart_task_manager.service.SmartTimeManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/ai")
public class SmartTimeManagerController {

    @Autowired
    SmartTimeManagerService smartTimeManagerService;

    // Logger for logging messages
    private static final Logger logger = LoggerFactory.getLogger(SmartTimeManagerController.class);

    // Endpoint to load all tasks from the database
    @GetMapping("/loadTasks")
    public List<TaskDTO> loadAllTasks() {
        logger.info("Request received to load tasks");
        List<TaskDTO> tasks = smartTimeManagerService.getAllTasks();
        return tasks;  // Return tasks
    }

    @PostMapping("/generateTasks")
    public List<TaskDTO> generateTasks(@RequestBody String query) {
        logger.info("Received query: {}", query);
        return smartTimeManagerService.generateTasks(query); // Return the generated tasks
    }

    @PostMapping("/saveTask")
    public ResponseEntity<TaskDTO> updateTask(@RequestBody TaskDTO taskDTO) {
        if (taskDTO.getId() != null && taskDTO.getId() > 0) {
            // Update existing task
            TaskDTO updatedTask = smartTimeManagerService.updateTask(taskDTO);
            logger.info("Updated task with ID: {}", updatedTask.getId());
            return ResponseEntity.status(HttpStatus.OK).body(updatedTask); // 200 OK response
        } else {
            // Insert new task (do not include id in TaskDTO when inserting)
            TaskDTO newTask = smartTimeManagerService.saveTask(taskDTO);
            logger.info("Created new task with ID: {}", newTask.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(newTask); // 201 Created response
        }
    }

    // Endpoint to delete a task by its ID
    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        logger.info("Request received to delete task with ID: {}", id);

        boolean isDeleted = smartTimeManagerService.deleteTask(id);

        if (isDeleted) {
            logger.info("Task with ID: {} deleted successfully", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Task deleted successfully");
        } else {
            logger.error("Task with ID: {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }
    }
}

