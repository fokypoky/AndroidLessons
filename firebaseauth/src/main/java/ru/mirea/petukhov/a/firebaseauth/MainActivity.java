package ru.mirea.petukhov.a.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import java.util.Objects;

import ru.mirea.petukhov.a.firebaseauth.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

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

        binding.createEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailField.getText().toString();
                String password = binding.passwordField.getText().toString();
                createAccount(email, password);
            }
        });

        binding.emailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailField.getText().toString();
                String password = binding.passwordField.getText().toString();
                signIn(email, password);
            }
        });

        binding.signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        binding.verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification();
            }
        });

    }
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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
                            Toast.makeText(MainActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (!task.isSuccessful()) {
                            binding.statusTextView.setText(R.string.auth_failed);
                        }
                    }
                });
    }
    @SuppressLint("StringFormatMatches")
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            binding.statusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail()));
            binding.detailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            binding.emailPasswordButtons.setVisibility(View.GONE);
            binding.emailPasswordFields.setVisibility(View.GONE);
            binding.signOutButton.setVisibility(View.VISIBLE);
            binding.verifyButton.setVisibility(View.VISIBLE);
        } else {
            binding.statusTextView.setText(R.string.signed_out);
            binding.detailTextView.setText(null);
            binding.statusTextView.setText(R.string.signed_out);
            binding.detailTextView.setText(null);

            binding.emailPasswordButtons.setVisibility(View.VISIBLE);
            binding.emailPasswordFields.setVisibility(View.VISIBLE);
            binding.signOutButton.setVisibility(View.GONE);
            binding.verifyButton.setVisibility(View.GONE);
        }
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
                            Toast.makeText(MainActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void sendEmailVerification() {
        binding.verifyButton.setEnabled(false);
        final FirebaseUser user = mAuth.getCurrentUser();
        Objects.requireNonNull(user).sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override

                    public void onComplete(@NonNull Task<Void> task) {

                        binding.verifyButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.e(MainActivity.class.getSimpleName(), "sendEmailVerification", task.getException());
                            Toast.makeText(MainActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm(){
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
    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }
}