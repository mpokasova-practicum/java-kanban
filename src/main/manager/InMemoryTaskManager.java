package main.manager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import main.model.Epic;
import main.model.Task;
import main.model.Subtask;



public class InMemoryTaskManager implements TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public static int globalId;
    HistoryManager historyManager = Managers.getDefaultHistory();
    TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime,
                    Comparator.nullsLast(Comparator.naturalOrder()))
    );

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        prioritizedTasks.removeIf(task -> task instanceof Task);
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        prioritizedTasks.removeIf(task -> task instanceof Subtask);
        epics.values().forEach(
                epic -> {
                    epic.getSubtasks().clear();
                    epic.calculateEpicStatus();
                }
        );
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
        prioritizedTasks.removeIf(task -> task instanceof Subtask);
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Task getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Task getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void createTask(Task task) {
        validateTask(task);
        globalId++;
        task.setId(globalId);
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        globalId++;
        epic.setId(globalId);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        validateTask(subtask);
        globalId++;
        subtask.setId(globalId);
        subtasks.put(subtask.getId(), subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
    }

    @Override
    public void updateTask(Task task) {
        validateTask(task);
        tasks.put(task.getId(), task);
        prioritizedTasks.remove(task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        validateTask(subtask);
        subtasks.put(subtask.getId(), subtask);
        subtask.getEpic().calculateEpicStatus();
        prioritizedTasks.remove(subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        epic.calculateEpicStatus();
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        prioritizedTasks.remove(getTaskById(id));
    }

    @Override
    public void deleteSubtaskById(int id) {
        Epic epic = subtasks.get(id).getEpic();
        epic.getSubtasks().remove(id);
        epic.calculateEpicStatus();
        subtasks.remove(id);
        prioritizedTasks.remove(getSubtaskById(id));
    }

    @Override
    public void deleteEpicById(int id) {
        epics.get(id).getSubtasks().forEach(subtask -> subtasks.remove(subtask.getId()));
        epics.remove(id);
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpic(Epic epic) {
        ArrayList<Subtask> result = new ArrayList<>();
        subtasks.values().forEach(
                subtask -> {
                    if (subtask.getEpic().equals(epic)) {
                        result.add(subtask);
                    }
                }
        );
        return result;
    }

    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public boolean checkIfTwoTasksIntersect(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }
        return !(task1.getEndTime().isBefore(task2.getStartTime())
                || task2.getEndTime().isBefore(task1.getStartTime()));
    }

    public boolean checkIfAllTasksIntersect(Task task1) {
        return prioritizedTasks.stream().anyMatch(task2 -> checkIfTwoTasksIntersect(task1, task2));
    }

    public void validateTask(Task task) {
        if (checkIfAllTasksIntersect((task))) {
            throw new ManagerValidateException("Данная задача пересекается по времени с одной из текущих задач");
        }
    }

}