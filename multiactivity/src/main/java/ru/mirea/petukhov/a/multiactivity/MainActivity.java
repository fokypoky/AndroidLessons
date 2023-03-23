package ru.mirea.petukhov.a.multiactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String infoText;
    private final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClickNewActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("key", "MIREA - Петухов Антон Олегович");
        startActivity(intent);
    }

    public void sendMessageButtonClick(View view) {

        EditText editText = (EditText) findViewById(R.id.sendInfoEditText);
        String infoText = editText.getText().toString();

        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("key", infoText);

        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "MAIN ACTIVITY: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "MAIN ACTIVITY: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "MAIN ACTIVITY: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "MAIN ACTIVITY: onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "MAIN ACTIVITY: onRestart()");
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "MAIN ACTIVITY: onDestroy()");
        super.onDestroy();
    }
}