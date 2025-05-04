package model;

import main.model.Epic;
import main.model.Subtask;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SubtaskTest {

    @Test
    public void shouldBeSubtaskEpicsIfEqualIds() {
        Epic epic = new Epic("name1", "description1", 1, new ArrayList<>());
        Subtask subtask1 = new Subtask("name3", "description3", 2, epic);
        Subtask subtask2 = new Subtask("name3", "description3", 2, epic);
        assertEquals(subtask1, subtask2);
    }

    @Test
    public void shouldNotBeSubtaskEpicsIfDifferentIds() {
        Epic epic = new Epic("name1", "description1", 1, new ArrayList<>());
        Subtask subtask1 = new Subtask("name3", "description3", 2, epic);
        Subtask subtask2 = new Subtask("name3", "description3", 3, epic);
        assertNotEquals(subtask1, subtask2);
    }

}