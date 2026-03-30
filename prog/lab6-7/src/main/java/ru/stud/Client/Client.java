package ru.stud.Client;

import ru.stud.Commands.Add;
import ru.stud.Commands.FilterGreaterThanDifficulty;
import ru.stud.Commands.RemoveByID;
import ru.stud.Common.CommandRequest;
import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;
import ru.stud.Managers.CommandManager;
import ru.stud.Managers.InputHelper;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Client {
    private final Scanner scanner = new Scanner(System.in);
    private final InputHelper inputHelper = new InputHelper(scanner);
    private final CommandManager commandManager = new CommandManager();

    private final InetSocketAddress serverAdress;
    private DatagramChannel channel;
    private Selector selector;

    private User user;
    private final Set<String> activeScripts = new HashSet<>();

    public Client(String host, int port){
        this.serverAdress=new InetSocketAddress(host,port);
    }
    public void run(){
        try {
            initNetwork();
            authorize();

            while (true) {
                System.out.print("Введите команду: ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) continue;

                if (input.equals("exit")) {
                    System.out.println("Завершение работы клиента");
                    break;
                }

                if (input.startsWith("execute_script")) {
                    String[] parts = input.split(" ");
                    if (parts.length < 2) {
                        System.out.println("Ошибка: укажите файл");
                        continue;
                    }
                    executeScript(parts[1]);
                    continue;
                }

                CommandRequest request = buildRequest(input);
                sendRequest(request);
                receiveResponse();
            }

        } catch (IOException e) {
            System.out.println("Ошибка сети: " + e.getMessage());
        }
    }

    public void initNetwork()throws IOException{
        channel = DatagramChannel.open();
        channel.configureBlocking(false);

        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);

        System.out.println("UDP клиент запущен");
    }

    private void authorize() throws IOException {
        while (true) {
            System.out.println("Логин: ");
            String username = scanner.nextLine().trim();
            System.out.println("Пароль: ");
            String password = scanner.nextLine().trim();
            user = new User(username, password);

            CommandRequest request=new CommandRequest("auth",null,null,user);
            sendRequest(request);
            boolean isOk = authResponse();
            if (!isOk){
                continue;
            }
            break;
        }
    }

    private CommandRequest buildRequest(String input){
        String[] parts = input.split(" ");
        String commandName = parts[0];
        String[] args = new String[parts.length-1];
        System.arraycopy(parts, 1,args,0,args.length);

        Object data = null;
        switch (commandName) {

            case "add":
                // создаём объект коллекции на клиенте
                data = inputHelper.readLabWork("Создание LabWork");
                break;

            case "update_by_id":
                // id идёт аргументом, объект — в data
                data = inputHelper.readLabWork("Обновление LabWork");
                break;

            case "filter_greater_than_difficulty":
                // передаём Difficulty как аргумент
                if (args.length == 0) {
                    System.out.println("Ошибка: необходимо указать сложность");
                }
                break;

            case "remove_by_id":
                if (args.length == 0) {
                    System.out.println("Ошибка: необходимо указать id");
                }
                break;

            case "execute_script":
                if (args.length == 0) {
                    System.out.println("Ошибка: необходимо указать файл");
                }
                break;

            default:
                // остальные команды без data
                break;
        }

        return new CommandRequest(commandName,args,data,user);

    }

    private void sendRequest(CommandRequest request) throws IOException{
        byte[] data = serialize(request);
        ByteBuffer buffer = ByteBuffer.wrap(data);

        channel.send(buffer, serverAdress);
        System.out.println("Запрос отправлен серверу");
    }

    private void receiveResponse()throws IOException {
        if (selector.select(3000) == 0) {
            System.out.println("Сервер временно недоступен");
            return;
        }

        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();

            if (key.isReadable()) {
                ByteBuffer buffer = ByteBuffer.allocate(65535);
                channel.receive(buffer);
                buffer.flip();

                CommandResponse response = deserialize(buffer);

                System.out.println("\n--- Ответ сервера ---");
                System.out.println(response.getMessage());
                System.out.println("----------------------\n");
            }
        }
    }

    //для ответа при авторизации
    private boolean authResponse()throws IOException{
        if (selector.select(3000) == 0) {
            System.out.println("Сервер временно недоступен");
            return false;
        }

        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();

            if (key.isReadable()) {
                ByteBuffer buffer = ByteBuffer.allocate(65535);
                channel.receive(buffer);
                buffer.flip();

                CommandResponse response = deserialize(buffer);
                return  response.isSuccess();
            }
        }
        return  false;
    }

    private byte[] serialize(Object obj) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        return baos.toByteArray();
    }

    private CommandResponse deserialize(ByteBuffer buffer) throws  IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array(),0,buffer.limit());
        ObjectInputStream ois = new ObjectInputStream(bais);
        try{
            return (CommandResponse) ois.readObject();
        }catch (ClassNotFoundException e){
            throw new IOException("Ошибка десериализации ответа", e);
        }
    }

    private CommandRequest buildRequestFromScript(String commandName, String[] args) {
        Object data = null;

        switch (commandName) {
            case "add":
                data = inputHelper.readLabWork("Создание LabWork (скрипт)");
                break;

            case "update_by_id":
                data = inputHelper.readLabWork("Обновление LabWork (скрипт)");
                break;

            default:
                break;
        }

        return new CommandRequest(commandName, args, data, user);
    }

    private void executeScript(String filename){
        if (activeScripts.contains(filename)){
            System.out.println("Ошибка! Рекурсивный ввод запрещен: "+filename);
            return;
        }
        File file = new File(filename);
        if (!file.exists() || !file.isFile()){
            System.out.println("Ошибка! Файл не найден или не существует");
            return;
        }
        activeScripts.add(filename);

        try(Scanner fileScanner = new Scanner(file)){
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                System.out.println("Выполняю: " + line);
                String[] parts = line.split(" ", 2);
                String commandName = parts[0].toLowerCase().trim();
                String[] commandArgs = new String[parts.length - 1];
                System.arraycopy(parts, 1, commandArgs, 0, commandArgs.length);;

                if (commandName.equals("execute_script")){
                    if(commandArgs.length==0){
                        System.out.println("Ошибка. Имя файла не задано!");
                        continue;
                    }
                    executeScript(commandArgs[0]);
                }else{
                    CommandRequest request = buildRequestFromScript(commandName,commandArgs);
                    sendRequest(request);
                    receiveResponse();
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении скрипта: "+e.getMessage());
        }finally {
            activeScripts.remove(filename);
        }
    }

}
