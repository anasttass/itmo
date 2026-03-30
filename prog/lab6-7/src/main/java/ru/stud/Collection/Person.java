package ru.stud.Collection;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Person implements Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private ZonedDateTime birthday; //Поле может быть null
    private Color eyeColor; //Поле не может быть null
    private Color hairColor; //Поле не может быть null
    private Country nationality; //Поле может быть null
    private Location location; //Поле может быть null

    public Person(String name, ZonedDateTime birthday,Color eyeColor, Color hairColor,Country nationality,Location location){
        this.name=name;
        this.birthday=birthday;
        this.eyeColor=eyeColor;
        this.hairColor=hairColor;
        this.nationality=nationality;
        this.location=location;
    }

    public String getName() {
        return name;
    }
    public ZonedDateTime getBirthday() {
        return birthday;
    }
    public Color getEyeColor() {
        return eyeColor;
    }
    public Color getHairColor() {
        return hairColor;
    }
    public Country getNationality() {
        return nationality;
    }
    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name +
                ", birthday=" + birthday +
                ", eyeColor=" + eyeColor +
                ", hairColor=" + hairColor +
                ", nationality=" + nationality +
                ", location=" + location +
                '}';
    }
    public boolean validate(){
        return (name!=null && !name.isEmpty() && birthday!=null && eyeColor!=null && hairColor!=null && nationality!=null && location!=null);
    }
}

