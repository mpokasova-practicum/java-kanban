package test;

import main.model.Task;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TaskTest {

    @Test
    public void shouldBeEqualTasksIfEqualIds() {
        Task task1 = new Task("name1", "description1", 1);
        Task task2 = new Task("name1", "description1", 1);
        assertEquals(task1, task2);
    }

    @Test
    public void shouldNotBeEqualTasksIfDifferentIds() {
        Task task1 = new Task("name1", "description1", 1);
        Task task2 = new Task("name1", "description1", 2);
        assertNotEquals(task1, task2);
    }
}
