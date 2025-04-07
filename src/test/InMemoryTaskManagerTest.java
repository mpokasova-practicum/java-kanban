package test;

import main.manager.Managers;
import main.manager.TaskManager;
import main.model.Epic;
import main.model.Subtask;
import main.model.Task;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    TaskManager taskManager;

    @BeforeEach
    public void init() {
        taskManager = Managers.getDefault();
    }


    @Test
    public void addAndFindTask() {
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
        Task task1 = new Task(
                "name",
                "description",
                1
        );
        taskManager.createTask(task1);
        Task task2 = taskManager.getTaskById(task1.getId());
        assertEquals(task1.getName(), task2.getName());
        assertEquals(task1.getDescription(), task2.getDescription());
        assertEquals(task1.getId(), task2.getId());
        assertEquals(task1.getStatus(), task2.getStatus());
    }

}