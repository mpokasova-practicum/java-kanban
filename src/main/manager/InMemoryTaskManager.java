package main.manager;

import java.util.ArrayList;
import java.util.HashMap;
import main.model.Epic;
import main.model.Task;
import main.model.Subtask;



public class InMemoryTaskManager implements TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public static int globalId;
    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getAllTasks () {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks () {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics () {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllTasks () {
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks () {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.calculateEpicStatus();
        }
    }

    @Override
    public void deleteAllEpics () {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task getTaskById (int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Task getSubtaskById (int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Task getEpicById (int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void createTask (Task task) {
        globalId++;
        task.setId(globalId);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic (Epic epic) {
        globalId++;
        epic.setId(globalId);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask (Subtask subtask) {
        globalId++;
        subtask.setId(globalId);
        subtasks.put(subtask.getId(), subtask);
    }

    @Override
    public void updateTask (Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask (Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        subtask.getEpic().calculateEpicStatus();
    }

    @Override
    public void updateEpic (Epic epic) {
        epics.put(epic.getId(), epic);
        epic.calculateEpicStatus();
    }

    @Override
    public void deleteTaskById (int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubtaskById (int id) {
        Epic epic = subtasks.get(id).getEpic();
        epic.getSubtasks().remove(id);
        epic.calculateEpicStatus();
        subtasks.remove(id);
    }

    @Override
    public void deleteEpicById (int id) {
        for (Subtask subtask : epics.get(id).getSubtasks()) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(id);
    }

    @Override
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