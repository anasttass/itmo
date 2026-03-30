package ru.stud.Collection;

import java.io.Serializable;
import java.time.LocalDate;

public class LabWork implements Comparable<LabWork>, Serializable {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer minimalPoint; //Поле может быть null, Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле может быть null
    private Person author; //Поле не может быть null

    private static long idCounter = 1;

    public LabWork(String name, Coordinates coordinates, Integer minimalPoint, Difficulty difficulty,Person author){
        this.id=idCounter++;
        this.name=name;
        this.coordinates=coordinates;
        this.creationDate=LocalDate.now();
        this.minimalPoint=minimalPoint;
        this.difficulty=difficulty;
        this.author=author;
    }
    public LabWork(String name, Coordinates coordinates, LocalDate creationDate, Integer minimalPoint, Difficulty difficulty,Person author){
        this.id=idCounter++;
        this.name=name;
        this.coordinates=coordinates;
        this.creationDate=creationDate;
        this.minimalPoint=minimalPoint;
        this.difficulty=difficulty;
        this.author=author;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Integer getMinimalPoint() {
        return minimalPoint;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Person getAuthor() {
        return author;
    }
    public void fixIdCounter(){
        idCounter++;
    }

    @Override
    public String toString() {
        return "LabWork{" +
                "id=" + id +
                ", name='" + name +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", minimalPoint=" + minimalPoint +
                ", difficulty=" + difficulty +
                ", author=" + author +
                '}';
    }

    @Override
    public int compareTo(LabWork o) {
        return Integer.compare(this.minimalPoint,o.minimalPoint); //сортировка по мин.баллу
    }

    public boolean validate(){
        return(name!=null && id!=null && id>0 && !name.isEmpty() && coordinates.validate() && minimalPoint!=null && minimalPoint>0
            && difficulty!=null && author.validate());
    }
}
