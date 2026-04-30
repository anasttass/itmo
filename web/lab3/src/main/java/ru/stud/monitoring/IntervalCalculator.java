package ru.stud.monitoring;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Named("IntervalCalculator")
@ApplicationScoped
public class IntervalCalculator extends NotificationBroadcasterSupport implements IntervalCalculatorMBean {
    private long all_time=0;
    private long last_time=0;
    private long amount=0;
    private long notification_number=1;
    private double current_interval;
    private static final List<IntervalStats> logging_string = new ArrayList<>();

    @Override
    public void calculateInterval() {
        long new_time = System.currentTimeMillis();
        if (last_time!=0){
            all_time+=new_time-last_time;
            amount++;
            current_interval=(double) (all_time/amount)/ 1000.0;
            Notification notification = new Notification(
                    "weblab3.points.intervalsUpdate",
                    "IntervalCalculator",
                    notification_number,
                    new_time,
                    "Обновление интервала между кликами! Новое значение: "+current_interval
            );
            notification_number++;
            sendNotification(notification);

            IntervalStats stats = new IntervalStats(current_interval);
            logging_string.add(stats);
        }
        last_time=new_time;
    }

    public double getCurrent_interval() {
        return current_interval;
    }

    public void setCurrent_interval(long current_interval) {
        this.current_interval = current_interval;
    }

    public void addClick(){
        this.amount++;
    }

    public long getLast_time() {
        return last_time;
    }

    public void setLast_time(long last_time) {
        this.last_time = last_time;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getAll_time() {
        return all_time;
    }

    public void setAll_time(long all_time) {
        this.all_time = all_time;
    }

    public long getNotification_number() {
        return notification_number;
    }

    public void setNotification_number(long notification_number) {
        this.notification_number = notification_number;
    }
}
