package ru.mirea.petukhov.a.workmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest
                        .Builder(UploadWorker.class)
                        .setConstraints(constraints)
                        .build();

        WorkManager
                .getInstance(this)
                .enqueue(uploadWorkRequest);
    }
}