package main.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.google.gson.*;

public abstract class BaseHttpHandler implements HttpHandler {
    public enum Endpoint {
        GET_ALL,
        GET_ID,
        GET_SUBTASKS,
        POST,
        DELETE,
        UNKNOWN
    }

    public TaskManager taskManager;
    public Gson gson;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }


    protected void sendText(HttpExchange exchange, String responseString, int rCode) throws IOException {
        byte[] resp = responseString.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(rCode, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange, String responseString) throws IOException {
        sendText(exchange, responseString, 404);
    }

    protected void sendHasInteractions(HttpExchange exchange, String responseString) throws IOException {
        sendText(exchange, responseString, 406);
    }

    protected void sendServerError(HttpExchange exchange, Exception exception) throws IOException {
        sendText(exchange, "{\"error\":\"" + exception.getMessage() + "\"}", 500);
    }

    public Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        switch (requestMethod) {
            case "DELETE" -> {
                return Endpoint.DELETE;
            }
            case "POST" -> {
                return Endpoint.POST;
            }
            case "GET" -> {
                if (pathParts.length == 3) {
                    return Endpoint.GET_SUBTASKS;
                } else if (pathParts.length == 2) {
                    return Endpoint.GET_ID;
                } else {
                    return Endpoint.GET_ALL;
                }
            }
        }

        return Endpoint.UNKNOWN;
    }
}