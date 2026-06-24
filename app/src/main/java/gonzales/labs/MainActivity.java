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

// LAB 2 Addition
import io.realm.Realm;


public class MainActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    CheckBox chkRemember;
    Button btnSignIn, btnRegister, btnClear;

    SharedPreferences sharedPreferences;
    Realm realm;

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
        realm = Realm.getDefaultInstance();

        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);

        if (rememberMe) {
//            edtUsername.setText(sharedPreferences.getString("username", ""));
//            edtPassword.setText(sharedPreferences.getString("password", ""));
            chkRemember.setChecked(true);

            String savedUuid = sharedPreferences.getString("uuid", "");

            User savedUser = realm.where(User.class).equalTo("uuid", savedUuid).findFirst();

            if (savedUser!=null) {
                edtUsername.setText(savedUser.getName());
            }
        }

        btnSignIn.setOnClickListener(view -> {
            String inputUsername = edtUsername.getText().toString();
            String inputPassword = edtPassword.getText().toString();

//            String savedUsername = sharedPreferences.getString("username", "");
//            String savedPassword = sharedPreferences.getString("password", "");
//
//            if (savedUsername.isEmpty() || savedPassword.isEmpty()) {
//                Toast.makeText(this, "Nothing saved", Toast.LENGTH_SHORT).show();
//            } else if (inputUsername.equals(savedUsername) && inputPassword.equals(savedPassword)) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putBoolean("rememberMe", chkRemember.isChecked());
//                editor.apply();
//
//                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
//                startActivity(intent);
//            } else {
//                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
//            }

            User user = realm.where(User.class).equalTo("name", inputUsername).findFirst();

            if (user == null) {
                Toast.makeText(this, "No User found", Toast.LENGTH_SHORT).show();
            } else if (!inputPassword.equals(user.getPassword())) {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("uuid", user.getUuid());
                editor.putBoolean("rememberMe", chkRemember.isChecked());
                editor.apply();

                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}