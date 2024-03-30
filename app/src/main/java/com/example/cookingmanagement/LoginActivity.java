package com.example.cookingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    DatabaseHelper db;
    TextView warnings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.editText_username);
        password = findViewById(R.id.editText_password);
        warnings = findViewById(R.id.textView_warnings_login);
        db = new DatabaseHelper(this);
    }

    public void signInUser(View view) {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            updateWarnings("Please fill in all the fields");
            return;
        }

        if (!db.checkUser(user, pass)) {
            updateWarnings("Invalid username or password");
            return;
        }

        // Successful login
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void updateWarnings(String message) {
        warnings.setText(message);
    }
}
