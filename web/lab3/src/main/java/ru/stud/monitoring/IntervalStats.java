package ru.stud.monitoring;

import ru.stud.model.DtoResultHolder;

import java.io.Serializable;
import java.time.LocalDateTime;

public class IntervalStats implements Serializable {
    private LocalDateTime time;
    private double current_interval;
    private String info;
    DtoResultHolder point;
    private byte[] debugPayload = new byte[1024 * 1024];
    public IntervalStats(double interval) {
        this.time = LocalDateTime.now();
        this.current_interval = interval;
        this.info = "ОБНОВЛЕНИЕ ИНТЕРВАЛА. Новое значение интервала равно"+interval+"MESSAGE".repeat(1000);
        java.util.Arrays.fill(debugPayload, (byte) 1);
    }


}
