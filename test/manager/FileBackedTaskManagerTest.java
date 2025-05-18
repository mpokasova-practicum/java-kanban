package manager;

import main.manager.FileBackedTaskManager;
import main.manager.ManagerSaveException;
import main.model.Epic;
import main.model.Subtask;
import main.model.Task;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    public File tempFile;


    @Test
    public void shouldSaveAndLoadTasksInFile() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);
        Task task1 = new Task("name1", "description1", 1);
        taskManager.createTask(task1);
        Epic epic1 = new Epic("name2", "description2", 2, new ArrayList<>());
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("name3", "description3", 3, epic1);
        taskManager.createSubtask(subtask1);
        try (BufferedReader bufferedReader =  new BufferedReader(new FileReader(tempFile))) {
            ArrayList<String> lines = new ArrayList<>();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                System.out.println(line);
                lines.add(line);
            }
            assertEquals(4, lines.size());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении из файла");
        }
        FileBackedTaskManager testManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(1, testManager.getAllTasks().size());
        assertEquals(1, testManager.getAllEpics().size());
        assertEquals(1, testManager.getAllSubtasks().size());
    }
}
