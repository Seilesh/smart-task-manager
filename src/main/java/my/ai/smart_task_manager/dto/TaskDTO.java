package my.ai.smart_task_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String deadline;
    private String formattedDeadLine;
    private String dailyTimeCommitment;
    private boolean completed;
}
