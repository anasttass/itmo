package ru.stud.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import ru.stud.model.DtoResultHolder;

import static java.lang.Math.pow;

@Named
@ApplicationScoped
public class HitChecker {

    public boolean isHit(DtoResultHolder dto){
        Double x= dto.getX();
        Double y = dto.getY();
        Double r= dto.getR();
        if (x>=0 && y>=0){
            return (x+(2*y))<=r;
        }
        else if (x>=0 && y<=0){
            return (pow(x,2)+pow(y,2))<=pow(r,2);
        }
        else if(x<=0 && y<=0){
            return (x>=-r)&&(y>=-r);
        }
        else{
            return false;
        }
    }
}
