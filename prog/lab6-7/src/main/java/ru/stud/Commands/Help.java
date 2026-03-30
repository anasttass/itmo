package ru.stud.Commands;

import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;

import java.util.HashMap;

public class Help extends AbsCommand{
    private HashMap<String, AbsCommand> commands;

    public Help(){
        super("help","Вывести список команд.Не требует аргумента");
    }

    public void setCommands(HashMap<String, AbsCommand> commands) {
        this.commands = commands;
    }

    public CommandResponse execute(Object[] args, Object data, User user){
        try {
            String res ="";
            for (AbsCommand command : commands.values()) {
                res+=("-- "+ command.getName()+" - "+command.getDescription()+"\n");
            }
            return new CommandResponse(true,res);
        } catch (Exception e) {
            return new CommandResponse(false,"Ошибка при работе функции help "+e.getMessage());
        }
    }
}
