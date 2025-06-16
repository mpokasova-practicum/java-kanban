package main;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import main.handlers.*;
import main.manager.Managers;
import main.manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    public static TaskManager taskManager = Managers.getDefault();
    public static Gson gson;

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new TasksHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicsHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));

        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");

    }
}

