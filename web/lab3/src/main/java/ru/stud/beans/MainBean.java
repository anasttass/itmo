package ru.stud.beans;


import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.xml.bind.ValidationException;
import ru.stud.model.DtoResultHolder;
import ru.stud.service.ServiceManager;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class MainBean implements Serializable {
    private Double x;
    private Double y;
    private Double r;

    @Inject
    private ServiceManager serviceManager;

    public Double getX(){return x;}
    public void setX(Double x){this.x=x;}
    public Double getY(){return y;}
    public void setY(Double y){this.y=y;}
    public Double getR(){return r;}
    public void setR(Double r){this.r=r;}

    public void checkPoint(){
        if (x==null || y==null || r==null){ //проверяем только наличие
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Ошибка. Все поля должны быть заполнены","Все поля должны быть заполнены"));
            return;
        }
        try{
            DtoResultHolder dtoResultHolder= new DtoResultHolder(x,y,r);
            serviceManager.checkAndSave(dtoResultHolder);
            //resultManager.addResult(dtoResultHolder);

        } catch (ValidationException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Ошибка. Невалидные данные","Невалидные данные"));
            return;
        }
    }

    public void checkPointFromClick(){
        checkPoint();
    }

    public void clearHistory(){
        serviceManager.clear();
    }

    public List<DtoResultHolder> getResults(){
        return serviceManager.getResults();
    }
}
