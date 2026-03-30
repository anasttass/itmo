package ru.stud.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Vetoed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ru.stud.model.ResultHolder;
import ru.stud.model.UserEntity;

import java.util.List;

@ApplicationScoped
public class ResultRepository {
    @PersistenceContext(unitName = "PointsPU")
    private EntityManager em;

    @Transactional
    public ResultHolder save(ResultHolder r){
        em.persist(r);
        return r;
    }

    public List<ResultHolder> listByUser(UserEntity user){
        return em.createQuery("SELECT r FROM ResultHolder r WHERE r.user= :user ORDER BY r.time DESC", ResultHolder.class).setParameter("user",user).getResultList();
    }

    public List<ResultHolder> listAll(){
        return em.createQuery("SELECT r FROM ResultHolder r ORDER BY r.time DESC", ResultHolder.class).getResultList();
    }

    @Transactional
    public void clearAll(){
        em.createQuery("DELETE FROM ResultHolder").executeUpdate();
    }

    @Transactional
    public void clearByUser(UserEntity user){
        em.createQuery("DELETE FROM ResultHolder r WHERE r.user= :user").setParameter("user",user).executeUpdate();
    }
}
