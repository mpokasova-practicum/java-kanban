package main.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.manager.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {
    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (!exchange.getRequestMethod().equals("GET")) {
                sendText(exchange, "{\"error\":\"Метод не поддерживается\"}", 405);
                return;
            }
            sendText(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }
}
