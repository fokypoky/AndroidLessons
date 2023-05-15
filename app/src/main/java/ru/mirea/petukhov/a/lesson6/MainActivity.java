package ru.mirea.petukhov.a.lesson6;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ru.mirea.petukhov.a.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //region init data
        AppPreferences appPreferences = getPreferences();

        binding.groupNumberEditText.setText(appPreferences.group);
        binding.studentNumberEditText.setText(String.valueOf(appPreferences.studentNumber));
        binding.favoriteMovieEditText.setText(appPreferences.movieTitle);
        //endregion

        binding.saveInfoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String groupNumber = binding
                        .groupNumberEditText
                        .getText()
                        .toString();

                String studentNumberStringValue = binding
                        .studentNumberEditText
                        .getText()
                        .toString();

                int studentNumber = Integer.parseInt(studentNumberStringValue);

                String movieTitle = binding
                        .favoriteMovieEditText
                        .getText()
                        .toString();

                savePreferences(groupNumber, studentNumber, movieTitle);
            }
        });
    }

    private void savePreferences(String groupNumber, int studentNumber, String movieTitle)
    {
        SharedPreferences preferences = getSharedPreferences("lesson6-app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("GroupNumber", groupNumber);
        editor.putInt("StudentNumber", studentNumber);
        editor.putString("Movie", movieTitle);

        editor.apply();

        Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_LONG).show();
    }
    private AppPreferences getPreferences()
    {
        SharedPreferences preferences = getSharedPreferences("lesson6-app", Context.MODE_PRIVATE);
        return new AppPreferences(
                preferences.getString("GroupNumber", "not set"),
                preferences.getInt("StudentNumber", 0),
                preferences.getString("Movie", "not set")
        );
    }

}