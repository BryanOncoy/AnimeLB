package com.lb.animelb.activities.dataChanges;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lb.animelb.R;
import com.lb.animelb.dbManagement.UserFB;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmPassword;
    private Button changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Hooks
        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        changePassword = findViewById(R.id.btChangePassword);

        currentPassword.addTextChangedListener(textWatcher);
        newPassword.addTextChangedListener(textWatcher);
        confirmPassword.addTextChangedListener(textWatcher);

        changePassword.setOnClickListener(v -> {
            String currentPwdText = currentPassword.getText().toString();
            String newPwdText = newPassword.getText().toString();
            boolean passwordsMatch = newPwdText.equals(confirmPassword.getText().toString());
            if (passwordsMatch) {
                UserFB.changePassword(currentPwdText, newPwdText,this);
            } else {
                Toast.makeText(ChangePasswordActivity.this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            int currPwLength = currentPassword.length();
            int newPwLength = newPassword.length();
            int confPwLength = confirmPassword.length();

            boolean textsFilled = currPwLength != 0 && newPwLength != 0 && confPwLength != 0;
            boolean lengthMatch = newPwLength == confPwLength;

            changePassword.setEnabled(textsFilled && lengthMatch);
        }
    };
}