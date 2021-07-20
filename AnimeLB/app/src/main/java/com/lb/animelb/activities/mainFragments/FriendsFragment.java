package com.lb.animelb.activities.mainFragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lb.animelb.AfterComplete;
import com.lb.animelb.R;
import com.lb.animelb.adapters.ProfileAdapter;
import com.lb.animelb.dbManagement.UserFB;

import static com.lb.animelb.clases.User.currentUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {
    private RecyclerView recyclerView;
    public EditText name;
    private CardView cardView;
    private TextView message;
    private ImageView imageView;

    public static ProfileAdapter profileAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam;
    private String mParam2;

    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
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
            mParam = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        // Hooks
        recyclerView = view.findViewById(R.id.recyclerViewFirends);
        name = view.findViewById(R.id.addFriendTextInput);
        cardView = view.findViewById(R.id.searchFriend);
        message = view.findViewById(R.id.friendMs);
        imageView = view.findViewById(R.id.friendImg);



        cardView.setOnClickListener(v -> {
                String sName = name.getText().toString();

                if (!sName.equals("") && !currentUser.username.equals(sName)) {
                    if (currentUser.friends.contains(sName)) {
                        Toast.makeText(getContext(), "Ya existe en tu lista de amigos.", Toast.LENGTH_SHORT).show();
                    } else {
                        UserFB.addUser(sName, getContext(), new AfterComplete() {
                            @Override
                            public void doAfterSuccess() {
                                Toast.makeText(getContext(), "Se ha añadido al usuario.", Toast.LENGTH_SHORT).show();
                                FriendsFragment.profileAdapter.notifyItemInserted(currentUser.friends.size());
                                name.getText().clear();
                            }
                        });
                    }
                }
            });
/*            AddFriendDialog addFriendDialog = new AddFriendDialog(getContext());
            addFriendDialog.create();
            addFriendDialog.show();*/

        profileAdapter = new ProfileAdapter(getContext(), currentUser.friends);
        recyclerView.setAdapter(profileAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Show message if recyclerView is empty when activity starts
        showEmptyRecyclerView();

        //when adapter range changed
        profileAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {


            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                showEmptyRecyclerView();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                showEmptyRecyclerView();
            }

            @Override
            public void onChanged() {
                super.onChanged();
                showEmptyRecyclerView();
            }
        });
        return view;
    }

    public void showEmptyRecyclerView(){
        if (profileAdapter.getItemCount() == 0) {
            message.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        } else {
            message.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }
    }
}