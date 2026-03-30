package ru.stud.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import ru.stud.model.ResultHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ResultManagerWithoutDB implements Serializable {

    private List<ResultHolder> results;

    @PostConstruct
    public void init(){
        results = new ArrayList<>();
    }

    public void addResult(ResultHolder r){
        results.add(r);
    }

    public List<ResultHolder> getResults() {
        return results;
    }

    public void clear(){
        results.clear();
    }
}
