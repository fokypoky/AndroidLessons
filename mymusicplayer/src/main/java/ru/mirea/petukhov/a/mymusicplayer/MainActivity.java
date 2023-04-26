package ru.mirea.petukhov.a.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;

import ru.mirea.petukhov.a.mymusicplayer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
//        activityMainBinding.imageViewPlayer
        Glide.with(this).load("https://sun9-57.userapi.com/impg/s3TmHxeYdwuVYdEIF_KeVNqMxcD8BqJ_OJwHmA/JYD3PAzvF0s.jpg?size=1280x1280&quality=96&sign=f19ec62b264ab13016f884d7cda3259c&type=album").into(activityMainBinding.imageViewPlayer);
    }
}