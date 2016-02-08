package le2e.lsvau.balldayeveryday.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import le2e.lsvau.balldayeveryday.R;
import le2e.lsvau.balldayeveryday.infrastructure.IconManager;
import le2e.lsvau.balldayeveryday.infrastructure.JSON_URL_Adapter;
import le2e.lsvau.balldayeveryday.infrastructure.WeatherCurrentDay;
import le2e.lsvau.balldayeveryday.infrastructure.WeatherParse;

public class CurrentForecastActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener
{
    private GoogleApiClient cGoogleApiClient;

    ProgressDialog progressDialog;

    // Layout view references
    TextView dayTextView;
    TextView actualTempTextView;
    TextView feelsLikeTextView;
    TextView humidityTextView;
    TextView rainChanceTextView;
    TextView windSpeedTextView;
    TextView summaryTextView;
    ImageView iconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_forecast);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Layout view references
        dayTextView = (TextView)findViewById(R.id.current_weather_day_text);
        actualTempTextView = (TextView)findViewById(R.id.current_weather_actualTemp_text);
        feelsLikeTextView = (TextView)findViewById(R.id.current_weather_apparentTemp_text);
        humidityTextView = (TextView)findViewById(R.id.current_weather_humidity_text);
        rainChanceTextView = (TextView)findViewById(R.id.current_weather_rainChance_text);
        windSpeedTextView = (TextView)findViewById(R.id.current_weather_windSpeed_text);
        summaryTextView = (TextView)findViewById(R.id.current_weather_summary_text);
        iconImageView = (ImageView)findViewById(R.id.current_weather_image_view);

        if (cGoogleApiClient == null) {
            cGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void displayWeatherReport(double longi, double lati) {
        // Formulate connection string
        String latLong = Double.toString(lati) + "," + Double.toString(longi);
        String url1 = getString(R.string.web_service_url);
        String apiKey = getString(R.string.api_key);
        String finalUrl = url1 + apiKey + "/" + latLong;

        new GetCurrentWeatherTask().execute(finalUrl);
    }

    // *************************************************************
    // ******************** ASYNCTASK Functions ********************
    // *************************************************************

    public class GetCurrentWeatherTask extends AsyncTask<String, String, WeatherCurrentDay>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CurrentForecastActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Loading WeatherDaily");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected WeatherCurrentDay doInBackground(String... params)
        {
            WeatherParse weatherParse;
            String weatherAsJSON = JSON_URL_Adapter.DownloadJSON_FromURL(params[0]);
            if(weatherAsJSON != null)
            {
                weatherParse = new WeatherParse(weatherAsJSON);
                weatherParse.ParseCurrentWeatherJSONArray();

                return weatherParse.weatherCurrentDay;
            }
            return null;
        }

        @Override
        protected void onPostExecute(WeatherCurrentDay result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            iconImageView.setImageResource(IconManager.getIconReference(result.iconRef));
            dayTextView.setText(result.dayOfWeek);
            actualTempTextView.setText("Actual: " + result.currentTemp + "\u00B0F");
            feelsLikeTextView.setText("Feels like: " + result.apparentTemp + "\u00B0F");
            humidityTextView.setText("Humidity " + result.humidity + "%");
            rainChanceTextView.setText(((result.precipType.equals("")) ? "Precipitation" : result.precipType) + " " + result.precipProbability + "%");
            windSpeedTextView.setText("Windspeed: " + result.windSpeed + "mph");
            summaryTextView.setText(result.summary);
        }
    }

    // **************************************************************
    // ******************** GooglePlay Functions ********************
    // **************************************************************

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation;
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(cGoogleApiClient);
        if(lastLocation != null) {
            displayWeatherReport(lastLocation.getLongitude(), lastLocation.getLatitude());
        }
        else
        {
            Toast.makeText(CurrentForecastActivity.this, "Failed to find device Location. Make sure GPS/Location is enabled.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Toast.makeText(CurrentForecastActivity.this, "WTF", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(CurrentForecastActivity.this, "WTF", Toast.LENGTH_SHORT).show();
    }

    // **********************************************************************
    // ******************** Activity Lifecycle Functions ********************
    // **********************************************************************

    @Override
    protected void onStart() {
        cGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        cGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }
}
