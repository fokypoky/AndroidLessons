package ru.mirea.petukhov.a.favoritebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        extras = getIntent().getExtras();

        TextView header = (TextView) findViewById(R.id.developerFavBook);

        if(extras != null)
        {
            String bookTitle = extras.getString(MainActivity.KEY);
            header.setText("Любимая книга разработчика - " + bookTitle);
        }
    }

    public void onSendButtonClick(View view) {
        EditText editText = (EditText) findViewById(R.id.bookTitle);
        String bookTitle = editText.getText().toString();

        Intent intent = new Intent();
        intent.putExtra(MainActivity.USER_MESSAGE, bookTitle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}