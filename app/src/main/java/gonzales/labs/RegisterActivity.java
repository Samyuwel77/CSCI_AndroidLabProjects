package gonzales.labs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// lab 2 additions (removed sharedprefs import)
import java.util.UUID;
import io.realm.Realm;

public class RegisterActivity extends AppCompatActivity {

    EditText edtRegUsername, edtRegPassword, edtConfirmPassword;
    Button btnSave, btnCancel;

//    SharedPreferences sharedPreferences;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtRegUsername = findViewById(R.id.edtRegUsername);
        edtRegPassword = findViewById(R.id.edtRegPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

//        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        realm.getDefaultInstance();

        btnSave.setOnClickListener(view -> {
            String username = edtRegUsername.getText().toString();
            String password = edtRegPassword.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Name or password must not be blank", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Confirm password does not match", Toast.LENGTH_SHORT).show();
            } else {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("username", username);
//                editor.putString("password", password);
//                editor.apply();
//
//                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
//                finish();   ***LAB 1 CODE***
                User existingUser = realm.where(User.class).equalTo("name", username).findFirst();

                if (existingUser!=null) {
                    Toast.makeText(this, "User " + username + " already exists", Toast.LENGTH_SHORT).show();
                } else {
                    realm.beginTransaction();

                    User newUser = realm.createObject(User.class, UUID.randomUUID().toString());
                    newUser.setName(username);
                    newUser.setPassword(password);

                    realm.commitTransaction();

                    long total = realm.where(User.class).count();

                    Toast.makeText(this, "New User saved. Total: " + total, Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(view -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}