package ru.stud;

import com.fastcgi.FCGIInterface;
import java.time.LocalDateTime;


public class Server {

    private final RequestHandler handler;
    private final ResponseBuilder responseBuilder;

    public Server() {
        this.handler = new RequestHandler();
        this.responseBuilder = new ResponseBuilder();
    }

    public void start() {
        FCGIInterface fcgi = new FCGIInterface();

        while (fcgi.FCGIaccept() >= 0) {
            try {
                String method = System.getProperty("REQUEST_METHOD");
                String pathInfo = System.getProperty("PATH_INFO");
                if ("GET".equalsIgnoreCase(method) && "/points".equals(pathInfo)){
                    String json = handler.getHistory();
                    System.out.print(responseBuilder.build(200, json));
                    continue;
                }

                String query = "";

                if ("POST".equalsIgnoreCase(method)) {
                    String contentLengthStr = System.getProperty("CONTENT_LENGTH");
                    if (contentLengthStr != null && !contentLengthStr.isEmpty()) {
                        int contentLength = Integer.parseInt(contentLengthStr);
                        if (contentLength > 0) {
                            byte[] body = new byte[contentLength];
                            int bytesRead = System.in.read(body);
                            if (bytesRead == contentLength) {
                                query = new String(body, java.nio.charset.StandardCharsets.UTF_8);
                            }
                        }
                    }
                } else {
                    //на всякий
                    query = System.getProperty("QUERY_STRING");
                }

                String json = handler.handle(query);

                System.out.print(responseBuilder.build(200, json));

            } catch (ValidationException e) {
                JsonMaker errorjson = new JsonMaker(e.getMessage());
                System.out.print(responseBuilder.build(400, errorjson.toJson()));

            } catch (Exception e) {
                JsonMaker errorjson = new JsonMaker("Internal server error");
                System.out.print(responseBuilder.build(500, errorjson.toJson()));
            }
        }
    }
}

