package ru.stud.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Point {
    private double x;
    private BigDecimal y;
    private double  r;
    private boolean hit;
    private String time;

    DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public Point(double x, BigDecimal y, double r,boolean hit){
        this.x=x;
        this.y=y;
        this.r=r;
        this.hit=hit;
        this.time=LocalDateTime.now().format(formatter);
    }
    public double getX(){return x;}
    public BigDecimal getY(){return y;}
    public double getR(){return r;}
    public boolean getHit(){return hit;}
    public String getTime(){return time;}

    private boolean setHit(){
        HitChecker hitChecker = new HitChecker();
        return hitChecker.isHit(x,y,r);
    }
}
