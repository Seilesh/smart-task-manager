package my.ai.smart_task_manager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.ai.smart_task_manager.dto.TaskDTO;
import my.ai.smart_task_manager.exception.TaskNotFoundException;
import my.ai.smart_task_manager.mapper.TaskMapper;
import my.ai.smart_task_manager.model.Task;
import my.ai.smart_task_manager.repository.TaskRepository;
import my.ai.smart_task_manager.response.TaskResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SmartTimeManagerService {

    private static final Logger logger = LoggerFactory.getLogger(SmartTimeManagerService.class);

    @Autowired
    OllamaChatModel ollamaChatModel;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    TaskMapper taskMapper;

    // Method to retrieve all tasks from the database and map them to DTOs
    public List<TaskDTO> getAllTasks() {
        logger.info("Fetching all tasks from the database...");

        // Fetch all tasks from the database
        List<Task> tasks = taskRepository.findAll();

        // Convert tasks to TaskDTOs
        return tasks.stream()
                .map(taskMapper::toDTO)  // Map each task to TaskDTO
                .collect(Collectors.toList());
    }

    // Get text in the structured format
    public List<TaskDTO> generateTasks(String text) {

        // Refine the prompt template to make it more specific about time commitment in hours
        String template = """
            Create a detailed study schedule in JSON format for the following subjects and deadlines:
            
            {tasks}
            
            Each task should be formatted as a JSON object with the following keys:
            - title: A concise title for the task
            - description: A brief description of the task
            - dailyTimeCommitment: Estimated time to spend on the task each day, in hours (e.g., 1.5 hours, 2 hours, etc.)
            - deadline: The due date of the task in YYYY-MM-DD format.
            
            Ensure that the time commitment is always specified in hours. 
            If no time commitment is provided, assume 1 hour per task by default.
            """;

        // Fill the task-specific content into the template
        PromptTemplate promptTemplate = new PromptTemplate(template);
        promptTemplate.add("tasks", text);

        // Generate the prompt
        Prompt prompt = promptTemplate.create();

        // Get the response from the model
        ChatResponse chatResponse = ollamaChatModel.call(prompt);
        List<Generation> generations = chatResponse.getResults();
        List<TaskDTO> tasks = new ArrayList<>();  // Use ArrayList for better mutability

        // Process the generated responses
        for (Generation generation : generations) {
            AssistantMessage assistantMessage = generation.getOutput();
            String jsonString = assistantMessage.getContent();
            ObjectMapper mapper = new ObjectMapper();
            TaskResponse taskResponse = null;

            try {
                // Parse the JSON response into a TaskResponse object
                taskResponse = mapper.readValue(jsonString, TaskResponse.class);

                // Ensure taskResponse is not null and contains tasks
                if (taskResponse != null && taskResponse.getTasks() != null) {
                    tasks.addAll(taskResponse.getTasks());
                }
            } catch (JsonProcessingException e) {
                // Log the error and continue processing other generations
                logger.error("Error parsing task generation JSON: " + e.getMessage());
                continue;
            }
        }

        // Process tasks (optional, depending on what you need to do with the tasks)
        for (TaskDTO task : tasks) {
            // Ensure dailyTimeCommitment is in hours (if it's missing, assume 1 hour)
            if (task.getDailyTimeCommitment() == null || task.getDailyTimeCommitment().isEmpty()) {
                task.setDailyTimeCommitment("1");  // Default to 1 hour if not provided
            } else {
                try {
                    // Convert to a double to handle decimal hours
                    Double timeCommitment = Double.parseDouble(task.getDailyTimeCommitment());
                    task.setDailyTimeCommitment(timeCommitment.toString());
                } catch (NumberFormatException e) {
                    // Handle invalid number formats (optional logging or default action)
                    task.setDailyTimeCommitment("1");  // Default to 1 hour if invalid
                    logger.warn("Invalid time commitment value for task: " + task.getTitle());
                }
            }

            // Ensure the deadline is in the expected format
            if (task.getDeadline() == null || task.getDeadline().isEmpty()) {
                task.setDeadline("2024-12-31");  // Default to a valid deadline (adjust as needed)
            }

            // Optionally, save or process each task here
            logger.info("Task details - Title: {}, Description: {}, Time Commitment: {} hours, Deadline: {}",
                    task.getTitle(), task.getDescription(), task.getDailyTimeCommitment(), task.getDeadline());
        }

        return tasks;
    }

    public TaskDTO saveTask(TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO); // Convert DTO to Entity
        return taskMapper.toDTO(taskRepository.save(task)); // Save the task to the database (insert)
    }

    // Update an existing task
    public TaskDTO updateTask(TaskDTO taskDTO) {
        // Check if the task exists in the database
        if (taskDTO.getId() == null || !taskRepository.existsById(taskDTO.getId())) {
            throw new TaskNotFoundException("Task not found for update.");
        }

        // Convert DTO to entity
        Task task = taskMapper.toEntity(taskDTO);

        // Update the task in the database
        task = taskRepository.save(task);  // The save method also works for updates, as it will update if the ID exists

        return taskMapper.toDTO(task);  // Convert the updated entity back to DTO
    }

    // Method to delete a task by its ID
    public boolean deleteTask(Long id) {
        // Check if the task exists
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            // If task exists, delete it
            taskRepository.deleteById(id);
            return true;
        }

        // If task does not exist, return false
        return false;
    }
}