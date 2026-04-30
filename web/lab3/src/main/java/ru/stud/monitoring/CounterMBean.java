package ru.stud.monitoring;

public interface CounterMBean {
    void incrementAll();
    void incrementMissed();
    long getMissedPoints();
    long getAllPoints();
}
