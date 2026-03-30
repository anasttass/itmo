package ru.stud.Client;

import java.io.IOException;
import java.util.Scanner;

public class RunClient {
    public static void main(String[] args)throws IOException{
        String host = "localhost";
        Scanner scanner = new Scanner(System.in);
        System.out.println("НАЧАЛО РАБОТЫ КЛИЕНТА");
        System.out.println("Введите порт: ");
        int port = Integer.parseInt(scanner.nextLine().trim());
        Client client = new Client(host, port);
        client.run();
    }
}
