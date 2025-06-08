package manager;

import main.manager.ManagerValidateException;
import main.manager.Managers;
import main.manager.TaskManager;
import main.model.Epic;
import main.model.Subtask;
import main.model.Task;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {


    @Test
    public void addAndFindTask() {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task(
                "name",
                "description",
                1
        );
        taskManager.createTask(task);
        assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    public void addAndFindEpic() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic(
                "name",
                "description",
                1,
                new ArrayList<>()
        );
        taskManager.createEpic(epic);
        assertEquals(epic, taskManager.getEpicById(epic.getId()));
    }

    @Test
    public void addAndFindSubtask() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic(
                "name",
                "description",
                1,
                new ArrayList<>()
        );
        Subtask subtask = new Subtask(
                "name",
                "description",
                2,
                epic
        );
        taskManager.createSubtask(subtask);
        assertEquals(subtask, taskManager.getSubtaskById(subtask.getId()));
    }

    @Test
    public void shouldBeTheSameAfterAdding() {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task(
                "name",
                "description",
                1
        );
        taskManager.createTask(task1);
        Task task2 = taskManager.getTaskById(task1.getId());
        assertEquals(task1, task2);
    }

    @Test
    public void testUniqueIdGeneration() {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("name1", "description1", 1);
        Task task2 = new Task("name2", "description2", 2);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertEquals(task1.getId() + 1, task2.getId());
    }

    @Test
    public void prioritizeCheck() {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("name1", "description1", 1);
        Task task2 = new Task("name2", "description2", 2);
        task1.setStartTime(LocalDateTime.of(2025, Month.APRIL, 10, 9, 0, 0));
        task1.setDuration(Duration.ofMinutes(60));
        task2.setStartTime(LocalDateTime.of(2025, Month.APRIL, 10, 9, 30, 0));
        task2.setDuration(Duration.ofMinutes(60));
        taskManager.createTask(task1);
        assertThrows(ManagerValidateException.class, () -> taskManager.createTask(task2));
    }

}