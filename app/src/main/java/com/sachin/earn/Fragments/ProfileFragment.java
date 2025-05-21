package com.sachin.earn.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sachin.earn.LoginActivity;
import com.sachin.earn.Models.UserModel;
import com.sachin.earn.PaymentRequestActivity;
import com.sachin.earn.R;
import com.sachin.earn.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    Uri profileUri;
    ProgressDialog progressDialog;

    public ProfileFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Profile Uploading");
        progressDialog.setMessage("We Are Uploading Your Profile");

        binding.privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.pinkesh.site/")));
            }
        });

        binding.term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.pinkesh.site/")));
            }
        });

        binding.transactionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), PaymentRequestActivity.class);
                startActivity(intent);
            }
        });

        binding.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.pinkesh.site/")));
            }
        });

        binding.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.pinkesh.site/")));
            }
        });

        binding.fetchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,2);
            }
        });

        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shareBody = "Hey, I am Using Best Earning App";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(intent);
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        loadUserData();
        return binding.getRoot();
    }

    private void loadUserData() {

        firestore.collection("users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserModel model = documentSnapshot.toObject(UserModel.class);

                if (documentSnapshot.exists()){
                    binding.usersName.setText(model.getName());
                    binding.usersEmail.setText(model.getEmail());

                    Picasso.get()
                            .load(model.getProfile())
                            .placeholder(R.drawable.friend_2)
                            .into(binding.profileImage);


                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==2){

            if (data!=null){

                profileUri = data.getData();
                binding.profileImage.setImageURI(profileUri);

                updateProfile(profileUri);
            }
        }
    }

    private void updateProfile(Uri profileUri) {

        progressDialog.show();

        final StorageReference reference = storage.getReference().child("profile").child(FirebaseAuth.getInstance().getUid());

        reference.putFile(profileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                                        .update("profile",uri.toString());

                                Toast.makeText(getContext(), "Profile update",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
    }
}