package ru.mirea.petukhov.a.timeservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;

import ru.mirea.petukhov.a.timeservice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final String host = "time.nist.gov";
    private final int port = 13;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTimeTask timeTask = new GetTimeTask();
                timeTask.execute();
            }
        });
    }

    private class GetTimeTask extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params){
            String timeResult = "";
            try{
                Socket socket = new Socket(host, port);
                BufferedReader reader = SocketUtils.getReader(socket);
                reader.readLine();
                timeResult = reader.readLine();
                Log.d(MainActivity.class.getSimpleName(), timeResult);
                socket.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return timeResult;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            //LocalDate date = LocalDate.parse(result);
            String[] subStrings = result.split(" ");
            binding.timeTextView.setText(result);
            binding.receivedDateTextView.setText(subStrings[1]);
            binding.receivedTimeTextView.setText(subStrings[2]);
        }
    }
}