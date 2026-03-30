package ru.stud.Server;

import ru.stud.DataBase.DBConnector;
import ru.stud.DataBase.DBManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class RunServer {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Введите порт: ");
        int port = Integer.parseInt(sc.nextLine().trim());

        System.out.println("URL БД: jdbc:postgresql://pg:5432/studs");
        String url = "jdbc:postgresql://pg:5432/studs";

        System.out.print("Введите пользователя БД: ");
        String user = sc.nextLine().trim();

        System.out.print("Введите пароль БД: ");
        String password = sc.nextLine().trim();
        try {
            DBConnector connector = new DBConnector(url, user, password);
            Connection connection = connector.getConnection();
            DBManager dbManager = new DBManager(connection);

            Server server = new Server(port, dbManager);
            server.run();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при запуске сервера: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при запуске сервера: " + e.getMessage());
        }
    }
}
