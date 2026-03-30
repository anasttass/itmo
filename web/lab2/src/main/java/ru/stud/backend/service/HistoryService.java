package ru.stud.backend.service;

import ru.stud.backend.model.Point;
import java.util.List;

public interface HistoryService {
    void addPoint(Point point);
    List<Point> getAllPoints();
}
