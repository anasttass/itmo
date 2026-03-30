package ru.stud.Commands;

import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;
import ru.stud.Managers.CommandManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExecuteScript extends AbsCommand{
    private CommandManager commandManager;

    public ExecuteScript(CommandManager commandManager) {
        super("execute_script", "Считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит");
        this.commandManager=commandManager;
    }

    private ArrayList <String> scripts = new ArrayList<>();

    public CommandResponse execute(Object[] args, Object data, User user){

        if (args.length < 1) {
            return new CommandResponse(false, "Укажите имя скрипта");
        }
        String scriptName = (String) args[0];
        try (Scanner scriptScanner=new Scanner(new File(scriptName))){
            scripts.add(scriptName);
            while (scriptScanner.hasNextLine()) {
                String line = scriptScanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                System.out.println("Выполняю: " + line);
                String[] parts = line.split(" ", 2);
                String commandName = parts[0].toLowerCase().trim();
                String[] commandArgs = parts.length > 1 ? parts[1].split(" ") : new String[0];
                if (commandName.equals("execute_script")){
                    if (scripts.contains(commandArgs[0])){
                        return new CommandResponse(false, "Ошибка: Рекурсивный вывод скриптов запрщён");
                    }
                    scripts.add(commandArgs[0]);
                }
                commandManager.executeCommand(line, data, user);
            }
            return new CommandResponse(true, "Скрипт выполнен");
        } catch (FileNotFoundException e) {
            return new CommandResponse(false, "Файл скрипта не найден: " + scriptName);
        } catch (Exception e) {
            return new CommandResponse(false, "Ошибка выполнения скрипта: " + e.getMessage());
        }
    }
}
