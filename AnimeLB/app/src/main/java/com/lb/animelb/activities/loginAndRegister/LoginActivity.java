package com.lb.animelb.activities.loginAndRegister;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.lb.animelb.R;
import com.lb.animelb.dbManagement.Firebase;


public class LoginActivity extends AppCompatActivity {
    private static final int GOOGLE_SING_IN_CODE = 10005;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;

    private EditText email;
    private EditText password;
    private TextView newUserRegister;
    private Button logInBt;
    private Button googleBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hooks
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);

        newUserRegister = findViewById(R.id.newUserRegister);
        logInBt = findViewById(R.id.logInBt);
        googleBt = findViewById(R.id.googleBt);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this, gso);


        //Buttons onClickListeners
        logInBt.setOnClickListener(v -> {
            String userEmail = email.getText().toString();
            String userPwd = password.getText().toString();
            if (userEmail.isEmpty() || userPwd.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Rellena los campos", Toast.LENGTH_SHORT).show();
            } else {
                Firebase.signInWithEmailAndPwd(this, userEmail, userPwd, this);
            }
        });

        googleBt.setOnClickListener(v -> {
            // Configure Google Sign In
            signInClient.signOut();
            Intent sign = signInClient.getSignInIntent();
            startActivityForResult(sign, GOOGLE_SING_IN_CODE);
        });

        newUserRegister.setOnClickListener(v ->
                startActivity(new Intent(this, NewUserActivity.class))
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SING_IN_CODE) {
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(
                        signInAcc.getIdToken(), null);
                Firebase.signInWithGoogle(authCredential, this);
            } catch (ApiException e) {
                //todo
            }
        }
    }

    public static void showAlert(Context context, String errorType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        switch (errorType) {
            case "password":
                builder.setMessage("Contraseña inválida");
                break;
            case "email":
                builder.setMessage("Usuario no registrado");
                break;
            case "google":
                builder.setMessage("No se ha podido registrar con Google");
                break;
        }
        builder.setTitle("ERROR");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.orange));
    }
}