package gonzales.labs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// LAB 2 Addition
import io.realm.Realm;

public class WelcomeActivity extends AppCompatActivity {

    TextView txtWelcome;
    SharedPreferences sharedPreferences;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtWelcome = findViewById(R.id.txtWelcome);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        realm = Realm.getDefaultInstance();

        String savedUuid = sharedPreferences.getString("uuid", "");

        User user = realm.where(User.class).equalTo("uuid", savedUuid).findFirst();

        if (user != null) {
            txtWelcome.setText("Welcome " + user.getName() + "!!!");
        } else {
            txtWelcome.setText("Welcome");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}