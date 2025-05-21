package com.sachin.earn.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sachin.earn.Adapters.TasksAdapter;
import com.sachin.earn.Models.TasksModel;
import com.sachin.earn.Models.UserModel;
import com.sachin.earn.R;
import com.sachin.earn.RedeemActivity;
import com.sachin.earn.databinding.FragmentHomeBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    ArrayList<TasksModel>list;
    TasksAdapter adapter;
    FirebaseFirestore firestore;


    public HomeFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        firestore = FirebaseFirestore.getInstance();

        loadUserData();


        list = new ArrayList<>();

        list.add(new TasksModel("SPIN WHEEL", R.drawable.spin));
        list.add(new TasksModel("SCRATCH CARD", R.drawable.card));
        list.add(new TasksModel("GUESS NUMBER", R.drawable.guess));
        list.add(new TasksModel("FLIP AND WIN", R.drawable.flip));
        list.add(new TasksModel("LUCKY BOX", R.drawable.lucky));
        list.add(new TasksModel("WATCH VIDEO", R.drawable.video));
        list.add(new TasksModel("REFER AND EARN", R.drawable.refer));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvTask.setLayoutManager(layoutManager);

        adapter = new TasksAdapter(getContext(),list);
        binding.rvTask.setAdapter(adapter);
        binding.btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), RedeemActivity.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();


    }

    private void loadUserData() {

        firestore.collection("users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserModel model = documentSnapshot.toObject(UserModel.class);

                if (documentSnapshot.exists()){
                    binding.userName.setText(model.getName());
                    binding.userCoin.setText(model.getCoin()+"");

                    Picasso.get()
                            .load(model.getProfile())
                            .placeholder(R.drawable.friend_2)
                            .into(binding.profileImage);

                }
            }
        });
    }
}