package ru.stud.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.xml.bind.ValidationException;
import ru.stud.dao.IResultRepository;
import ru.stud.model.DtoResultHolder;
import ru.stud.model.ResultHolder;
import ru.stud.monitoring.CounterMBean;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ServiceManager {
    @Inject
    private Validator validator;
    @Inject
    private HitChecker hitChecker;
    @Inject
    private Converter converter;
    @Inject
    private IResultRepository repository;
    @Inject
    private MonitorManager monitorManager;

    private List<DtoResultHolder> resultsCache;

    @PostConstruct
    public void init(){
        resultsCache=getAndConvertFromRep();
    }

    public void checkAndSave(DtoResultHolder dto) throws ValidationException {
        if (!validator.validate(dto)){
            throw new ValidationException("Невалидные данные");
        }
        boolean hit = hitChecker.isHit(dto);
        dto.setHit(hit);

        //MONITORING
        monitorManager.prossesMonitoring(dto);


        ResultHolder rEntity=this.convertNormalToEntity(dto); //сначала конвертируем в сущ потом сохраняем
        repository.save(rEntity);
        resultsCache.add(0,dto);
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

    public DtoResultHolder convertEntityToNormal(ResultHolder rh){
        return converter.entityToNormal(rh);
    } //конвертирует сущность в обычный класс

    public ResultHolder convertNormalToEntity(DtoResultHolder drh){
        return converter.NormalToEntity(drh);
    }//конвертирует обычный класс в сущность

    private List<DtoResultHolder> getAndConvertFromRep(){
        return repository.listAll().stream().map(this::convertEntityToNormal).collect(Collectors.toList());
    }
}
