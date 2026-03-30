package ru.stud.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Vetoed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ru.stud.model.ResultHolder;

import java.util.List;

@ApplicationScoped
@Jpa
public class ResultRepository implements IResultRepository { //сделать интерфейсом, добавить jooq реализацию. выбор зависит от переменной окружения при страте. в логах прописать это
    @PersistenceContext(unitName = "PointsPU")
    private EntityManager em;

    @Transactional
    public ResultHolder save(ResultHolder r){
        em.persist(r);
        return r;
    }

    public List<ResultHolder> listAll(){
        return em.createQuery("SELECT r FROM ResultHolder r ORDER BY r.time DESC", ResultHolder.class).getResultList();
    }

    @Transactional
    public void clearAll(){
        em.createQuery("DELETE FROM ResultHolder").executeUpdate();
    }
}
