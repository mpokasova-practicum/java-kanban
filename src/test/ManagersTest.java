package test;

import main.manager.Managers;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ManagersTest {

    @Test
    public void getDefault() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    public void getDefaultHistory() {
        assertNotNull(Managers.getDefaultHistory());
    }

}