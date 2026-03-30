package ru.stud.backend.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.stud.backend.model.ContextParams;
import ru.stud.backend.service.RequestStats;
@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute(ContextParams.REQUESTSTATS.getKey(), new RequestStats());
    }
}
