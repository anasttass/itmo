package ru.stud;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class HitRecord {
    private final BigDecimal x;
    private final BigDecimal y;
    private final double r;
    private final boolean hit;
    private final String time;
    private final long execNanos;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HitRecord(BigDecimal x, BigDecimal y, double r, boolean hit, LocalDateTime time, long execNanos) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.time = formatTime(time);
        this.execNanos=execNanos;
    }
    private String formatTime(LocalDateTime time) {
        return time.format(formatter);
    }

    public BigDecimal getX() {
        return x;
    }
    public BigDecimal getY() {
        return y;
    }
    public double getR() {
        return r;
    }
    public boolean isHit() {
        return hit;
    }
    public String getTime() {
        return time;
    }
    public long getExecNanos() {
        return execNanos;
    }
}


