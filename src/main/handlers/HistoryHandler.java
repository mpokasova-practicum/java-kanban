package main.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.manager.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }


    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (!exchange.getRequestMethod().equals("GET")) {
                sendText(exchange, "{\"error\":\"Метод не поддерживается\"}", 405);
                return;
            }
            sendText(exchange, gson.toJson(taskManager.getHistoryManager().getHistory()), 200);
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }
}
