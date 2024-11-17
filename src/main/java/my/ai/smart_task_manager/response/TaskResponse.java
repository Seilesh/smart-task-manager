package my.ai.smart_task_manager.response;

import lombok.Getter;
import lombok.Setter;
import my.ai.smart_task_manager.dto.TaskDTO;


import java.util.List;

@Getter
@Setter
public class TaskResponse {
    private List<TaskDTO> tasks;
}
