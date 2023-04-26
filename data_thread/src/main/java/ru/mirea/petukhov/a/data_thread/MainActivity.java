package ru.mirea.petukhov.a.data_thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import ru.mirea.petukhov.a.data_thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setDifferenceInfo();

        final Runnable runn1 = new Runnable() {
            @Override
            public void run() {
                binding.threadTextView.setText("runn1");
            }
        };
        final Runnable runn2 = new Runnable() {
            @Override
            public void run() {
                binding.threadTextView.setText("runn2");
            }
        };
        final Runnable runn3 = new Runnable() {
            @Override
            public void run() {
                binding.threadTextView.setText("runn3");
            }
        };
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    TimeUnit.SECONDS.sleep(2);
                    runOnUiThread(runn1);
                    TimeUnit.SECONDS.sleep(1);
                    binding.threadTextView.postDelayed(runn3, 2000);
                    binding.threadTextView.post(runn2);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }
    private void setDifferenceInfo(){
        binding.differenceInfoTextView.setText("1. runOnUiThread Запускает указанное действие в потоке пользовательского интерфейса. Если текущий поток является потоком пользовательского интерфейса, то действие выполняется немедленно. Если текущий поток не является потоком пользовательского интерфейса, действие отправляется в очередь событий потока пользовательского интерфейса");
        binding.differenceInfoTextView.append("\n\n2. postDelayed Вызывает добавление Runnable r в очередь сообщений для запуска по истечении указанного времени. Runnable будет запущен в потоке, к которому прикреплен этот обработчик");
        binding.differenceInfoTextView.append("\n\n3. post Вызывает добавление Runnable в очередь сообщений. Runnable будет запущен в потоке, к которому прикреплен этот обработчик");
    }
}