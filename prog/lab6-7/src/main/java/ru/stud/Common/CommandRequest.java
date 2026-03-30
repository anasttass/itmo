package ru.stud.Common;

import java.io.Serializable;

public class CommandRequest implements Serializable {
    private String commandName;
    private Object[] args;
    private Object data;
    private User user;

    public CommandRequest(String commandName, Object[] args, Object data, User user){
        this.commandName=commandName;
        this.args=args;
        this.data=data;
        this.user=user;
    }
    public String getCommandName(){
        return commandName;
    }

    public Object[] getArgs(){
        return args;
    }

    public Object getData() {
        return data;
    }

    public User getUser() {
        return user;
    }
}
