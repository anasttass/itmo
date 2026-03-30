package ru.stud;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AreaChecker {
    public boolean isHit(BigDecimal x, BigDecimal y, double r){
        BigDecimal bdr =BigDecimal.valueOf(r);
        if (x.compareTo(BigDecimal.ZERO)>=0 && y.compareTo(BigDecimal.ZERO)>=0){
            return (x.add(y).compareTo(bdr) < 0);
        }
        else if(x.compareTo(BigDecimal.ZERO)<=0 && y.compareTo(BigDecimal.ZERO)>=0){
            BigDecimal dx = x.pow(2);
            BigDecimal dy = y.pow(2);
            BigDecimal dr = bdr.pow(2);
            return (dx.add(dy).compareTo(dr) < 0);
        }
        else if(x.compareTo(BigDecimal.ZERO)<=0 && y.compareTo(BigDecimal.ZERO)<=0){
            return(x.compareTo(bdr.negate()) > 0 && y.compareTo(bdr.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP).negate()) > 0);
        }
        else if(x.compareTo(BigDecimal.ZERO)>=0 && y.compareTo(BigDecimal.ZERO)<=0){
            return false;
        }
        return false;
    }
}
