package com.lb.animelb.dbManagement;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lb.animelb.clases.AnimeInfo;

import static com.lb.animelb.clases.User.currentUser;

public class AnimeFB {
    public static void addAnime(AnimeInfo currentAnime) {
        // Save the anime in our local list
        currentUser.favAnimes.add(currentAnime);

        // Save it in FireStore
        Firebase.animesFB.document(currentAnime.getTitle()).set(currentAnime);
    }

    public static void removeAnime(AnimeInfo currentAnime) {
        // Remove the anime from our local list
        currentUser.favAnimes.remove(currentAnime);

        // Remove it from FireStore
        Firebase.animesFB.document(currentAnime.getTitle()).delete();
    }

    public static void fillFavAnimes() {
        Firebase.animesFB.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                AnimeInfo animeInfo = doc.toObject(AnimeInfo.class);
                                currentUser.favAnimes.add(animeInfo);
                            }
                        }
                        currentUser.ready[1] = true;
                    }
                });
    }
}
