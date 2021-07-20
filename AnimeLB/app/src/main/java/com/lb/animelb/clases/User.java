package com.lb.animelb.clases;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.lb.animelb.activities.MainActivity;
import com.lb.animelb.dbManagement.AnimeFB;
import com.lb.animelb.dbManagement.Firebase;
import com.lb.animelb.dbManagement.UserFB;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class User {
    public static User currentUser = new User();

    public Uri uri = null;
    public Map<String, Uri> pictures = new HashMap<>();
    public ArrayList<String> friends = new ArrayList<>();
    public ArrayList<String> favAnimeNames = new ArrayList<>();
    public ArrayList<AnimeInfo> favAnimes = new ArrayList<>();
    public String email = null;
    public String username = null;
    private ImageView profilePic = null;
    private ImageView profilePicFull = null;

    public boolean[] ready = new boolean[3];

    public void clearUser() {
        pictures.clear();
        friends.clear();
        favAnimeNames.clear();
        favAnimes.clear();
        email = null;
        username = null;
        profilePic = null;
        profilePicFull = null;
    }

    public void startApp(Context context) {
        Arrays.fill(ready, false);

        ProgressDialog dialog = ProgressDialog.show(context, "",
                "Cargando. Porfavor espere...", true);

        //Set email and username
        email = Firebase.getEmail();
        username = Firebase.getUsername();
        Firebase.init();
        UserFB.loadProfilePic();
        UserFB.fillFriendsList();
        AnimeFB.fillFavAnimes();;

        Thread t = new Thread(() -> {
            boolean start;
            do {
                start = true;
                for (boolean b : ready) {
                    if (!b) {
                        start = false;
                        break;
                    }
                }
            } while (!start);

            // Dismiss the dialog and switch to MainActivity
            dialog.dismiss();
            context.startActivity(new Intent(context, MainActivity.class));
        });

        t.start();
    }

    public void updateProfilePic() {
        Picasso.get().load(uri).centerCrop().fit().into(profilePic);
    }

    public void updateProfilePicFull() {
        Picasso.get().load(uri).centerCrop().fit().into(profilePicFull);
    }

    public void setProfilePic(ImageView profilePic) {
        this.profilePic = profilePic;
    }

    public void setProfilePicFull(ImageView profilePicFull) {
        this.profilePicFull = profilePicFull;
    }
}