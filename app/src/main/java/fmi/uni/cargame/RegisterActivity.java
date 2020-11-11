package fmi.uni.cargame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RegisterActivity extends Activity {

    TextView usernameTV;
    TextView passwordTV;
    TextView repeatPasswordTV;
    EditText usernameET;
    EditText passwordET;
    EditText repeatPasswordET;
    TextView okTV;
    TextView cancelTV;

    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        usernameTV = findViewById(R.id.usernameTextView);
        passwordTV = findViewById(R.id.passwordTextView);
        repeatPasswordTV = findViewById(R.id.repeatPasswordTextView);
        usernameET = findViewById(R.id.usernameEditText);
        passwordET = findViewById(R.id.passwordEditText);
        repeatPasswordET = findViewById(R.id.repeatPasswordEditText);
        okTV = findViewById(R.id.okTV);
        cancelTV = findViewById(R.id.cancelTV);

        okTV.setOnClickListener(onClick);

        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String username = usernameET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();
            String repeatPassword = repeatPasswordET.getText().toString().trim();
            boolean isRegistred = false;

            List<Account> accounts = db.getAllAccounts();


            if (!username.isEmpty() && !password.isEmpty() && password.equals(repeatPassword)) {

                for (Account a : accounts) {
                    if (a.username.equals(username)) {
                        Toast.makeText(RegisterActivity.this, "Username is already used!", Toast.LENGTH_LONG).show();
                        isRegistred = true;
                        break;
                    }
                }

                if (!isRegistred) {
                    db.addAccount(new Account(username, password));

                    Toast.makeText(RegisterActivity.this, "Successfully registered ", Toast.LENGTH_LONG).show();

                    finish();
                }
            }

        }
    };
}
