package org.model;

import lombok.Getter;
import lombok.Setter;

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
    String sDate1="31/12/1998";
    Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
    LocalTime time = LocalTime.parse("12:34:45");
     */
    private Date startDate;
    private Date endDate;

    // date je samo datum, localtime je samo vreme, a localdatetime je i datum i vreme
    private LocalTime startTime;
    private LocalTime endTime;
    //TODO: vrv jos nesto


    public Time(Date startDate, Date endDate, LocalTime startTime, LocalTime endTime) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getWeekDay(Date date) //TODO: moze string pa da se prebaci u date u metodi
    {
        int num = date.getDay();
        String dayOfTheWeek = null;
        switch (num)
        {
            case 0: dayOfTheWeek = "Sunday"; break;
            case 1: dayOfTheWeek = "Monday"; break;
            case 2: dayOfTheWeek = "Tuesday"; break;
            case 3: dayOfTheWeek = "Wednesday"; break;
            case 4: dayOfTheWeek = "Thursday"; break;
            case 5: dayOfTheWeek = "Friday"; break;
            case 6: dayOfTheWeek = "Saturday"; break;

        }
        return dayOfTheWeek;
    }

    @Override
    public boolean equals(Object obj) {
        Time time = (Time) obj;
        return this.startDate.equals(time.startDate) && this.endDate.equals(time.endDate) && this.startTime.equals(time.startTime) && this.endTime.equals(time.endTime);
    }
}
