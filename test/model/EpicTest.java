package model;

import main.manager.InMemoryTaskManager;
import main.model.Subtask;
import main.model.Epic;
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

//    @Test
//    public void subtaskRemovedFromEpic() {
//        InMemoryTaskManager tm = new InMemoryTaskManager();
//        Epic epic = new Epic("name1", "description1", 1, new ArrayList<>());
//        Subtask subtask = new Subtask("name2", "description2", 2, epic);
//        epic.setSubtasks(new ArrayList<>(List.of(subtask)));
//        tm.createEpic(epic);
//        tm.createSubtask(subtask);
//
//        tm.deleteSubtaskById(subtask.id);
//        assertEquals(0, epic.getSubtasks().size());
//    }
}