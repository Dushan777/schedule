package org.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
@Getter
@Setter
public class Time {

    /*
    Vreme se prilikom ovih provera može zadavati na dva načina, prvi je
    zadavanje tačnog datuma, a drugi je zadavanje dana u nedelji i perioda (na primer da li je slobodan
    termin sredom 10-12h u periodu od 1.10.2023. do 1.12.2023). Termini se mogu zadavati kao vreme
    početka i završetka ili kao vreme početka i trajanje.
     */
    /*
    String dateStr = "2023-10-26";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateStr, formatter);
     */
    private LocalDate startDate;
    private LocalDate endDate;

    // date je samo datum, localtime je samo vreme, a localdatetime je i datum i vreme
    private LocalTime startTime;
    private LocalTime endTime;

    // za prvu impl. startDateTime i endDate ce biti isti
    @JsonCreator
    public Time(@JsonProperty("startDate") LocalDate startDate, @JsonProperty("endDate")LocalDate endDate, @JsonProperty("startTime")LocalTime startTime, @JsonProperty("endTime")LocalTime endTime) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public Time(LocalDate startDate, LocalDate endDate, LocalTime startTime, int durationInMinutes) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(durationInMinutes);
    }
    public static String getWeekDay(LocalDate date) //TODO: moze string pa da se prebaci u date u metodi
    {
        int num = date.getDayOfWeek().getValue();
        String dayOfTheWeek = null;
        switch (num)
        {
            case 1: dayOfTheWeek = "Monday";
                break;
            case 2: dayOfTheWeek = "Tuesday";
                break;
            case 3: dayOfTheWeek = "Wednesday";
                break;
            case 4: dayOfTheWeek = "Thursday";
                break;
            case 5: dayOfTheWeek = "Friday";
                break;
            case 6: dayOfTheWeek = "Saturday";
                break;
            case 7: dayOfTheWeek = "Sunday";
                break;

        }
        return dayOfTheWeek;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Time))
            return false;
        Time time = (Time) obj;
        return this.startDate.equals(time.startDate) && this.endDate.equals(time.endDate) && this.startTime.equals(time.startTime) && this.endTime.equals(time.endTime);
    }

    @Override
    public String toString() {
        return "Time{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
    public boolean hasNULL()
    {
        return startDate == null || endDate == null || startTime == null || endTime == null;
    }

}
