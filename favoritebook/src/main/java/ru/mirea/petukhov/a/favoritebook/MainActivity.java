package ru.mirea.petukhov.a.favoritebook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> activityResultLauncher;
    static final String KEY = "book_name";
    static final String USER_MESSAGE = "MESSAGE";
    private TextView textViewUserBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewUserBook = (TextView) findViewById(R.id.userBookTitle);

        ActivityResultCallback<ActivityResult> callback =
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK)
                        {
                            Intent data = result.getData();
                            String userBook = data.getStringExtra(USER_MESSAGE);
                            textViewUserBook.setText(userBook);
                        }
                    }
                };
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), callback
        );
    }

    public void getBookTitle(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(KEY, "'Хранители' - Дж. Р. Р. Толкин");
        activityResultLauncher.launch(intent);
    }
}