package manager;

import main.manager.Managers;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ManagersTest {

    @Test
    public void getDefault() {
        assertNotNull(Managers.getDefault());
        assertTrue(Managers.getDefault().getAllTasks().isEmpty());
    }

    @Test
    public void getDefaultHistory() {
        assertNotNull(Managers.getDefaultHistory());
    }

}