package ru.stud.Commands;

import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;
import ru.stud.Server.Server;

import java.util.Scanner;

public class Exit extends AbsCommand{
    private final Server server;

    public Exit(Server server) {
        super("exit", "Завершить работу (без сохранения)");
        this.server=server;
    }

    public CommandResponse execute(Object[] args, Object data, User user){
        return new CommandResponse(true,"Завершение работы приложения");
    }
}
