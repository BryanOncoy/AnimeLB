package com.lb.animelb.activities.mainFragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.lb.animelb.activities.dataChanges.ChangePasswordActivity;
import com.lb.animelb.activities.dataChanges.FullScreenImgActivity;
import com.lb.animelb.R;
import com.lb.animelb.dbManagement.Firebase;
import com.lb.animelb.activities.loginAndRegister.LoginActivity;

import static com.lb.animelb.clases.User.currentUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private ImageView profilePic;
    private TextView email;
    private TextView username;
    private CardView changePassword;
    private Button takePhoto;
    private Button logOutBt;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        if (Firebase.authIsGoogle()) {
            view = inflater.inflate(R.layout.fragment_profile_google, container, false);
            initGoogleAuth(view);
        } else {
            view = inflater.inflate(R.layout.fragment_profile, container, false);
            initEmailAndPwAuth(view);
        }

        currentUser.setProfilePic(profilePic);
        currentUser.updateProfilePic();

        profilePic.setOnClickListener(v ->
                startActivity(new Intent(getContext(), FullScreenImgActivity.class))
        );

        username.setText(currentUser.username);
        email.setText(currentUser.email);

        logOutBt.setOnClickListener(v -> leave());

        return view;
    }


    private void initGoogleAuth(View view) {
        profilePic = view.findViewById(R.id.profilePicGoogle);
        username = view.findViewById(R.id.userTxtGoogle);
        email = view.findViewById(R.id.currentEmailGoogle);
        logOutBt = view.findViewById(R.id.logOutBtGoogle);
    }

    private void initEmailAndPwAuth(View view) {
        profilePic = view.findViewById(R.id.profilePic);
        username = view.findViewById(R.id.userTxt);
        email = view.findViewById(R.id.currentEmail);
        changePassword = view.findViewById(R.id.changePassword);
        logOutBt = view.findViewById(R.id.logOutBt);

        changePassword.setOnClickListener(v ->
                startActivity(new Intent(ProfileFragment.this.getActivity(), ChangePasswordActivity.class))
        );
    }

    private void leave() {
        Firebase.signOut();
        currentUser.clearUser();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}