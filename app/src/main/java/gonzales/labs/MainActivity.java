package gonzales.labs;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    CheckBox chkRemember;
    Button btnSignIn, btnRegister, btnClear;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        chkRemember = findViewById(R.id.chkRemember);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);
        btnClear = findViewById(R.id.btnClear);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);

        if (rememberMe) {
            edtUsername.setText(sharedPreferences.getString("username", ""));
            edtPassword.setText(sharedPreferences.getString("password", ""));
            chkRemember.setChecked(true);
        }

        btnSignIn.setOnClickListener(view -> {
            String inputUsername = edtUsername.getText().toString();
            String inputPassword = edtPassword.getText().toString();

            String savedUsername = sharedPreferences.getString("username", "");
            String savedPassword = sharedPreferences.getString("password", "");

            if (savedUsername.isEmpty() || savedPassword.isEmpty()) {
                Toast.makeText(this, "Nothing saved", Toast.LENGTH_SHORT).show();
            } else if (inputUsername.equals(savedUsername) && inputPassword.equals(savedPassword)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("rememberMe", chkRemember.isChecked());
                editor.apply();

                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnClear.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            edtUsername.setText("");
            edtPassword.setText("");
            chkRemember.setChecked(false);

            Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show();
        });
    }
}