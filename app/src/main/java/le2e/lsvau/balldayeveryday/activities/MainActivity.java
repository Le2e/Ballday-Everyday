package le2e.lsvau.balldayeveryday.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import java.util.ArrayList;

import le2e.lsvau.balldayeveryday.R;
import le2e.lsvau.balldayeveryday.infrastructure.JSON_URL_Adapter;
import le2e.lsvau.balldayeveryday.infrastructure.MyLocation;
import le2e.lsvau.balldayeveryday.infrastructure.WeatherDaily;
import le2e.lsvau.balldayeveryday.infrastructure.WeatherListAdapter;
import le2e.lsvau.balldayeveryday.infrastructure.WeatherParse;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {
    private RecyclerView recyclerView;
    private WeatherListAdapter adapter;
    private GoogleApiClient mGoogleApiClient;
    private Location lastLocation;
    private WeatherParse weatherParse;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WeatherListAdapter(this);
        recyclerView.setAdapter(adapter);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
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

        new GetWeatherTask().execute(finalUrl);
    }

    // *************************************************************
    // ******************** ASYNCTASK Functions ********************
    // *************************************************************

    public class GetWeatherTask extends AsyncTask<String, String, ArrayList<WeatherDaily>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Loading WeatherDaily");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected ArrayList<WeatherDaily> doInBackground(String... params) {
            String weatherAsJSON = JSON_URL_Adapter.DownloadJSON_FromURL(params[0]);
            if(weatherAsJSON != null)
            {
                weatherParse = new WeatherParse(weatherAsJSON);
                weatherParse.ParseDailyWeatherJSONArray();

                return weatherParse.weatherDailyArrayList;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<WeatherDaily> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            for(int i = 0; i < result.size(); ++i)
            {
                adapter.addWeatherItemToList(result.get(i));
            }
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
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(lastLocation != null) {
            displayWeatherReport(lastLocation.getLongitude(), lastLocation.getLatitude());
        }
        else
        {
            Toast.makeText(MainActivity.this, "Failed to find device Location. Make sure GPS/Location is enabled.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    // **********************************************************************
    // ******************** Activity Lifecycle Functions ********************
    // **********************************************************************

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }
}
