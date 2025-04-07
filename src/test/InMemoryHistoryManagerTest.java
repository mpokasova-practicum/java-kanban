package test;

import main.manager.HistoryManager;
import main.manager.Managers;
import main.manager.TaskManager;
import main.model.Task;
import main.model.TaskStatus;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    TaskManager taskManager;
    HistoryManager historyManager;

    @BeforeEach
    public void init() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void previousVersion() {
        Task task = new Task("name", "description", 1);
        taskManager.createTask(task);
        taskManager.getTaskById(task.getId());
        task.setStatus(TaskStatus.IN_PROGRESS);
        ArrayList<Task> history = historyManager.getHistory();
        assertNotEquals(history.get(0), task);
    }

}