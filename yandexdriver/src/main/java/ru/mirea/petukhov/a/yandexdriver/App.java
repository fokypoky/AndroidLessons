package ru.mirea.petukhov.a.yandexdriver;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

public class App extends Application{
    private final String API_KEY = "5fa69f17-ad8e-43a2-8d6c-28d5771df7b6";

    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey(API_KEY);
    }
}
