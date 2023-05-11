package ru.mirea.petukhov.a.camera;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.petukhov.a.camera.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int CAMERA_REQUEST = 0;
    private boolean isWork = false;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ////////////////////////////////////////////////////////////////////////////////////////////
        /// ПРОВЕРКА РАЗРЕШЕНИЙ
        ////////////////////////////////////////////////////////////////////////////////////////////

        int camPermStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int manageStoragePermStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(camPermStatus == PackageManager.PERMISSION_GRANTED && manageStoragePermStatus == PackageManager.PERMISSION_GRANTED)
        {
            isWork = true;
        }
        else
        {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION
            );
        }

        ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>()
        {
            @Override
            public void onActivityResult(ActivityResult result)
            {
                if(result.getResultCode() == Activity.RESULT_OK)
                {
                    Intent data = result.getData();
                    binding.photoImageView.setImageURI(imageUri);
                }
            }
        };

        ActivityResultLauncher<Intent> cameraActivityResultLauncher =
                registerForActivityResult( new ActivityResultContracts.StartActivityForResult(), callback);

        // button instead of imageview
        binding.button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(isWork)
                {
                    try
                    {
                        File photo = createImageFile();

                        String authorities = getApplicationContext().getPackageName() + ".fileprovider";
                        imageUri = FileProvider.getUriForFile(MainActivity.this, authorities, photo);

                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                        cameraActivityResultLauncher.launch(cameraIntent);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION)
        {
            isWork = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }
    private File createImageFile() throws IOException{
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH)
                        .format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }

}