package ru.stud.service;

import jakarta.inject.Inject;
import ru.stud.model.DtoResultHolder;
import ru.stud.monitoring.CounterMBean;
import ru.stud.monitoring.IntervalCalculatorMBean;

public class MonitorManager {
    @Inject
    private CounterMBean counter;
    @Inject
    private IntervalCalculatorMBean intervalCalculator;

    public void prossesMonitoring(DtoResultHolder dto){

        counter.incrementAll();
        if (!dto.getHit()){
            counter.incrementMissed();
        }

        intervalCalculator.calculateInterval();
    }
}
