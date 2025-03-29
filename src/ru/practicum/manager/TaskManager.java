package ru.practicum.manager;

import java.util.ArrayList;
import java.util.HashMap;
import  ru.practicum.model.Epic;
import  ru.practicum.model.Task;
import  ru.practicum.model.Subtask;



public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public static int globalId;

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
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.calculateEpicStatus();
        }
    }

    public void deleteAllEpics () {
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById (int id) {
        return tasks.get(id);
    }

    public Task getSubtaskById (int id) {
        return subtasks.get(id);
    }

    public Task getEpicById (int id) {
        return epics.get(id);
    }

    public void createTask (Task task) {
        globalId++;
        task.setId(globalId);
        tasks.put(task.getId(), task);
    }

    public void createEpic (Epic epic) {
        globalId++;
        epic.setId(globalId);
        epics.put(epic.getId(), epic);
    }

    public void createSubtask (Subtask subtask) {
        globalId++;
        subtask.setId(globalId);
        subtasks.put(subtask.getId(), subtask);
    }

    public void updateTask (Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask (Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        subtask.getEpic().calculateEpicStatus();
    }

    public void updateEpic (Epic epic) {
        tasks.put(epic.getId(), epic);
        epic.calculateEpicStatus();
    }

    public void deleteTaskById (int id) {
        tasks.remove(id);
    }

    public void deleteSubtaskById (int id) {
        Epic epic = subtasks.get(id).getEpic();
        epic.getSubtasks().remove(id);
        epic.calculateEpicStatus();
        subtasks.remove(id);
    }

    public void deleteEpicById (int id) {
        for (Subtask subtask : epics.get(id).getSubtasks()) {
            subtasks.remove(subtask.getId());
        }
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