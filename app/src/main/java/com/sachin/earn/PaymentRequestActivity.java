package com.sachin.earn;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sachin.earn.Adapters.TrHistoryAdapter;
import com.sachin.earn.Models.PaymentRequestModel;
import com.sachin.earn.databinding.ActivityPaymentRequestBinding;

import java.util.ArrayList;

public class PaymentRequestActivity extends AppCompatActivity {

    ActivityPaymentRequestBinding binding;
    FirebaseFirestore firestore;
    TrHistoryAdapter adapter;
    ArrayList<PaymentRequestModel> list;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        list = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.historyRecy.setLayoutManager(layoutManager);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_dialog);

        if (dialog.getWindow() != null){

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
        }

        dialog.show();

        adapter = new TrHistoryAdapter(this,list);
        binding.historyRecy.setAdapter(adapter);

        firestore.collection("redeem").document(FirebaseAuth.getInstance().getUid()).collection("request").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                list.clear();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots){

                    PaymentRequestModel user = snapshot.toObject(PaymentRequestModel.class);
                    list.add(user);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }
}