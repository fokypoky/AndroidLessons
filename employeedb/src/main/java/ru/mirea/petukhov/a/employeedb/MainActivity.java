package ru.mirea.petukhov.a.employeedb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import ru.mirea.petukhov.a.employeedb.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppDatabase db = App.getInstance().getDatabase();
        ISuperheroDao dao = db.superheroDao();

        Superhero superhero = new Superhero();

        superhero.id = 1;
        superhero.name = "Krivoi PORK";
        superhero.defeatedEvilCount = 140;

        dao.insert(superhero);

        List<Superhero> superheroes = dao.getAll();

        List<Superhero> superheroes1 = dao.getById(1);

        superhero.defeatedEvilCount = 1000;
        dao.update(superhero);

        Log.d("ИНФОРМАЦИЯ ПРО СУПЕРГЕРОЯ", superhero.name + " " + superhero.defeatedEvilCount);
    }
}