package ru.stud.service;


import jakarta.enterprise.context.ApplicationScoped;
import ru.stud.model.DtoResultHolder;
import ru.stud.model.ResultHolder;

@ApplicationScoped
public class ResultConverter {
    public DtoResultHolder entityToNormal(ResultHolder rh){
        Double x=rh.getX();
        Double y=rh.getY();
        Double r=rh.getR();
        boolean hit=rh.getHit();
        String time=rh.getTime();

        DtoResultHolder result = new DtoResultHolder();

        result.setX(x);
        result.setY(y);
        result.setR(r);
        result.setHit(hit);
        result.setTime(time);

        return result;
    }


    public ResultHolder NormalToEntity(DtoResultHolder drh){
        Double x=drh.getX();
        Double y=drh.getY();
        Double r=drh.getR();
        boolean hit=drh.getHit();
        String time=drh.getTime();

        ResultHolder result = new ResultHolder(x,y,r,hit);

//        result.setX(x);
//        result.setY(y);
//        result.setR(r);
//        result.setHit(hit);
//        result.setTime(time);

        return result;
    }
}

