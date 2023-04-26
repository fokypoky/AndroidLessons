package ru.mirea.petukhov.a.looper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.os.Handler;

import ru.mirea.petukhov.a.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    MyLooper looper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler mainThreadHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message){
                Log.d(
                        MainActivity.class.getSimpleName(),
                        "Task execute. This is result:" +
                                message.getData().getString("result")
                );
            }
        };

        looper = new MyLooper(mainThreadHandler);
        looper.start();

        binding.studentNumberTextView.setText("Мой номер по списку №13");

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("KEY", "mirea");
                message.setData(bundle);
                looper.mHandler.sendMessage(message);
            }
        });
        binding.inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String position = binding.positionEditText.getText().toString();
                int age;
                try{
                    age = Integer.parseInt(binding.ageEditText.getText().toString());
                }
                catch(Exception e){
                    age = 0;
                }
/*

                String age = binding.ageEditText.getText().toString();
                String position = binding.positionEditText.getText().toString();

*/
                Message message = Message.obtain();
                Bundle bundle = new Bundle();

                bundle.putString("KEY", position);
                bundle.putString("AGE", Integer.toString(age));

                message.setData(bundle);

                looper.mHandler.sendMessage(message);
            }
        });
    }
}