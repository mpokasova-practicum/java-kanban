package main.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.manager.TaskManager;
import main.model.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicsHandler extends BaseHttpHandler {
    public EpicsHandler(TaskManager taskManager, Gson gson) {
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
                case GET_SUBTASKS -> handleGetSubtasks(exchange);
                default -> sendText(exchange, "{\"error\":\"Метод не поддерживается\"}", 405);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }

    public void handleGetAll(HttpExchange exchange) throws IOException {
        try {
            sendText(exchange, gson.toJson(taskManager.getAllEpics()), 200);
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }

    public void handleGetId(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                sendNotFound(exchange, "{\"error\":\"Некорректно указан номер эпика\"}");
                return;
            }
            int epicId = taskIdOpt.get();

            Epic epic = (Epic) taskManager.getEpicById(epicId);
            if (epic == null) {
                sendNotFound(exchange, "{\"error\":\"Эпик не найден\"}");
            } else {
                sendText(exchange, gson.toJson(epic), 200);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }

    public void handlePost(HttpExchange exchange) throws IOException {
        try {
            String taskRequest = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(taskRequest, Epic.class);

            taskManager.createEpic(epic);
            exchange.sendResponseHeaders(201, 0);
            exchange.close();
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }

    public void handleDelete(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                sendNotFound(exchange, "{\"error\":\"Некорректно указан номер эпика\"}");
                return;
            }
            int epicId = taskIdOpt.get();

            Epic epic = (Epic) taskManager.getEpicById(epicId);
            if (epic == null) {
                sendNotFound(exchange, "{\"error\":\"Эпик не найден\"}");
            } else {
                taskManager.deleteEpicById(epicId);
                sendText(exchange, "{\"message\":\"Эпик успешно удален\"}", 200);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }

    public void handleGetSubtasks(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                sendNotFound(exchange, "{\"error\":\"Некорректно указан номер эпика\"}");
                return;
            }
            int epicId = taskIdOpt.get();

            Epic epic = (Epic) taskManager.getEpicById(epicId);
            if (epic == null) {
                sendNotFound(exchange, "{\"error\":\"Эпик не найден\"}");
            } else {
                sendText(exchange, gson.toJson(taskManager.getSubtasksByEpic(epic)), 200);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }
}
