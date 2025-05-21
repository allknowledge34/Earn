package com.sachin.earn;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sachin.earn.AdmobAds.Admob;
import com.sachin.earn.Models.UserModel;
import com.sachin.earn.databinding.ActivityFlipCardBinding;

import java.util.Random;

public class FlipCardActivity extends AppCompatActivity {

    ActivityFlipCardBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    CountDownTimer timer;
    Dialog dialog;
    TextView wonCoins;
    Button btnGetCoin;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlipCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.flip_dialog);

        if (dialog.getWindow() != null){

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
        }

        wonCoins = dialog.findViewById(R.id.wonCoin);
        btnGetCoin = dialog.findViewById(R.id.btnGetCoin);

        loadCoins();
        resetTimer();

        Admob.loadBannerAd(findViewById(R.id.bannerAds),FlipCardActivity.this);
        Random random = new Random();
        int val = random.nextInt(50);
        binding.num1.setText(val+"");

        Random random2 = new Random();
        int val2 = random2.nextInt(40);
        binding.num2.setText(val2+"");

        Random random3 = new Random();
        int val3 = random3.nextInt(30);
        binding.num3.setText(val3+"");

        Random random4 = new Random();
        int val4 = random4.nextInt(20);
        binding.num4.setText(val4+"");

        Random random5 = new Random();
        int val5 = random5.nextInt(10);
        binding.num5.setText(val5+"");

        Random random6 = new Random();
        int val6 = random6.nextInt(30);
        binding.num6.setText(val6+"");

        Random random7 = new Random();
        int val7 = random7.nextInt(40);
        binding.num7.setText(val7+"");

        Random random8 = new Random();
        int val8 = random8.nextInt(50);
        binding.num8.setText(val8+"");

        Random random9 = new Random();
        int val9 = random9.nextInt(20);
        binding.num9.setText(val9+"");

        Random random10 = new Random();
        int val10 = random10.nextInt(50);
        binding.num10.setText(val10+"");

        Random random11 = new Random();
        int val11 = random11.nextInt(40);
        binding.num11.setText(val11+"");

        Random random12 = new Random();
        int val12 = random12.nextInt(10);
        binding.num12.setText(val12+"");

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        binding.gifit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wonCoins.setText(val+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                addCoins(val);

                binding.gifit1.setVisibility(View.GONE);
                dialog.show();
            }
        });

        binding.gifit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wonCoins.setText(val2+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                addCoins(val2);

                binding.gifit2.setVisibility(View.GONE);
                dialog.show();
            }
        });

        binding.gifit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wonCoins.setText(val3+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                addCoins(val3);

                binding.gifit3.setVisibility(View.GONE);
                dialog.show();
            }
        });

        binding.gifit4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wonCoins.setText(val4+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                addCoins(val4);

                binding.gifit4.setVisibility(View.GONE);
                dialog.show();
            }
        });

        binding.gifit5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wonCoins.setText(val5+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                addCoins(val5);

                binding.gifit5.setVisibility(View.GONE);
                dialog.show();
            }
        });

        binding.gifit6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wonCoins.setText(val6+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                addCoins(val6);

                binding.gifit6.setVisibility(View.GONE);
                dialog.show();
            }
        });

        binding.gifit7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wonCoins.setText(val7+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                addCoins(val7);

                binding.gifit7.setVisibility(View.GONE);
                dialog.show();
            }
        });

        binding.gifit8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wonCoins.setText(val8+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                addCoins(val8);

                binding.gifit8.setVisibility(View.GONE);
                dialog.show();
            }
        });

        binding.gifit9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wonCoins.setText(val9+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                addCoins(val9);

                binding.gifit9.setVisibility(View.GONE);
                dialog.show();
            }
        });

        binding.gifit10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wonCoins.setText(val10+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                addCoins(val10);

                binding.gifit10.setVisibility(View.GONE);
                dialog.show();
            }
        });

        binding.gifit11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wonCoins.setText(val11+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                addCoins(val11);

                binding.gifit11.setVisibility(View.GONE);
                dialog.show();
            }
        });

        binding.gifit12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wonCoins.setText(val12+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                addCoins(val12);

                binding.gifit12.setVisibility(View.GONE);
                dialog.show();
            }
        });

        btnGetCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int availableCoins = Integer.parseInt(binding.totalCoin.getText().toString());
                int won = Integer.parseInt(wonCoins.getText().toString());

                int finalCoins = availableCoins + won;
                binding.totalCoin.setText(finalCoins+"");
                dialog.dismiss();

                binding.flipCardTimer.setVisibility(View.VISIBLE);
                timer.start();
                cardDisable();
            }
        });
    }

    private void cardDisable() {

        binding.gifit1.setEnabled(false);
        binding.gifit2.setEnabled(false);
        binding.gifit3.setEnabled(false);
        binding.gifit4.setEnabled(false);
        binding.gifit5.setEnabled(false);
        binding.gifit6.setEnabled(false);
        binding.gifit7.setEnabled(false);
        binding.gifit8.setEnabled(false);
        binding.gifit9.setEnabled(false);
        binding.gifit10.setEnabled(false);
        binding.gifit11.setEnabled(false);
        binding.gifit12.setEnabled(false);
    }

    private void addCoins(int val) {

        firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                .update("coin", FieldValue.increment(val));
    }

    private void resetTimer(){

        timer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {

                binding.flipCardTimer.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {

                timer.cancel();
                binding.flipCardTimer.setVisibility(View.GONE);
                cardEnable();
            }
        };
    }

    private void cardEnable() {

        binding.gifit1.setEnabled(true);
        binding.gifit2.setEnabled(true);
        binding.gifit3.setEnabled(true);
        binding.gifit4.setEnabled(true);
        binding.gifit5.setEnabled(true);
        binding.gifit6.setEnabled(true);
        binding.gifit7.setEnabled(true);
        binding.gifit8.setEnabled(true);
        binding.gifit9.setEnabled(true);
        binding.gifit10.setEnabled(true);
        binding.gifit11.setEnabled(true);
        binding.gifit12.setEnabled(true);
    }

    private void loadCoins() {

        firestore.collection("users").document(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserModel model = documentSnapshot.toObject(UserModel.class);
                if (documentSnapshot.exists()){

                    binding.totalCoin.setText(model.getCoin()+"");
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        Admob.showInterstitial(FlipCardActivity.this,true);
        super.onBackPressed();
    }
}