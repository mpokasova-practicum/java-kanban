package main.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.manager.TaskManager;
import main.model.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler {
    public TasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    public void handle(HttpExchange exchange) throws IOException {
        BaseHttpHandler.Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        try {
            switch (endpoint) {
                case GET_ALL -> handleGetAll(exchange);
                case GET_ID -> handleGetId(exchange);
                case POST -> handlePost(exchange);
                case DELETE -> handleDelete(exchange);
                default -> sendText(exchange, "{\"error\":\"Метод не поддерживается\"}", 405);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }

    public void handleGetAll(HttpExchange exchange) throws IOException {
        try {
            sendText(exchange, gson.toJson(taskManager.getAllTasks()), 200);
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }

    public void handleGetId(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                sendNotFound(exchange, "{\"error\":\"Некорректно указан номер задачи\"}");
                return;
            }
            int taskId = taskIdOpt.get();

            Task task = taskManager.getTaskById(taskId);
            if (task == null) {
                sendNotFound(exchange, "{\"error\":\"Задача не найдена\"}");
            } else {
                sendText(exchange, gson.toJson(task), 200);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }

    public void handlePost(HttpExchange exchange) throws IOException {
        try {
            String taskRequest = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(taskRequest, Task.class);

            if (taskManager.checkIfAllTasksIntersect(task)) {
                sendHasInteractions(exchange,
                        "{\"error\":\"Данная задача пересекается по времени с одной из текущих задач\"}");
            } else if (taskManager.getTaskById(task.getId()) == null) {
                taskManager.createTask(task);
                exchange.sendResponseHeaders(201, 0);
                exchange.close();
            } else {
                taskManager.updateTask(task);
                exchange.sendResponseHeaders(201, 0);
                exchange.close();
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }

    public void handleDelete(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                sendNotFound(exchange, "{\"error\":\"Некорректно указан номер задачи\"}");
                return;
            }
            int taskId = taskIdOpt.get();

            Task task = taskManager.getTaskById(taskId);
            if (task == null) {
                sendNotFound(exchange, "{\"error\":\"Задача не найдена\"}");
            } else {
                taskManager.deleteTaskById(taskId);
                sendText(exchange, "{\"message\":\"Задача успешно удалена\"}", 200);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }
}
