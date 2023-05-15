package ru.mirea.petukhov.a.internalfilestorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.mirea.petukhov.a.internalfilestorage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private boolean canReadFile = false;
    private ActivityMainBinding binding;
    private final String filePath = "date_file.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.readFileButton.setEnabled(canReadFile);

        binding.saveFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.dateEditText.getText().toString();


                if(writeTextInFile(filePath, text)){
                    Log.d(MainActivity.class.getSimpleName(), "Файл записан успешно");
                    canReadFile = true;
                    binding.readFileButton.setEnabled(canReadFile);
                }
                else{
                    Log.d(MainActivity.class.getSimpleName(), "При записи файла произошла ошибка");
                }
            }
        });

        binding.readFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Thread.sleep(5000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        String readiedText = readFile();
                        showToast("Прочитанный текст: " + readiedText);
                    }
                });
            }
        });

    }
    private String readFile(){
        FileInputStream inputStream = null;
        try
        {
            inputStream = openFileInput("date_file.txt");
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            String text = new String(bytes);
            return text;
        }
        catch (IOException e)
        {
            showToast(e.getMessage());
        }
        finally
        {
            try
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
            }
            catch (IOException e)
            {
                showToast(e.getMessage());
            }
        }
        return null;
    }
    private void showToast(String toastMessage){
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }
    private boolean writeTextInFile(String filePath, String text)
    {
        FileOutputStream outputStream;
        try
        {
            outputStream = openFileOutput(filePath, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}