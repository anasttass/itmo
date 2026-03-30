package ru.stud.Collection;

import java.io.Serializable;

public class Location implements Serializable {
    private float x;
    private Double y; //Поле не может быть null
    private Double z; //Поле не может быть null

    public Location(float x, Double y, Double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
        return z;
    }

    public float getX() {
        return x;
    }

    @Override
    public String toString() {
        return "Location{ X: "+x+"; Y: "+y+"; Z: "+z+"}";
    }

    public boolean validate(){
        return (z!=null && y!=null);
    }
}
