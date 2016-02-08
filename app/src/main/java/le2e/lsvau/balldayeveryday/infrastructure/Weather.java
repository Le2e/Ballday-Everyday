package le2e.lsvau.balldayeveryday.infrastructure;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class Weather
{
    public String dayOfWeek;
    public String summary;
    public String iconRef;
    public String precipProbability;
    public String precipType;
    public String humidity;
    public String windSpeed;

    public String setDayOfWeek(long time, String format)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);
        return formatter.format(calendar.getTime());
    }

    public String setPercentage(double number)
    {
        return Double.toString(number * 100);
    }

    public String setTimeOfDay(long time, String format)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);
        return formatter.format(calendar.getTime());
    }
}
