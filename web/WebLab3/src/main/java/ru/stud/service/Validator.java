package ru.stud.service;

import jakarta.enterprise.context.ApplicationScoped;
import ru.stud.model.DtoResultHolder;

@ApplicationScoped
public class Validator {

    public boolean validate(DtoResultHolder dto) {
        Double x= dto.getX();
        Double y = dto.getY();
        Double r= dto.getR();
        return (x >= -4 && x <= 4 && y >= -5 && y <= 5 && r >= 1 && r <= 3);
    }
}
