package com.raffelberg.cr_ticker.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.profiles.TickerEditor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LogInActivity extends AppCompatActivity {

    private Button logIn_Button;
    private TextView pw_Input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        pw_Input= findViewById(R.id.pw_Input);
        logIn_Button = findViewById(R.id.logIn_Button);

        /**
         * checks if input = password, if yes, redirects to target activity and sets user logged in
         */
        logIn_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pw_Input.getText().toString().equals("123648")){
                    TickerEditor user = TickerEditor.getInstance();
                    user.setLogged_in(true);
                    String target = getIntent().getStringExtra("target");
                    if(target != null) {
                        if (target.equals("addMatch")) {
                            Intent addMatch_Intent = new Intent(getApplicationContext(), AddMatchActivity.class);
                            startActivity(addMatch_Intent);
                        } else {
                            Intent manageMatch_Intent = new Intent(getApplicationContext(), manageMatchActivity.class);
                            startActivity(manageMatch_Intent);
                        }
                    }
                }
            }
        });
    }
}