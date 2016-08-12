package le2e.lsvau.balldayeveryday.infrastructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherParse
{
    String weatherAsJSON;

    // populated containers with collected weather data
    public ArrayList<WeatherDaily> weatherDailyArrayList;
    public ArrayList<WeatherHourly> weatherHourlyArrayList;
    public WeatherCurrentDay weatherCurrentDay;

    // private JSON field titles
    private String time = "time";
    private String icon = "icon";
    private String summary = "summary";
    private String precipProbability = "precipProbability";
    private String humidity = "humidity";
    private String windSpeed = "windSpeed";
    private String currentTemp = "temperature";
    private String apparentTemp = "apparentTemperature";
    private String sunriseTime = "sunriseTime";
    private String sunsetTime = "sunsetTime";
    private String precipType = "precipType";
    private String temperatureMin = "temperatureMin";
    private String temperatureMinTime = "temperatureMinTime";
    private String temperatureMax = "temperatureMax";
    private String temperatureMaxTime = "temperatureMaxTime";


    public WeatherParse(String s)
    {
        weatherAsJSON = s;
        weatherDailyArrayList = new ArrayList<>();
        weatherHourlyArrayList = new ArrayList<>();
    }

    public void ParseHourlyWeatherJSONArray()
    {
        int size;
        WeatherDaily weatherDaily;
        WeatherHourly weatherHourly;
        try
        {
            JSONObject parentWeatherObject = new JSONObject(weatherAsJSON);
            if(parentWeatherObject.has("hourly")) {
                JSONObject dailyWeatherObject = parentWeatherObject.getJSONObject("hourly");
                if (dailyWeatherObject.has("data")) {
                    JSONArray dailyArray = dailyWeatherObject.getJSONArray("data");

                    size = dailyArray.length();
                    for (int i = 0; i < size; ++i) {
                        weatherHourly = new WeatherHourly();
                        JSONObject object = dailyArray.getJSONObject(i);

                        weatherHourly.dayOfWeek = weatherHourly.setDayOfWeek(getJSONLongParamter(object, time), "E - H:mm");
                        weatherHourly.iconRef = getJSONStringParameter(object, icon);
                        weatherHourly.summary = getJSONStringParameter(object, summary);
                        weatherHourly.currentTemp = getJSONStringParameter(object, currentTemp);
                        weatherHourly.apparentTemp = getJSONStringParameter(object, apparentTemp);

                        String type = getJSONStringParameter(object, precipType);
                        if(type.length() > 0)
                            weatherHourly.precipType = type.substring(0,1).toUpperCase() + type.substring(1);
                        else
                            weatherHourly.precipType = "";

                        weatherHourly.precipProbability = weatherHourly.setPercentage(getJSONDoubleParamter(object, precipProbability));
                        weatherHourly.windSpeed = getJSONStringParameter(object, windSpeed);
                        weatherHourlyArrayList.add(weatherHourly);
                    }
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ParseCurrentWeatherJSONArray()
    {
        try
        {
            JSONObject parentWeatherObject = new JSONObject(weatherAsJSON);
            if(parentWeatherObject.has("currently")) {
                JSONObject currentWeatherObject = parentWeatherObject.getJSONObject("currently");

                weatherCurrentDay = new WeatherCurrentDay();
                weatherCurrentDay.dayOfWeek = weatherCurrentDay.setDayOfWeek(getJSONLongParamter(currentWeatherObject, time), "EEEE - M/d");
                weatherCurrentDay.iconRef = getJSONStringParameter(currentWeatherObject, icon);
                weatherCurrentDay.summary = getJSONStringParameter(currentWeatherObject, summary);
                weatherCurrentDay.precipProbability = weatherCurrentDay.setPercentage(getJSONDoubleParamter(currentWeatherObject, precipProbability));

                String type = getJSONStringParameter(currentWeatherObject, precipType);
                if(type.length() > 0)
                    weatherCurrentDay.precipType = type.substring(0,1).toUpperCase() + type.substring(1);
                else
                    weatherCurrentDay.precipType = "";

                weatherCurrentDay.windSpeed = getJSONStringParameter(currentWeatherObject, windSpeed);
                weatherCurrentDay.humidity = weatherCurrentDay.setPercentage(getJSONDoubleParamter(currentWeatherObject, humidity));
                weatherCurrentDay.currentTemp = getJSONStringParameter(currentWeatherObject, currentTemp);
                weatherCurrentDay.apparentTemp = getJSONStringParameter(currentWeatherObject, apparentTemp);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            int i = 0;
            ++i;
        }
    }

    public void ParseDailyWeatherJSONArray()
    {
        int size;
        WeatherDaily weatherDaily;
        try
        {
            JSONObject parentWeatherObject = new JSONObject(weatherAsJSON);
            if(parentWeatherObject.has("daily")) {
                JSONObject dailyWeatherObject = parentWeatherObject.getJSONObject("daily");
                if (dailyWeatherObject.has("data")) {
                    JSONArray dailyArray = dailyWeatherObject.getJSONArray("data");

                    size = dailyArray.length();
                    for (int i = 0; i < size; ++i) {
                        weatherDaily = new WeatherDaily();
                        JSONObject object = dailyArray.getJSONObject(i);

                        weatherDaily.dayOfWeek = weatherDaily.setDayOfWeek(getJSONLongParamter(object, time), "E - M/d");
                        weatherDaily.iconRef = getJSONStringParameter(object, icon);
                        weatherDaily.summary = getJSONStringParameter(object, summary);

                        String type = getJSONStringParameter(object, precipType);
                        if(type.length() > 0)
                            weatherDaily.precipType = type.substring(0,1).toUpperCase() + type.substring(1);
                        else
                            weatherDaily.precipType = "";

                        weatherDaily.precipProbability = weatherDaily.setPercentage(getJSONDoubleParamter(object, precipProbability));
                        weatherDaily.temperatureMax = getJSONStringParameter(object, temperatureMax);
                        weatherDaily.temperatureMaxTime = weatherDaily.setTimeOfDay(getJSONLongParamter(object, temperatureMaxTime), "H:mm");
                        weatherDaily.temperatureMin = getJSONStringParameter(object, temperatureMin);
                        weatherDaily.temperatureMinTime = weatherDaily.setTimeOfDay(getJSONLongParamter(object, temperatureMinTime), "H:mm");

                        weatherDaily.windSpeed = getJSONStringParameter(object, windSpeed);
                        weatherDailyArrayList.add(weatherDaily);
                    }
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private double getJSONDoubleParamter(JSONObject object, String param)
    {
        double result = 0.0;
        try
        {
            result = object.has(param) ? object.getDouble(param) : 0.0;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            return result;
        }
    }

    private String getJSONStringParameter(JSONObject object, String param)
    {
        String result = "";
        try
        {
            result = object.has(param) ? object.getString(param) : "";
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            return result;
        }
    }

    private long getJSONLongParamter(JSONObject object, String param)
    {
        long result = 0;
        try
        {
            result = object.has(param) ? object.getLong(param) : 0;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            return result;
        }
    }
}
