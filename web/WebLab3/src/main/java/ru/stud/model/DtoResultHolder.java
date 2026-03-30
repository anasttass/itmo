package ru.stud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DtoResultHolder implements Serializable {
    private Double x;
    private Double y;
    private Double r;
    private boolean hit;
    private String time;

    DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            .withZone(ZoneId.of("Europe/Moscow"));


    public DtoResultHolder(Double x, Double y, Double r){
        this.x=x;
        this.y=y;
        this.r=r;
        //this.hit=hit;
        this.time= ZonedDateTime.now(ZoneId.of("Europe/Moscow")).format(formatter);
    }
    public  DtoResultHolder(){}
    public Double getR() {return r;}
    public Double getX() {return x;}
    public Double getY() {return y;}
    public boolean getHit() {return hit;}
    public String getTime() {return time;}

    public void setHit(boolean hit) {this.hit = hit;}
    public void setX(Double x) {this.x = x;}
    public void setY(Double y) {this.y = y;}
    public void setR(Double r) {this.r = r;}
    public void setTime(String time) {this.time = time;}
}
