package com.example.cookingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    EditText username, password, confirmPassword;
    TextView warnings;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.editText_username);
        password = findViewById(R.id.editText_password);
        confirmPassword = findViewById(R.id.editText_confirm_password);
        warnings = findViewById(R.id.textView_warnings);
        db = new DatabaseHelper(this);
    }

    public void signUpUser(View view) {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        if (pass.isEmpty() || confirmPass.isEmpty() || user.isEmpty()) {
            updateWarnings("Please fill in all the fields");
            return;
        }

        if (!isValidUsername(user)) {
            updateWarnings("Invalid username format");
            return;
        }

        if (!isValidPassword(pass)) {
            updateWarnings("Invalid password format");
            return;
        }

        if (!pass.equals(confirmPass)) {
            updateWarnings("Passwords don't match");
            return;
        }

        if (isUsernameTaken(user)) {
            updateWarnings("Username is already taken. Please choose a different one.");
            return;
        }

        long result = db.addUser(user, pass);

        if (result != -1) {
            // Successfully signed up
            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Handle signup failure
            updateWarnings("Sign up failed. Please try again.");
        }
    }

    private boolean isValidUsername(String username) {
        String regex = "^[a-z0-9]{6,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[0-9\\W]).{6,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean isUsernameTaken(String username) {
        return db.isUsernameTaken(username);
    }

    private void updateWarnings(String message) {
        warnings.setText(message);
    }

    public void goToSignIn(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
