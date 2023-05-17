package ru.mirea.petukhov.a.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.petukhov.a.httpurlconnection.databinding.ActivityWeatherBinding;

public class WeatherActivity extends AppCompatActivity {
    private ActivityWeatherBinding binding;
    protected static JSONObject ipJson = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.showWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Bundle bundle = getIntent().getExtras();
                String data = bundle.getString("ip_info");
                if (data != null){
                    try {
                        ipJson = new JSONObject(data);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
//                        Toast.makeText(getBaseContext(), "Ошибка в полученом значении", Toast.LENGTH_LONG).show();
                    }
                }
                try {
                    binding.countryTextView.setText("Страна: " + ipJson.getString("country"));
                    binding.regionTextView.setText("Регион: " + ipJson.getString("region"));
                    binding.cityTextView.setText("Город: " + ipJson.getString("city"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                if(ipJson != null){
                    String[] geo;
                    try {
                        geo = ipJson.getString("loc").split(",");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    new DownloadWeatherData().execute(String.format(
                            "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true",
                            geo[0], geo[1]
                    ));
                }
            }
        });

    }
    private class DownloadWeatherData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            binding.weatherTextView.setText("Загружаю, подожди");
        }
        @Override
        protected void onPostExecute(String result){
            //binding.ipTextView.setText(result);
            Log.d(WeatherActivity.class.getSimpleName(), result);
            try{
                JSONObject responseJson = new JSONObject(result);
                JSONObject weather = responseJson.getJSONObject("current_weather");

                //Log.wtf(WeatherActivity.class.getSimpleName(), "Response: " + responseJson);

                //Log.wtf(WeatherActivity.class.getSimpleName(), "WEATHER: " + weather);
                double temperature = weather.getDouble("temperature");
                binding.weatherTextView.setText(String.valueOf("Температура: " + temperature));
            }
            catch (JSONException e){
                throw new RuntimeException(e);
            }
            super.onPostExecute(result);
        }
        @Override
        protected String doInBackground(String... urls){
            try{
                return downloadIpInfo(urls[0]);
            }
            catch (IOException e){
                e.printStackTrace();
                return "error";
            }
        }

        private String downloadIpInfo(String address) throws IOException{
            InputStream inputStream = null;
            String data = "";

            try{
                URL url = new URL(address);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);

                int responseCode = connection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){

                    inputStream = connection.getInputStream();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                    int read = 0;

                    while ((read = inputStream.read()) != -1){
                        outputStream.write(read);
                    }

                    outputStream.close();

                    data = outputStream.toString();
                }
                else{
                    data = connection.getResponseMessage() + ". Error code: " + responseCode;
                }

                connection.disconnect();
            }
            catch (IOException e){
                //e.printStackTrace();
                throw new RuntimeException(e);
            }
            finally {
                if (inputStream != null){
                    inputStream.close();
                }
            }

            return data;
        }

    }

}