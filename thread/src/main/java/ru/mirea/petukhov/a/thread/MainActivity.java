package ru.mirea.petukhov.a.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import ru.mirea.petukhov.a.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setThreadInfoTextView();

        binding.btnColculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            int daysCount = Integer.parseInt(binding.daysEditText.getText().toString());
                            int lessonsCount = Integer.parseInt(binding.lessonEditText.getText().toString());
                            int result = lessonsCount / daysCount;

                            binding.resultTextView.setText("СРЕДНЕЕ КОЛ-ВО ПАР В ДЕНЬ = " + result);

                        }catch (Exception e){
                            binding.resultTextView.setText("INPUT ERROR");
                        }
                    }
                }).start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int numberThread = counter++;
                        Log.d(
                                MainActivity.class.getSimpleName(),
                                String.format(
                                        "Запущен поток №%d студентом группы №%s номер по списку № 13",
                                        numberThread, "БСБО-17-20", -1
                                )
                        );


                        long endTime = System.currentTimeMillis() + 20 * 1000;

                        while (System.currentTimeMillis() < endTime) {
                            synchronized (this){
                                try {
                                    wait(endTime - System.currentTimeMillis());
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            Log.d(MainActivity.class.getSimpleName(),
                                    "Выполнен поток №" + numberThread);
                        }
                    }
                }).start();
            }
        });


    }

    private void setThreadInfoTextView(){
        Thread mainThread = Thread.currentThread();

        TextView infoTextView = binding.threadInfoTextView;
        infoTextView.setText("Имя текущего потока: " + mainThread.getName());

        mainThread.setName("МОЙ НОМЕР ГРУППЫ БСБО-17-20, НОМЕР ПО СПИСКУ: 13, МОЙ ЛЮБИМЫЙ ФИЛЬМ: В.К. - Братство кольца");

        infoTextView.append("\n Новое имя потока: " + mainThread.getName());

        Log.d(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));
    }

}