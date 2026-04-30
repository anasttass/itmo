package ru.stud.monitoring;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

import javax.management.*;
import java.lang.management.ManagementFactory;

@Singleton
@Startup
public class MBeanRegister {

    @Inject
    private IntervalCalculator intervalCalculatorMBean;
    @Inject
    private Counter counterMBean;

    @PostConstruct
    private void register() {
        try{
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName counterName = new ObjectName("PointsMBean:type=Counter");
        ObjectName intervalName = new ObjectName("PointsMBean:type=IntervalCalculator");

        if (!mbs.isRegistered(counterName)){
            mbs.registerMBean(counterMBean, counterName);
        }
        if(!mbs.isRegistered(intervalName)){
            mbs.registerMBean(intervalCalculatorMBean,intervalName);
        }
        System.out.print("\n --------- MBeans was registered successfully! -------------");
        } catch (Exception e) {
            System.err.print("Error in registration of MBeans"+ e.getMessage());
        }
    }
}
