package ru.stud.Common;

import java.io.Serializable;

public class CommandResponse implements Serializable {
    private String message;
    private boolean success;

    public CommandResponse(boolean success, String message){
        this.success=success;
        this.message=message;
    }

    public boolean isSuccess(){
        return success;
    }

    public String getMessage(){
        return message;
    }
}
