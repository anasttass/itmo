package ru.stud.Server;

import ru.stud.Collection.LabWork;
import ru.stud.Commands.*;
import ru.stud.Common.*;
import ru.stud.DataBase.DBManager;
import ru.stud.Managers.CollectionManager;
import ru.stud.Managers.CommandManager;
import ru.stud.Managers.InputHelper;


import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.security.MessageDigest;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * UDP сервер для обработки команд от клиентов
 */
public class Server {
    private final CollectionManager collectionManager;
    private final CommandManager commandManager;
    private final ReentrantLock collectionLock = new ReentrantLock();
    private final ExecutorService processPool = Executors.newCachedThreadPool();
    private final DBManager dbManager;
    private final int port;

    public Server(int port,DBManager dbManager) {
        this.port = port;

        this.dbManager=dbManager;
        this.collectionManager = new CollectionManager(this.dbManager);
        this.commandManager = new CommandManager();

        registerCommands(commandManager);
    }

    /**
     * Регистрируем команды так же, как в старом сервере
     */
    private void registerCommands(CommandManager commandManager) {
        System.out.println("Регистрация команд...");

        // простые команды
        Help helpCommand = new Help();
        commandManager.addCommand("help", helpCommand);
        commandManager.addCommand("info", new Info(collectionManager));
        commandManager.addCommand("show", new Show(collectionManager));
        commandManager.addCommand("clear", new Clear(collectionManager));
//        commandManager.addCommand("save", new Save(collectionManager));
        commandManager.addCommand("shuffle", new Shuffle(collectionManager));
        commandManager.addCommand("reorder", new Reorder(collectionManager));
        commandManager.addCommand("sum_of_minimal_points", new SumOfMinimalPoints(collectionManager));
        commandManager.addCommand("print_ascending", new PrintAscending(collectionManager));

        // команды с аргументами
        commandManager.addCommand("remove_by_id", new RemoveByID(collectionManager));
        commandManager.addCommand("filter_greater_than_difficulty", new FilterGreaterThanDifficulty(collectionManager));

        // команды с объектами
        commandManager.addCommand("add", new Add(collectionManager));
        commandManager.addCommand("update_by_id", new UpdateId(collectionManager));

        // специальные команды
        commandManager.addCommand("history", new History(commandManager.getHistory()));
        commandManager.addCommand("exit", new Exit(null));
        commandManager.addCommand("execute_script", new ExecuteScript(commandManager));

        helpCommand.setCommands(commandManager.getCommands());
        System.out.println("Зарегистрировано команд: " + commandManager.getCommands().size());
    }

    /**
     * Запуск сервера
     */
    public void run() throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(port));
        channel.configureBlocking(false);

        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);

        ByteBuffer buffer = ByteBuffer.allocate(65507); // максимум UDP-пакета

        System.out.println("UDP сервер запущен на порту " + port);

        while (true) {
            selector.select();

            for (SelectionKey key : selector.selectedKeys()) {
                if (key.isReadable()) {
                    buffer.clear();
                    SocketAddress clientAddress = channel.receive(buffer);
                    if (clientAddress != null) {
                        buffer.flip();
                        byte[] data = new byte[buffer.limit()];
                        buffer.get(data);

                        //обработка в отдельном потоке
                        processPool.submit(() -> handleRequest(channel, clientAddress, data));
                    }
                }
            }
            selector.selectedKeys().clear();
        }
    }

    private void handleRequest(DatagramChannel channel, SocketAddress clientAddress, byte[] data) {
        try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(data))) {
            CommandRequest request = (CommandRequest) objIn.readObject();

            CommandResponse response;
            collectionLock.lock();
            try {
                response = processRequest(request);
            } finally {
                collectionLock.unlock();
            }

            sendResponse(channel, clientAddress, response);

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при обработке запроса: " + e.getMessage());
        }
    }

    private CommandResponse processRequest(CommandRequest request) {
        User user = request.getUser();
        String commandName = request.getCommandName();

        if (!dbManager.isUserExists(user.getUsername())) { //если новое имя - регистрируем
            dbManager.registerUser(user);
        }

        if(commandName.equals("auth")){ //спец команда при входе, чтобы не пускать к работе без авторизации
            if (user == null || !dbManager.checkPassword(user.getUsername(), user.getPassword())) {
                return new CommandResponse(false, "Ошибка авторизации. Неверный логин и пароль.");
            }
            return new CommandResponse(true,"Авторизация прошла успешно");
        }

        if (user == null || !dbManager.checkPassword(user.getUsername(), user.getPassword())) { //на всякий случай еще раз при каждом запросе
            return new CommandResponse(false, "Ошибка авторизации. Проверьте логин и пароль.");
        }

        commandManager.addToHistory(commandName);

        Object[] args = request.getArgs();
        Object data = request.getData();

        var command = commandManager.getCommands().get(commandName);
        if (command == null) {
            return new CommandResponse(false, "Неизвестная команда: " + commandName);
        }

        return command.execute(args, data, user);
    }

    private void sendResponse(DatagramChannel channel, SocketAddress clientAddress, CommandResponse response) {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream objOut = new ObjectOutputStream(byteOut)) {

            objOut.writeObject(response);
            objOut.flush();

            byte[] respData = byteOut.toByteArray();
            ByteBuffer buffer = ByteBuffer.wrap(respData);

            channel.send(buffer, clientAddress);

        } catch (IOException e) {
            System.err.println("Ошибка отправки ответа клиенту: " + e.getMessage());
        }
    }
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-224");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при хэшировании пароля", e);
        }
    }
}
