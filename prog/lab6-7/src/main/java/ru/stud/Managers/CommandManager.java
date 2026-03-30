package ru.stud.Managers;

import ru.stud.Commands.AbsCommand;
import ru.stud.Common.User;
import ru.stud.Exceptions.IncorrectInputException;

import java.util.*;

public class CommandManager {
    private final HashMap<String, AbsCommand> commands = new HashMap<>();
    private final LinkedList<String> commandHistory = new LinkedList<>();
    private static final int HISTORY_SIZE = 14;


    public void addCommand(String commandName, AbsCommand command){
        commands.put(commandName, command);
    }
    public HashMap<String,AbsCommand> getCommands(){
        return commands;
    }

    //выполнение комманд
    public void executeCommand(String input, Object data, User user){
        String[] parts = input.trim().split(" ");
        String commandName = parts[0];
        AbsCommand command = commands.get(commandName);
        if (command!=null){
            command.execute(parts, data, user);
        } else {
            System.err.println("Неизвестная команда: " + commandName); //должно возвращать Response
        }
    }

    //работа с историей
    public void addToHistory(String commandName) {
        commandHistory.addFirst(commandName);
        if (commandHistory.size() > HISTORY_SIZE) {
            commandHistory.removeLast();
        }
    }
    public LinkedList<String> getHistory(){
        return commandHistory;
    }
}
