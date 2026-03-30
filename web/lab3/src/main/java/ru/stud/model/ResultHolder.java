package ru.stud.model;

import jakarta.persistence.*;

import javax.annotation.processing.Generated;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name="results")
public class ResultHolder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Double x;
    @Column(nullable = false)
    private Double y;
    @Column(nullable = false)
    private Double r;
    @Column(nullable = false)
    private boolean hit;
    @Column(nullable = false)
    private String time;

    @Transient
    DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").withZone(ZoneId.of("Europe/Moscow"));

    public ResultHolder(){}

    public ResultHolder(Double x, Double y, Double r, boolean hit){
        this.x=x;
        this.y=y;
        this.r=r;
        this.hit=hit;
        this.time= ZonedDateTime.now(ZoneId.of("Europe/Moscow")).format(formatter);
    }

    public Double getR() {return r;}
    public Double getX() {return x;}
    public Double getY() {return y;}
    public boolean getHit() {return hit;}
    public String getTime() {return time;}

    public void setId(Long id) {this.id=id;}
    public void setHit(boolean hit) {this.hit = hit;}
    public void setX(Double x) {this.x = x;}
    public void setY(Double y) {this.y = y;}
    public void setR(Double r) {this.r = r;}
    public void setTime(String time) {this.time = time;}
}
