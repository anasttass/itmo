package ru.stud.backend.model;

import java.math.BigDecimal;

public class HitChecker {

    public boolean isHit(double x, BigDecimal y, double r){
        BigDecimal dx = BigDecimal.valueOf(x);
        BigDecimal dr=BigDecimal.valueOf(r);
        if(dx.compareTo(BigDecimal.ZERO)<=0 && y.compareTo(BigDecimal.ZERO)>=0){ //вторая четверть
            BigDecimal ddx= dx.multiply(dx);
            BigDecimal ddy=y.multiply(y);
            BigDecimal ddr=dr.multiply(dr);
            return (ddx.add(ddy)).compareTo(ddr)<=0;
        }
        else if (dx.compareTo(BigDecimal.ZERO)<=0 && y.compareTo(BigDecimal.ZERO)<=0){ //третья четверть
            BigDecimal sum = y.add(dx);
            return sum.compareTo(dr)<=0;
        }
        else if (dx.compareTo(BigDecimal.ZERO)>=0 && y.compareTo(BigDecimal.ZERO)<=0){ //четвертая четверть
            boolean f1 = dx.compareTo(dr)<=0;
            BigDecimal minr = dr.divide(BigDecimal.valueOf(2)).negate();
            boolean f2 = y.compareTo(minr)>=0;
            return f1&&f2;
        }
        return false;
    }
}
