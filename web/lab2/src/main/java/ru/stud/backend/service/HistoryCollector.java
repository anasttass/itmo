package ru.stud.backend.service;

import jakarta.servlet.ServletContext;
import ru.stud.backend.model.ContextParams;
import ru.stud.backend.model.Point;

import java.util.ArrayList;
import java.util.List;

public class HistoryCollector implements HistoryService{

    private final ServletContext context;

    public HistoryCollector(ServletContext context) {
        this.context = context;
    }

    @Override
    public void addPoint(Point point) {
        List<Point> results = (List<Point>) context.getAttribute(ContextParams.RESULTS.getKey());
        if (results == null) {
            results = new ArrayList<>();
            context.setAttribute(ContextParams.RESULTS.getKey(), results);
        }
        results.add(point);
    }

    @Override
    public List<Point> getAllPoints() {
        List<Point> results = (List<Point>) context.getAttribute(ContextParams.RESULTS.getKey());
        return results != null ? results : new ArrayList<>();
    }
}
