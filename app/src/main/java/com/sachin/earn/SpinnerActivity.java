package com.sachin.earn;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

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
import com.sachin.earn.SpinWheel.LuckyWheelView;
import com.sachin.earn.SpinWheel.model.LuckyItem;
import com.sachin.earn.databinding.ActivitySpinnerBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpinnerActivity extends AppCompatActivity {

    ActivitySpinnerBinding binding;
    CountDownTimer timer;
    private List<LuckyItem> data = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    private int CurrentCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadCoins();
        resetTimer();
        addSpinItems();

        Admob.loadBannerAd(findViewById(R.id.bannerAds),SpinnerActivity.this);
        binding.wheelview.setData(data);
        binding.wheelview.setRound(5);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        
        binding.spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random r= new Random();
                int randomNumber = r.nextInt(data.size());
                
                binding.wheelview.startLuckyWheelWithTargetIndex(randomNumber);
            }
        });
        
        binding.wheelview.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                updateCash(index);
            }
        });
    }


    private void addSpinItems() {
        data.clear();

        int[] coins = {0, 10, 15, 20, 25, 30, 35, 5};
        String[] textColors = {"#212121", "#00cf00", "#212121", "#7f00d9", "#212121", "#dc0000", "#212121", "#008bff"};
        String[] bgColors = {"#eceff1", "#ffffff", "#eceff1", "#ffffff", "#eceff1", "#ffffff", "#eceff1", "#ffffff"};

        for (int i = 0; i < coins.length; i++) {
            LuckyItem item = new LuckyItem();
            item.topText = String.valueOf(coins[i]);
            item.secondaryText = "COINS";
            item.textColor = Color.parseColor(textColors[i]);
            item.color = Color.parseColor(bgColors[i]);
            data.add(item);
        }
    }
    private void updateCash(int index) {

        Admob.showRewarded(SpinnerActivity.this, true);

        int coinValue = Integer.parseInt(data.get(index).topText);
        firestore.collection("users").document(auth.getUid()).update("coin", FieldValue.increment(coinValue)).addOnSuccessListener(unused -> {

            Toast.makeText(this,coinValue+ "Coin Added Successfully", Toast.LENGTH_SHORT).show();
            loadCoins();
            binding.timer.setVisibility(View.VISIBLE);
            timer.start();
        });
    }

    private void resetTimer(){

        timer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {

                binding.timer.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                
                timer.cancel();
                binding.timer.setVisibility(View.GONE);
            }
        };
    }

    private void loadCoins() {

        firestore.collection("users").document(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserModel model = documentSnapshot.toObject(UserModel.class);
                if (documentSnapshot.exists() && model != null) {

                    binding.totalCoin.setText(String.valueOf(model.getCoin()));
                    CurrentCoin = model.getCoin();
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        Admob.showInterstitial(SpinnerActivity.this,true);
        super.onBackPressed();
    }
}