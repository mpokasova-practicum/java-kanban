package manager;

import main.manager.HistoryManager;
import main.manager.InMemoryTaskManager;
import main.manager.Managers;
import main.manager.TaskManager;
import main.model.Task;
import main.model.TaskStatus;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {


    @Test
    public void previousVersion() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task = new Task("name", "description", 1);
        historyManager.add(task);

        task.setStatus(TaskStatus.IN_PROGRESS);

        ArrayList<Task> history = historyManager.getHistory();
        assertNotEquals(history.get(0), task);
    }

    @Test
    public void shouldRemoveTask() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task("name1", "description1", 1);

        historyManager.add(task1);

        historyManager.remove(task1.getId());
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    public void shouldAddToTheEnd() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task("name1", "description1", 1);
        Task task2 = new Task("name2", "description2", 2);

        historyManager.add(task2);
        historyManager.add(task1);

        assertEquals(task1, historyManager.getHistory().get(1));
    }

    @Test
    public void shouldDeleteDuplicates() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task("name1", "description1", 1);
        Task task2 = new Task("name2", "description2", 1);

        historyManager.add(task1);
        historyManager.add(task2);

        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task2, historyManager.getHistory().get(0));
    }

}