package com.lb.animelb.dbManagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lb.animelb.activities.loginAndRegister.CreateUsernameActivity;

import java.util.List;

import static com.lb.animelb.activities.loginAndRegister.LoginActivity.showAlert;
import static com.lb.animelb.clases.User.currentUser;

public class Firebase {
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static StorageReference storage = FirebaseStorage.getInstance().getReference().child("profilePics");
    public static CollectionReference usersFB = db.collection("users");
    public static CollectionReference animesFB;
    public static CollectionReference friendsFB;

    public static void init() {
        animesFB = usersFB.document(currentUser.username).collection("animes");
        friendsFB = usersFB.document(currentUser.username).collection("friends");
    }

    public static void signInWithGoogle(AuthCredential authCredential, Context context) {
        firebaseAuth.signInWithCredential(authCredential).addOnSuccessListener(authResult -> {
            if (authResult.getAdditionalUserInfo().isNewUser() || getUsername().equals(getEmail())) {
                context.startActivity(new Intent(context, CreateUsernameActivity.class));
            } else {
                currentUser.startApp(context);
            }
        }).addOnFailureListener(e -> showAlert(context, "google"));
    }

    public static void signInWithEmailAndPwd(Activity activity, String email, String password, Context context) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (getUsername().equals(getEmail())) {
                    context.startActivity(new Intent(context, CreateUsernameActivity.class));
                } else {
                    currentUser.startApp(context);
                }
            } else {
                existEmailFirebase(email, context);
            }
        });
    }

    public static void existEmailFirebase(String email, Context context){
        firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()){
                            SignInMethodQueryResult result = task.getResult();
                            List<String>  signInMethods = result.getSignInMethods();
                            if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD) || signInMethods.contains(EmailAuthProvider.EMAIL_LINK_SIGN_IN_METHOD)){
                                showAlert(context, "password");
                            }else {
                                showAlert(context, "email");
                            }
                        }
                    }
                });
    }

    public static boolean authIsGoogle() {
        return firebaseAuth.getAccessToken(false).getResult().getSignInProvider().equals("google.com");
    }

    public static String getUsername() {
        return firebaseAuth.getCurrentUser().getDisplayName();
    }

    public static String getEmail() {
        return firebaseAuth.getCurrentUser().getEmail();
    }

    public static void signOut() {
        firebaseAuth.signOut();
    }

    public static FirebaseUser getUser() {
        return firebaseAuth.getCurrentUser();
    }
}
