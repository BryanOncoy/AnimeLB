package com.lb.animelb.activities.loginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.lb.animelb.R;
import com.lb.animelb.dbManagement.UserFB;

public class CreateUsernameActivity extends AppCompatActivity {
    private EditText username;
    private Button btContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_username);

        // Hooks
        username = findViewById(R.id.usernameTxt);
        btContinue = findViewById(R.id.continueBt);

        // Create a temporary Firebase username with the email value in case the user
        // closes the app before setting a username
        UserFB.setTempUsername();

        //Make the button only clickable when the text field is not empty
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                btContinue.setEnabled(username.getText().length() > 0);
            }
        });

        btContinue.setOnClickListener(v ->
                UserFB.createUserDB(username.getText().toString(), CreateUsernameActivity.this)
        );
    }
}