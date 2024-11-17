package my.ai.smart_task_manager.repository;


import my.ai.smart_task_manager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long>  {
}
