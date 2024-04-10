package com.example.weatherapi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText latitudeEditText, longitudeEditText;
    private Button calculateButton;
    private TextView resultTextView;

    private static final String API_KEY = "7f4eb2e88d74b1e04bc3d665dcbcda24";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);
        calculateButton = findViewById(R.id.calculateButton);
        resultTextView = findViewById(R.id.resultTextView);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latitude = latitudeEditText.getText().toString();
                String longitude = longitudeEditText.getText().toString();

                if (!latitude.isEmpty() && !longitude.isEmpty()) {
                    new FetchWeatherTask().execute(latitude, longitude);
                }
            }
        });
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, Double> {

        @Override
        protected Double doInBackground(String... params) {
            String latitude = params[0];
            String longitude = params[1];

            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" +
                        latitude + "&lon=" + longitude + "&appid=" + API_KEY);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                    JSONObject wind = jsonObject.getJSONObject("wind");
                    return wind.getDouble("speed");
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Double windSpeed) {
            if (windSpeed != null) {
                double powerOutput = WindTurbinePowerCalculator.calculatePowerOutput(windSpeed);
                resultTextView.setText("Prędkość wiatru: " + windSpeed + " m/s\nProdukcja prądu: " + powerOutput + " kW");
            } else {
                resultTextView.setText("Błąd pobierania danych o pogodzie");
            }
        }
    }
}