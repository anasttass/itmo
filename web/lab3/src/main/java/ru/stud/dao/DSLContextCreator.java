package ru.stud.dao;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@ApplicationScoped
public class DSLContextCreator {

    private DataSource dataSource;

    @PostConstruct
    public void init() {
        try {
            this.dataSource = (DataSource) new InitialContext().lookup("java:/PostgresDS");
        } catch (NamingException e) {
            throw new IllegalStateException("Не получилось найти java:/PostgresDS", e);
        }
    }

    @Produces
    @ApplicationScoped
    public DSLContext createDSLContext() {
        if (dataSource == null) {
            throw new IllegalStateException("Датасорс не получен");
        }
        return DSL.using(dataSource, SQLDialect.POSTGRES);
    }
}