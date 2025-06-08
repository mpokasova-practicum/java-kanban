package model;

import main.manager.InMemoryTaskManager;
import main.model.Subtask;
import main.model.Epic;
import main.model.TaskStatus;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EpicTest {

    @Test
    public void shouldBeEqualEpicsIfEqualIds() {
        Epic epic1 = new Epic("name1", "description1", 1, new ArrayList<>());
        Epic epic2 = new Epic("name1", "description1", 1, new ArrayList<>());
        assertEquals(epic1, epic2);
    }

    @Test
    public void shouldNotBeEqualEpicsIfDifferentIds() {
        Epic epic1 = new Epic("name1", "description1", 1, new ArrayList<>());
        Epic epic2 = new Epic("name2", "description2", 2, new ArrayList<>());
        assertNotEquals(epic1, epic2);
    }

    @Test
    public void checkEpicStatus() {
        InMemoryTaskManager tm = new InMemoryTaskManager();

        Epic epic = new Epic("name", "description", 4, new ArrayList<>());
        tm.createEpic(epic);
        Subtask subtask1 = new Subtask("name1", "description1", 1, epic);
        Subtask subtask2 = new Subtask("name2", "description2", 2, epic);
        tm.createSubtask(subtask1);
        tm.createSubtask(subtask2);

        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        epic.setSubtasks(subtasks);

//         Все подзадачи со статусом NEW.
        subtask1.setStatus(TaskStatus.NEW);
        subtask2.setStatus(TaskStatus.NEW);
        tm.updateSubtask(subtask1);
        tm.updateSubtask(subtask2);

        tm.updateEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus());

//        Все подзадачи со статусом DONE.
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        tm.updateSubtask(subtask1);
        tm.updateSubtask(subtask2);

        tm.updateEpic(epic);
        assertEquals(TaskStatus.DONE, epic.getStatus());

//        Подзадачи со статусами NEW и DONE
        subtask1.setStatus(TaskStatus.NEW);
        subtask2.setStatus(TaskStatus.DONE);
        tm.updateSubtask(subtask1);
        tm.updateSubtask(subtask2);

        tm.updateEpic(epic);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());

//        Подзадачи со статусом IN_PROGRESS.
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateSubtask(subtask1);
        tm.updateSubtask(subtask2);

        tm.updateEpic(epic);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

}