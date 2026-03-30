package ru.stud.Collection;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private Long x; //Поле не может быть null
    private Long y; //Поле не может быть null

    public Coordinates(Long x, Long y){
        this.x=x;
        this.y=y;
    }

    public Long getX(){
        return x;
    }

    public Long getY(){
        return y;
    }

    @Override
    public String toString(){
        return "Coordinates{X: "+ x + "; Y: " + y + "}";
    }

    public boolean validate(){
        return (x!=null && y!=null);
    }
}
