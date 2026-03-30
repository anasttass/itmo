package ru.stud.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ru.stud.model.ResultHolder;
import ru.stud.model.UserEntity;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class UserRepository  {

    @PersistenceContext(unitName = "PointsPU")
    private EntityManager em;

    @Transactional
    public UserEntity save(UserEntity r){
        em.persist(r);
        return r;
    }

    @Transactional
    public void clearUsers(){
        em.createQuery("DELETE FROM UserEntity").executeUpdate();
    }

    public Optional<UserEntity> findByUsername(String username){
        List<UserEntity> result = em.createQuery(
                        "SELECT u FROM UserEntity u WHERE u.login = :login",
                        UserEntity.class)
                .setParameter("login", username)
                .getResultList();

        return result.stream().findFirst();
    }

    public Optional<UserEntity> findById(long id){
        return em.createQuery("SELECT u FROM UserEntity u WHERE u.id = :id",UserEntity.class).setParameter("id",id).getResultList().stream().findFirst();
    }

}
