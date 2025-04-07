package main.manager;

import main.model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task){
        if (history.size() == 10) {
            history.remove(0);
        }
        Task tempTask = new Task(task.getName(), task.getDescription(), task.getId());
        tempTask.setStatus(task.getStatus());
        history.add(tempTask);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
