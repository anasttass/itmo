package ru.stud.Commands;

import ru.stud.Collection.LabWork;
import ru.stud.Common.CommandRequest;
import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;

import java.util.HashMap;
import java.util.LinkedList;

public class History extends AbsCommand{
    private LinkedList<String> commandHistory;
    public History(LinkedList<String> commandHistory){
        super("history","Вывести последние 14 команд.Не требует аргумента");
        this.commandHistory=commandHistory;
    }

    public CommandResponse execute(Object[] args, Object data, User user){
        try {
            String res = "";
            for (int i = 0; i < commandHistory.size(); i++) {
                res+=("-- " + commandHistory.get(i)+"\n");
            }
            return new CommandResponse(true,res);
        } catch (Exception e) {
            return new CommandResponse(false, "Ошибка при выполнении команды history");
        }
    }
}
