package ru.stud.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.enterprise.inject.Produces;


@ApplicationScoped
public class RepositoryChooser {

    @Inject @Jpa
    private IResultRepository jpaRepo;

    @Inject @Jooq
    private IResultRepository jooqRepo;

    @Produces
    @ApplicationScoped
    public IResultRepository getRepository() {
        String type = System.getProperty("REPOTYPE");
        if ("jooq".equalsIgnoreCase(type)) {
            System.out.println("Using JOOQ repo");
            return jooqRepo;
        } else if ("jpa".equalsIgnoreCase(type)) {
            System.out.println("Using JPA repo");
            return jpaRepo;
        }
        System.out.println("Error in enviromental value. JPA is used as a default");
        System.out.println(type);
        return jpaRepo;
    }
}
