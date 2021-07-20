package com.lb.animelb.dbManagement;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lb.animelb.AfterComplete;
import com.lb.animelb.R;
import com.lb.animelb.activities.MainActivity;
import com.lb.animelb.activities.loginAndRegister.CreateUsernameActivity;

import java.util.HashMap;
import java.util.Map;

import static com.lb.animelb.clases.User.currentUser;

public class UserFB {
    private static String defaultUri = "https://firebasestorage.googleapis.com/v0/b/animelb-d9635.appspot.com/o/profilePics%2Fdefault.jpg?alt=media&token=66d22ab9-c9b2-4370-a29b-7db772528777";

    public static void setTempUsername() {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Firebase.getEmail()).build();
        Firebase.firebaseAuth.getCurrentUser().updateProfile(profileUpdates);
    }

    public static void createUserAuth(String email, String password, Context context) {
        if (password.length() < 6) {
            Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
        } else {
        Firebase.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(context, CreateUsernameActivity.class);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Este email ya está registrado.", Toast.LENGTH_SHORT).show();
                    }
                });
    }}

    public static void createUserDB(String username, Context context) {
        Firebase.usersFB.document(username).get().addOnSuccessListener(documentSnapshot -> {
            // Check if the username is already registered in FireStore
            if (documentSnapshot.exists()) {
                Toast.makeText(context, "Este nombre de usuario ya existe.", Toast.LENGTH_SHORT).show();
            } else {
                // Update the user's displayName
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username).build();

                // Create an entry in FireStore for the new user
                Firebase.firebaseAuth.getCurrentUser().updateProfile(profileUpdates).addOnSuccessListener(unused -> {
                    Map<String, Object> defaultUser = new HashMap<>();
                    defaultUser.put("publicList", true);
                    defaultUser.put("picUri", defaultUri);
                    Firebase.db.collection("users").document(username).set(defaultUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            currentUser.startApp(context);
                        }
                    });
                });
            }
        });
    }

    public static void fillFriendsList() {
        Firebase.friendsFB.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String nickName = doc.getString("nickName");
                    currentUser.friends.add(nickName);

                    Firebase.usersFB.document(nickName).get()
                            .addOnSuccessListener(documentSnapshot ->
                                    currentUser.pictures.put(nickName, Uri.parse(documentSnapshot.getString("picUri")))
                            );
                }
            }
            currentUser.ready[2] = true;
        });
    }

    public static void loadProfilePic() {
        Firebase.usersFB.document(currentUser.username).get().addOnSuccessListener(documentSnapshot -> {
            currentUser.uri = Uri.parse(documentSnapshot.getString("picUri"));
            currentUser.ready[0] = true;
        });
    }

    public static void changePassword(String currentPassword, String newPassword, Context context) {
        AuthCredential authCredential = EmailAuthProvider.getCredential(
                currentUser.email,
                currentPassword
        );

        Firebase.getUser().reauthenticate(authCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Firebase.getUser().updatePassword(newPassword).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(context, "Se ha actualizado la contraseña.", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, MainActivity.class));
                    } else {
                        Toast.makeText(context, "Introduzca una contraseña de al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                showAlert(context);
            }
        });
    }

    private static void showAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("La contraseña actual no es correcta.");
        builder.setTitle("Error");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.orange));
    }

    public static void uploadProfilePic() {
        String picName = "/" + currentUser.username + ".jpg";

        // Upload the selected picture
        Firebase.storage.child(picName).putFile(currentUser.uri).addOnSuccessListener(taskSnapshot -> {

            // Get the Storage Uri for the new profile picture
            Firebase.storage.child(picName).getDownloadUrl().addOnSuccessListener(uri -> {

                // Save uri in Data.uri for local usages
                currentUser.uri = uri;

                // Add it to the user field 'picUri'
                Firebase.usersFB.document(currentUser.username).update(
                        "picUri", uri.toString()
                );
            });
        });
    }

    public static void addUser(String friendName, Context context, AfterComplete afterComplete) {
        Firebase.usersFB.document(friendName).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // If it exists, add user to local
                String nickName = documentSnapshot.getId();
                currentUser.friends.add(nickName);

                // Add his profile pic to local
                currentUser.pictures.put(nickName, Uri.parse(documentSnapshot.getString("picUri")));

                // Add user to database
                Map<String, String> newFriend = new HashMap<>();
                newFriend.put("nickName", nickName);
                Firebase.usersFB.document(currentUser.username).collection("friends")
                        .document(nickName).set(newFriend);

                // AfterSucess interface method
                afterComplete.doAfterSuccess();
            } else {
                Toast.makeText(context, "Este usuario no existe.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void unfollow(String friendName, AfterComplete afterComplete) {
        Firebase.friendsFB.document(friendName).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                currentUser.friends.remove(friendName);
                currentUser.pictures.remove(friendName);
                afterComplete.doAfterSuccess();
            }
        });
    }
}
