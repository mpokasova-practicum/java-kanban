package main.manager;

import main.model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    public static final int MAX_HISTORY_SIZE = 10;
    ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task){
        if (task != null) {
            if (history.size() == MAX_HISTORY_SIZE) {
                history.remove(0);
            }
            Task tempTask = new Task(task.getName(), task.getDescription(), task.getId());
//            tempTask.setStatus(task.getStatus());
//            А почему статус устанавливать не нужно? В Task я делала конструктор с 3 параметрами (name, description и id),
//            а статус он сам проставляет NEW. Тогда получится, что статус у tempTask будет NEW, а у task может быть другой.
//            Или нужно конструктор тогда переделать, добавив статус как параметр?
            history.add(tempTask);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
