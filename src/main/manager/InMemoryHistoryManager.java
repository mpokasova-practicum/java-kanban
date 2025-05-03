package main.manager;

import main.model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    HandMadeLinkedHashMap history = new HandMadeLinkedHashMap();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.nodeMap.containsKey(task.getId())) {
                history.removeNode(history.nodeMap.get(task.getId()));
            }
            history.linkLast(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public void remove(int id) {
        history.removeNode(history.nodeMap.get(id));
    }

    public static class HandMadeLinkedHashMap {
        public HashMap<Integer, Node> nodeMap = new HashMap<>();
        public Node head;
        public Node tail;

        public void linkLast(Task task) {
            Node oldTail = tail;
            Node newNode = new Node(oldTail, task, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
            nodeMap.put(task.getId(), newNode);
        }

        public ArrayList<Task> getTasks() {
            ArrayList<Task> result = new ArrayList<>();
            Node curNode = head;
            while (curNode != null) {
                result.add(curNode.task);
                curNode = curNode.next;
            }
            return result;
        }

        public void removeNode(Node node) {
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                head = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                tail = node.prev;
            }
            nodeMap.remove(node.task.getId());
        }
    }

    static class Node {
        public Task task;
        public Node next;
        public Node prev;

        public Node(Node prev, Task task, Node next) {
            this.task = task;
            this.next = prev;
            this.prev = next;
        }
    }
}
