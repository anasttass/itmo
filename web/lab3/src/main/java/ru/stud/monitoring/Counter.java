package ru.stud.monitoring;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Named("Counter")
@ApplicationScoped
public class Counter extends NotificationBroadcasterSupport implements CounterMBean {
    private long allPoints=0;
    private long missedPoints=0;
    private long notification_number=1;

    @Override
    public void incrementAll() {
        this.allPoints+=1;
        if (allPoints%5==0){
            Notification notification = new Notification(
                    "weblab3.points.5Reached",
                    "Counter",
                    notification_number,
                    System.currentTimeMillis(),
                    "Проставлено 5 новых точек! Новая статистика: \nВсего точек: "+allPoints+"\nИз них "+missedPoints+" мимо области."
            );
            sendNotification(notification);
            notification_number++;

        }
    }

    @Override
    public void incrementMissed() {
        this.missedPoints+=1;
    }

    public long getAllPoints() {
        return allPoints;
    }

    public void setAllPoints(long allPoints) {
        this.allPoints = allPoints;
    }

    public long getMissedPoints() {
        return missedPoints;
    }

    public void setMissedPoints(long missedPoints) {
        this.missedPoints = missedPoints;
    }

    public long getNotification_number() {
        return notification_number;
    }

    public void setNotification_number(long notification_number) {
        this.notification_number = notification_number;
    }

}
