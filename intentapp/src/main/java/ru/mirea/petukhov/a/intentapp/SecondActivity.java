package ru.mirea.petukhov.a.intentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        String text = "КВАДРАТ ЗНАЧЕНИЯ МОЕГО НОМЕРА ПО СПИСКУ В ГРУППЕ СОСТАВЛЯЕТ 169, "
                + "а текущее время " + (getIntent()).getSerializableExtra("date").toString();

        TextView textView = (TextView) findViewById(R.id.infoTextView);
        textView.setText(text);
    }
}