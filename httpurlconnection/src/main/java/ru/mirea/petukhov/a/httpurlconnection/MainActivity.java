package ru.mirea.petukhov.a.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.petukhov.a.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public static boolean canShowWeatherButtonClick = false;
    protected static String ipInfoString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.getIpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo netInfo = null;

                if(conManager != null){
                    netInfo = conManager.getActiveNetworkInfo();
                }
                if(netInfo != null && netInfo.isConnected()){
                    new DownloadPageTask().execute("https://ipinfo.io/json");
                }
                else{
                    Toast.makeText(getBaseContext(), "Нет интернета", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.showWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canShowWeatherButtonClick){
                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                    intent.putExtra("ip_info", ipInfoString);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getBaseContext(), "Данные не загружены", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class DownloadPageTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            binding.ipTextView.setText("Загружаю, подожди");
        }
        @Override
        protected void onPostExecute(String result){
            Log.d(MainActivity.class.getSimpleName(), result);
            try{
                JSONObject responseJson = new JSONObject(result);
                Log.d(MainActivity.class.getSimpleName(), "Response: " + responseJson);
                binding.ipTextView.setText(responseJson.getString("ip"));
                Log.d(MainActivity.class.getSimpleName(), "IP: " + responseJson.getString("ip"));
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            MainActivity.ipInfoString = result;
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
                MainActivity.canShowWeatherButtonClick = true;
            }
            catch (IOException e){
                e.printStackTrace();
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