package com.example; 
import java.util.List;

import com.google.gson.Gson;

import spark.Spark;

public class App {
    private static final Database db = new Database();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        Spark.options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            response.type("application/json");
        });

        Spark.get("/messages", (req, res) -> {
            List<Message> messages = db.getMessages();
            return gson.toJson(messages);
        });
        Spark.post("/messages", (req, res) -> {
            String body = req.body();
            MessageRequest messageRequest = gson.fromJson(body, MessageRequest.class);
            db.sendMessage(messageRequest.getSenderId(), messageRequest.getContent());
            res.status(201);
            return "Сообщение отправлено";
        });
    }
    private static class MessageRequest {
        private int senderId;
        private String content;
        public int getSenderId() { 
            return senderId; 
        }
        
        public String getContent() { 
            return content; 
        }
    }
}