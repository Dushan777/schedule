package org.model;

public class ScheduleManager {

    private static ScheduleSpecification scheduleSpecification;
    public static void setScheduleSpecification(ScheduleSpecification scheduleSpecification) {
        ScheduleManager.scheduleSpecification = scheduleSpecification;
    }
    public static ScheduleSpecification getScheduleSpecification() {
        return scheduleSpecification;
    }
}
