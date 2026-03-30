package ru.stud;

import java.math.BigDecimal;
import java.util.Locale;

public class ResultData {
    private boolean hit;
    private BigDecimal x;
    private  BigDecimal y;
    private double r;
    public ResultData(BigDecimal x,BigDecimal y, double r, boolean hit){
        this.x=x;
        this.y=y;
        this.r=r;
        this.hit=hit;
    }
    public String toJson() {
        return String.format(Locale.US,
                "{\"x\":%s,\"y\":%s,\"r\":%.3f,\"hit\":%b}",
                x.toPlainString(),
                y.toPlainString(),
                r,
                hit
        );
    }
}
