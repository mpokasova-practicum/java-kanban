import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public ArrayList<Task> getAllTasks () {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getAllSubtasks () {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getAllEpics () {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllTasks () {
        tasks.clear();
    }

    public void deleteAllSubtasks () {
        subtasks.clear();
    }

    public void deleteAllEpics () {
        epics.clear();
    }

    public Task getTaskById (int id) {
        if (tasks.get(id) != null) {
            return tasks.get(id);
        }
        else {
            System.out.println("Задачи с таким индентификатор нет.");
            return null;
        }
    }

    public Task getSubtaskById (int id) {
        if (subtasks.get(id) != null) {
            return subtasks.get(id);
        }
        else {
            System.out.println("Подзадачи с таким индентификатор нет.");
            return null;
        }
    }

    public Task getEpicById (int id) {
        if (epics.get(id) != null) {
            return epics.get(id);
        }
        else {
            System.out.println("Эпика с таким индентификатор нет.");
            return null;
        }
    }

    public void createTask (Task task) {
        tasks.put(task.hashCode(), task);
    }

    public void createEpic (Epic epic) {
        epics.put(epic.hashCode(), epic);
    }

    public void createSubtask (Subtask subtask) {
        subtasks.put(subtask.hashCode(), subtask);
    }

    public void updateTask (Task task) {
        tasks.put(task.hashCode(), task);
    }

    public void updateSubtask (Subtask subtask) {
        subtasks.put(subtask.hashCode(), subtask);
        subtask.getEpic().calculateEpicStatus();
    }

    public void updateEpic (Epic epic) {
        tasks.put(epic.hashCode(), epic);
        epic.calculateEpicStatus();
    }

    public void deleteTaskById (int id) {
        tasks.remove(id);
    }

    public void deleteSubtaskById (int id) {
        subtasks.remove(id);
    }

    public void deleteEpicById (int id) {
        epics.remove(id);
    }

    public ArrayList<Subtask> getSubtasksByEpic (Epic epic) {
        ArrayList<Subtask> result = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpic().equals(epic)) {
                result.add(subtask);
            }
        }
        return result;
    }

}
