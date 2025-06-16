package main.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.manager.TaskManager;
import main.model.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubtasksHandler extends BaseHttpHandler {
    public SubtasksHandler(TaskManager taskManager, Gson gson) {
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
            sendText(exchange, gson.toJson(taskManager.getAllSubtasks()), 200);
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }

    public void handleGetId(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                sendNotFound(exchange, "{\"error\":\"Некорректно указан номер подзадачи\"}");
                return;
            }
            int subtaskId = taskIdOpt.get();

            Subtask subtask = (Subtask) taskManager.getSubtaskById(subtaskId);
            if (subtask == null) {
                sendNotFound(exchange, "{\"error\":\"Подзадача не найдена\"}");
            } else {
                sendText(exchange, gson.toJson(subtask), 200);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }

    public void handlePost(HttpExchange exchange) throws IOException {
        try {
            String taskRequest = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(taskRequest, Subtask.class);

            if (taskManager.checkIfAllTasksIntersect(subtask)) {
                sendHasInteractions(exchange,
                        "{\"error\":\"Данная задача пересекается по времени с одной из текущих задач\"}");
            } else if (taskManager.getSubtaskById(subtask.getId()) == null) {
                taskManager.createSubtask(subtask);
                exchange.sendResponseHeaders(201, 0);
                exchange.close();
            } else {
                taskManager.updateSubtask(subtask);
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
            int subtaskId = taskIdOpt.get();

            Subtask subtask = (Subtask) taskManager.getSubtaskById(subtaskId);
            if (subtask == null) {
                sendNotFound(exchange, "{\"error\":\"Задача не найдена\"}");
            } else {
                taskManager.deleteSubtaskById(subtaskId);
                sendText(exchange, "{\"message\":\"Задача успешно удалена\"}", 200);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }
}
