package le2e.lsvau.balldayeveryday.infrastructure;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import le2e.lsvau.balldayeveryday.R;

public class WeatherListViewHolder extends RecyclerView.ViewHolder
{
    public ImageView conditionImageView;
    public TextView dayTextView;
    public TextView lowTextView;
    public TextView hiTextView;
    public TextView summaryTextView;
    public LinearLayout weather_list_layout;
    public TextView windSpeedTextView;
    public TextView precipTypeTextView;


    public WeatherListViewHolder(View itemView){
        super(itemView);

        conditionImageView = (ImageView)itemView.findViewById(R.id.list_weather_icon);
        weather_list_layout = (LinearLayout)itemView.findViewById(R.id.weather_list_linear_layout);
        dayTextView = (TextView)itemView.findViewById(R.id.list_weather_day_text);
        lowTextView = (TextView)itemView.findViewById(R.id.list_weather_minTemp_text);
        hiTextView = (TextView)itemView.findViewById(R.id.list_weather_maxTemp_text);
        summaryTextView = (TextView)itemView.findViewById(R.id.list_weather_summary_text);
        windSpeedTextView = (TextView)itemView.findViewById(R.id.list_weather_windSpeed_text);
        precipTypeTextView = (TextView)itemView.findViewById(R.id.list_weather_precipType_text);
    }
}