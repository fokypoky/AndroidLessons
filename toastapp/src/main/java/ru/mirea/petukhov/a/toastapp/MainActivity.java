package ru.mirea.petukhov.a.toastapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void tapButtonClick(View view) {
        EditText text = (EditText) findViewById(R.id.editTextTextPersonName);
        int charCount = text.getText().length();

        Toast toast = Toast.makeText(getApplicationContext(),
                "Студент № 13 Группа БСБО-17-20 Количество символов " + Integer.toString(charCount),
                Toast.LENGTH_LONG);
        toast.show();
    }
}