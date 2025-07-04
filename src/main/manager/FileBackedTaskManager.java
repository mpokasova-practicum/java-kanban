package main.manager;

import main.model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,duration,startTime,endTime,epic" + "\n");
            for (Task task : getAllTasks()) {
                fileWriter.write(toString(task) + "\n");
            }
            for (Subtask task : getAllSubtasks()) {
                fileWriter.write(toString(task) + "\n");
            }
            for (Epic task : getAllEpics()) {
                fileWriter.write(toString(task) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи в файл");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                Task task = fromString(line);
                if (task.getTaskType() == TaskType.TASK) {
                    fileBackedTaskManager.createTask(task);
                } else if (task.getTaskType() == TaskType.SUBTASK) {
                    fileBackedTaskManager.createSubtask((Subtask) task);
                } else {
                    fileBackedTaskManager.createEpic((Epic) task);
                }
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении из файла");
        }
    }

    public String toString(Task task) {
        StringBuilder builder = new StringBuilder();
        builder.append(task.getId()).append(",");
        builder.append(task.getTaskType()).append(",");
        builder.append(task.getName()).append(",");
        builder.append(task.getStatus()).append(",");
        builder.append(task.getDescription()).append(",");
        if (task.getDuration() != null) {
            builder.append(task.getDuration().toMinutes()).append(",");
        } else {
            builder.append("0,");
        }
        builder.append(task.getStartTime()).append(",");
        builder.append(task.getEndTime());
        if (task.getTaskType() == TaskType.SUBTASK) {
            builder.append(",").append(((Subtask) task).getEpic().getId());
        }
        return builder.toString();
    }

    public static Task fromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        TaskType type = TaskType.valueOf(split[1]);
        String name = split[2];
        TaskStatus status = TaskStatus.valueOf(split[3]);
        String description = split[4];
        Duration duration = Duration.ofMinutes(Long.parseLong(split[5]));
        LocalDateTime startTime;
        if (!split[6].equals("null")) {
            startTime = LocalDateTime.parse(split[6]);
        } else {
            startTime = null;
        }

        Task result = null;
        switch (type) {
            case TaskType.TASK:
                result = new Task(name, description, id);
                break;
            case TaskType.EPIC:
                result = new Epic(name, description, id, new ArrayList<>());
                break;
            case TaskType.SUBTASK:
                int epicId = Integer.parseInt((split[8]));
                result = new Subtask(name, description, id, new Epic(null, null, epicId, new ArrayList<>()));
                break;
        }
        result.setStatus(status);
        result.setDuration(duration);
        result.setStartTime(startTime);
        return result;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }
}
