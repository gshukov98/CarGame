package fmi.uni.cargame;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LoginActivity extends Activity {

    TextView usernameTV;
    TextView passwordTV;
    EditText usernameET;
    EditText passwordET;
    TextView loginTV;
    TextView registerTV;

    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        usernameTV = findViewById(R.id.usernameTextView);
        passwordTV = findViewById(R.id.passwordTextView);
        usernameET = findViewById(R.id.usernameEditText);
        passwordET = findViewById(R.id.passwordEditText);
        loginTV = findViewById(R.id.loginTV);
        registerTV = findViewById(R.id.registerTV);

        loginTV.setOnClickListener(onClick);
        registerTV.setOnClickListener(onClick);
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.registerTV) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            } else {
                String username = usernameET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                boolean login = false;

                List<Account> accounts = db.getAllAccounts();

                for (Account a : accounts) {
                    if (a.username.equals(username) && a.password.equals(password)) {
                        login = true;
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("accountName",usernameET.getText().toString().trim());
                        startActivity(intent);
                    }
                }

                if (login) {
                    Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    passwordET.setText("");

                } else {
                    Toast.makeText(LoginActivity.this, "Incorrect username or password!", Toast.LENGTH_LONG).show();
                }


            }
        }
    };
}

