package ru.stud.beans;


import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.stud.dao.IResultRepository;
import ru.stud.model.DtoResultHolder;
import ru.stud.model.ResultHolder;
import ru.stud.service.ServiceManager;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class ResultManager implements Serializable {
    @Inject
    private IResultRepository repository;
    @Inject
    private ServiceManager serviceManager;

    private List<DtoResultHolder> resultsCache;

    @PostConstruct
    public void init(){
        resultsCache=getAndConvertFromRep();
    }
    public void addResult(DtoResultHolder r){
        ResultHolder rEntity=serviceManager.convertNormalToEntity(r); //сначала конвертируем в сущ потом сохраняем
        repository.save(rEntity);
        resultsCache=getAndConvertFromRep();
    }

    public List<DtoResultHolder> getResults() {
        if (resultsCache==null){
            resultsCache=getAndConvertFromRep();
        }
        return resultsCache;
    }

    public void clear(){
        repository.clearAll();
        resultsCache=getAndConvertFromRep();
    }

    private List<DtoResultHolder> getAndConvertFromRep(){
        return repository.listAll().stream().map(serviceManager::convertEntityToNormal).collect(Collectors.toList());
    }
}
