import java.util.ArrayList;

public class Epic extends Task{
    public ArrayList<Subtask> subtasks;

    public Epic(String name, String description, ArrayList<Subtask> subtasks) {
        super(name, description);
        calculateEpicStatus();
        this.subtasks = subtasks;
    }

    public boolean isInStatus (ArrayList<Subtask> subtasks, TaskStatus status) {
        for (Subtask subtask : subtasks) {
            if (!subtask.getStatus().equals(status)) {
                return false;
            }
        }
        return true;
    }

    public void calculateEpicStatus() {
        if (subtasks.isEmpty() || isInStatus(subtasks, TaskStatus.NEW)) {
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
                "subtasks.size=" + subtasks.size() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
