package com.lb.animelb.activities.loginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.lb.animelb.R;
import com.lb.animelb.dbManagement.UserFB;

public class NewUserActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private CheckBox checkBox;
    private TextView logInTxt;
    private Button registerBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        // Hooks
        email = findViewById(R.id.emailUser);
        password = findViewById(R.id.passwordUser);
        checkBox = findViewById(R.id.checkBox);
        logInTxt = findViewById(R.id.logInTxt);
        registerBt = findViewById(R.id.registerBt);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                registerBt.setEnabled(isChecked);
            }
        });

        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFB.createUserAuth(email.getText().toString(), password.getText().toString(), NewUserActivity.this);
            }
        });

        logInTxt.setOnClickListener(v -> {
            Intent intent = new Intent(NewUserActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}