package ru.stud.dao;

import ru.stud.model.ResultHolder;

import java.util.List;

public interface IResultRepository {
    ResultHolder save(ResultHolder r);
    List<ResultHolder> listAll();
    void clearAll();
}
