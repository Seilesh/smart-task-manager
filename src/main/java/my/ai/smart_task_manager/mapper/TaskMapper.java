package my.ai.smart_task_manager.mapper;


import my.ai.smart_task_manager.controller.SmartTimeManagerController;
import my.ai.smart_task_manager.dto.TaskDTO;
import my.ai.smart_task_manager.model.Task;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TaskMapper {
    // Logger for logging messages
    private static final Logger logger = LoggerFactory.getLogger(TaskMapper.class);

    public TaskDTO toDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setDeadline(getFormattedDeadline(task.getDeadline()));
        dto.setDailyTimeCommitment(task.getDailyTimeCommitment());
        dto.setCompleted(task.isCompleted());
        return dto;
    }

    public Task toEntity(TaskDTO dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDeadline(setFormattedDeadline(dto.getDeadline()));
        task.setDailyTimeCommitment(dto.getDailyTimeCommitment());
        task.setCompleted(dto.isCompleted());
        return task;
    }

    private String getFormattedDeadline(Calendar deadline) {
        if (deadline == null) {
            return null;  // or return a default date like "1970-01-01"
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(deadline.getTime());
    }

    private Calendar setFormattedDeadline(String deadline) {
        if (deadline == null) {
            return null;  // or return a default date like "1970-01-01"
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(deadline);
        } catch (ParseException e) {
            logger.error("Invalid date format: " + e.getMessage());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
