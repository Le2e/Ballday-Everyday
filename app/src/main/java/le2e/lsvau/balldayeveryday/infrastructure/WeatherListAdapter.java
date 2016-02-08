package le2e.lsvau.balldayeveryday.infrastructure;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import le2e.lsvau.balldayeveryday.R;

public class WeatherListAdapter extends RecyclerView.Adapter<WeatherListViewHolder>
{
    private final ArrayList<WeatherDaily> weatherDailyArrayList;
    private Context context;

    public WeatherListAdapter(Context contextParam)
    {
        weatherDailyArrayList = new ArrayList<>();
        context = contextParam;
    }

    public void addWeatherItemToList(WeatherDaily wParam)
    {
        weatherDailyArrayList.add(wParam);
        notifyItemInserted(weatherDailyArrayList.size() - 1);
    }

    @Override
    public WeatherListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate((R.layout.list_item_weather), parent, false);
        return new WeatherListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherListViewHolder holder, int position) {
        String day = weatherDailyArrayList.get(position).dayOfWeek;
        String min = weatherDailyArrayList.get(position).temperatureMin;
        String max = weatherDailyArrayList.get(position).temperatureMax;
        String maxTime = weatherDailyArrayList.get(position).temperatureMaxTime;
        String minTime = weatherDailyArrayList.get(position).temperatureMinTime;
        String windSpeed = weatherDailyArrayList.get(position).windSpeed;
        String precipType = weatherDailyArrayList.get(position).precipType;
        String precipChance = weatherDailyArrayList.get(position).precipProbability;
        String summary = weatherDailyArrayList.get(position).summary;
        String icon = weatherDailyArrayList.get(position).iconRef;

        holder.conditionImageView.setImageResource(IconManager.getIconReference(icon));
        holder.dayTextView.setText(day);
        holder.lowTextView.setText("Low: " + min + "\u00B0F @ " + minTime);
        holder.hiTextView.setText("High: " + max + "\u00B0F @ " + maxTime);
        holder.windSpeedTextView.setText("Windspeed: " + windSpeed + "mph");
        holder.precipTypeTextView.setText(((precipType.equals("")) ? "Precipitation" : precipType) + " " + precipChance + "%");
        holder.summaryTextView.setText(summary);


        if(position % 2 == 0)
            holder.weather_list_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDarkest));
        else
            holder.weather_list_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark600));
    }

    @Override
    public int getItemCount() {
        return weatherDailyArrayList.size();
    }
}
