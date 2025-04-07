package test;

import main.model.Task;
import main.model.Epic;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EpicTest {

    @Test
    public void shouldBeEqualEpicsIfEqualIds() {
        Epic epic1 = new Epic("name1", "description1", 1, new ArrayList<>());
        Epic epic2 = new Epic("name2", "description2", 1, new ArrayList<>());
        assertEquals(epic1, epic2);
    }

    @Test
    public void shouldNotBeEqualEpicsIfDifferentIds() {
        Epic epic1 = new Epic("name1", "description1", 1, new ArrayList<>());
        Epic epic2 = new Epic("name2", "description2", 2, new ArrayList<>());
        assertNotEquals(epic1, epic2);
    }

}