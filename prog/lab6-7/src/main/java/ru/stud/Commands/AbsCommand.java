package ru.stud.Commands;

import ru.stud.Common.CommandRequest;
import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;

public abstract class AbsCommand {
    private final String name;
    private final String description;

    public AbsCommand(String name, String description){
        this.name=name;
        this.description=description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name+" ("+description+")";
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(obj==null)return false;
        if(this.getClass()!=obj.getClass())return false;
        AbsCommand oth = (AbsCommand) obj;
        return name.equals(oth.name) && description.equals(oth.description);
    }

    @Override
    public int hashCode() {
        return name.hashCode()+description.hashCode();
    }

    public CommandResponse execute(Object[] args, Object data, User user){
        return new CommandResponse(true,"Success");
    }
}
