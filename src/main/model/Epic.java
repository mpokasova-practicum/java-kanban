package main.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Subtask> subtasks;
    public LocalDateTime endTime;

    public Epic(String name, String description, int id, ArrayList<Subtask> subtasks) {
        super(name, description, id);
        calculateEpicStatus();
        this.subtasks = subtasks;
    }

    public ArrayList<Subtask> getSubtasks() {
        return this.subtasks;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public boolean isInStatus(ArrayList<Subtask> subtasks, TaskStatus status) {
        for (Subtask subtask : subtasks) {
            if (!subtask.getStatus().equals(status)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Duration getDuration() {
        duration = Duration.ZERO;
        subtasks.forEach(
                subtask -> duration = duration.plus(subtask.getDuration())
        );
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        if (!subtasks.isEmpty()) {
            startTime = subtasks.get(0).getStartTime();
            subtasks.forEach(
                    subtask -> {
                        if (startTime.isAfter(subtask.getStartTime())) {
                            startTime = subtask.getStartTime();
                        }
                    });
            return startTime;
        } else {
            return null;
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        if (!subtasks.isEmpty()) {
            endTime = subtasks.get(0).getEndTime();
            subtasks.forEach(
                    subtask -> {
                        if (endTime.isBefore(subtask.getEndTime())) {
                            endTime = subtask.getEndTime();
                        }
                    }
            );
            return endTime;
        } else {
            return null;
        }
    }

    public void calculateEpicStatus() {
        if (subtasks == null || subtasks.isEmpty() || isInStatus(subtasks, TaskStatus.NEW)) {
            status = TaskStatus.NEW;
        } else if (isInStatus(subtasks, TaskStatus.DONE)) {
            status = TaskStatus.DONE;
        } else {
            status = TaskStatus.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

}