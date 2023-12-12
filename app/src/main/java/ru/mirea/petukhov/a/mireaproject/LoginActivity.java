package ru.mirea.petukhov.a.mireaproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.mirea.petukhov.a.mireaproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private final String TAG = LoginActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean serviceIsStarted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        binding.emailField.setText(preferences.getString("login", ""));
        binding.passwordField.setText(preferences.getString("password", ""));

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(MainActivity.class.getSimpleName(), "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(MainActivity.class.getSimpleName(), "onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        };

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailField.getText().toString();
                String password = new Sha256Cryptor(binding.passwordField.getText().toString()).getHash();
                Log.wtf("HASH", password);
                signIn(email, password);
            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailField.getText().toString();
                String password = new Sha256Cryptor(binding.passwordField.getText().toString()).getHash();
                createAccount(email, password);
            }
        });

        List<String> allPackages = getAppNames();
        if(allPackages.contains("com.anydesk.anydeskandroid")){
            Toast.makeText(this, "Есть anydesk. БЛОКИРУЮ!!!", Toast.LENGTH_LONG).show();
            finish();
        }

    }
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void updateUI(FirebaseUser user) {

    }

    private List<String> getAppNames(){
        List<String> appNames = new ArrayList<>();

        PackageManager packageManager = this.getPackageManager();

        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for(ApplicationInfo packageInfo : packages){
            appNames.add(packageInfo.packageName);
        }

        return appNames;
    }

    private void signIn(String email, String password){
        Log.d(MainActivity.class.getSimpleName(), "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(MainActivity.class.getSimpleName(), "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(MainActivity.class.getSimpleName(), "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // TODO: запись в preferences
                            String login = String.valueOf(binding.emailField.getText());
                            String password = String.valueOf(binding.passwordField.getText());

                            SharedPreferences preferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("login", login);
                            editor.putString("password", password);
                            editor.apply();


                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
    private void createAccount(String email, String password){
        Log.d(MainActivity.class.getSimpleName(), "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(MainActivity.class.getSimpleName(), "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getBaseContext(), "Check your email", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = binding.emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            binding.emailField.setError("Required.");
            valid = false;
        } else {
            binding.emailField.setError(null);
        }

        String password = binding.passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            binding.passwordField.setError("Required.");
            valid = false;
        } else {
            binding.passwordField.setError(null);
        }

        return valid;
    }
}