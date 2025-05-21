package com.sachin.earn.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sachin.earn.Adapters.LeaderboardAdapter;
import com.sachin.earn.Models.UserModel;
import com.sachin.earn.R;
import com.sachin.earn.databinding.FragmentLeaderboardBinding;
import com.sachin.earn.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LeaderboardFragment extends Fragment {

    FragmentLeaderboardBinding binding;
    ArrayList<UserModel>list;
    LeaderboardAdapter adapter;
    FirebaseFirestore firestore;

    public LeaderboardFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);

        firestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvLeaderboard.setLayoutManager(layoutManager);

        adapter = new LeaderboardAdapter(getContext(), list, new LeaderboardAdapter.RankListener() {
            @Override
            public void rankItem(int position, String name, String profile, int coin, ArrayList<UserModel> list) {

                if (position==0){

                    binding.rank1Name.setText(name);
                    binding.rank1Coin.setText(coin+"");

                    Picasso.get()
                            .load(profile)
                            .placeholder(R.drawable.friend_2)
                            .into(binding.profileImage);
                }
                else if (position==1){

                    binding.rank2Name.setText(name);
                    binding.rank2Coin.setText(coin+"");

                    Picasso.get()
                            .load(profile)
                            .placeholder(R.drawable.friend_2)
                            .into(binding.rank2Profile);
                }
                else if (position==2){

                    binding.rank3Name.setText(name);
                    binding.rank3Coin.setText(coin+"");

                    Picasso.get()
                            .load(profile)
                            .placeholder(R.drawable.friend_2)
                            .into(binding.rank3profile);
                }
            }
        });

        binding.rvLeaderboard.setAdapter(adapter);

        firestore.collection("users").orderBy("coin", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot snapshot : queryDocumentSnapshots){

                    UserModel model = snapshot.toObject(UserModel.class);
                    list.add(model);
                }

                adapter.notifyDataSetChanged();
            }
        });

        return binding.getRoot();
    }
}