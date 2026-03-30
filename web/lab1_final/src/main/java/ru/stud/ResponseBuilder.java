package ru.stud;

import java.nio.charset.StandardCharsets;

public class ResponseBuilder {

    public String build(int code, String json) {
        String status = switch (code) {
            case 200 -> "200 OK";
            case 400 -> "400 Bad Request";
            default -> "500 Internal Server Error";
        };

        int length = json.getBytes(StandardCharsets.UTF_8).length;

        return "HTTP/1.1 " + status + "\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + length + "\r\n" +
                "Connection: close\r\n\r\n" +
                json;
    }
}
