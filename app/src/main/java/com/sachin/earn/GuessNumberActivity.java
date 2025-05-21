package com.sachin.earn;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

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
import com.sachin.earn.databinding.ActivityGuessNumberBinding;

import java.util.Random;

public class GuessNumberActivity extends AppCompatActivity {

    ActivityGuessNumberBinding binding;
    CountDownTimer timer;
    private int givenNumber,guessNumber;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    private int CurrentCoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuessNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadCoins();
        resetTimer();

        Admob.loadBannerAd(findViewById(R.id.bannerAds),GuessNumberActivity.this);
        Random random = new Random();
        givenNumber = random.nextInt(60);
        binding.firstCardNum.setText(givenNumber+"");

        Random random2 = new Random();
        guessNumber = random2.nextInt(60);
        binding.secondCardNum.setText(guessNumber+"");

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        binding.btnHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Admob.showRewarded(GuessNumberActivity.this, true);

                binding.question.setVisibility(View.GONE);
                binding.secondCardNum.setVisibility(View.VISIBLE);
                binding.timer.setVisibility(View.VISIBLE);
                timer.start();

                if (guessNumber>givenNumber){

                    firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                            .update("coin", FieldValue.increment(100));

                    loadCoins();

                    binding.result.setText("You Win 100 Coins");
                    binding.result.setVisibility(View.VISIBLE);
                    binding.btnHigh.setEnabled(false);
                    binding.btnLow.setEnabled(false);
                }
                else {

                    firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                            .update("coin", FieldValue.increment(-50));

                    loadCoins();

                    binding.result.setText("You Loss 50 Coins");
                    binding.result.setVisibility(View.VISIBLE);
                    binding.btnHigh.setEnabled(false);
                    binding.btnLow.setEnabled(false);
                }
            }
        });

        binding.btnLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Admob.showRewarded(GuessNumberActivity.this, true);

                binding.question.setVisibility(View.GONE);
                binding.secondCardNum.setVisibility(View.VISIBLE);
                binding.timer.setVisibility(View.VISIBLE);
                timer.start();

                if (guessNumber>givenNumber){

                    firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                            .update("coin", FieldValue.increment(100));

                    loadCoins();

                    binding.result.setText("You Win 100 Coins");
                    binding.result.setVisibility(View.VISIBLE);
                    binding.btnHigh.setEnabled(false);
                    binding.btnLow.setEnabled(false);
                }
                else {

                    firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                            .update("coin", FieldValue.increment(-50));

                    loadCoins();

                    binding.result.setText("You Loss 50 Coins");
                    binding.result.setVisibility(View.VISIBLE);
                    binding.btnHigh.setEnabled(false);
                    binding.btnLow.setEnabled(false);
                }
            }
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

                binding.question.setVisibility(View.VISIBLE);
                binding.secondCardNum.setVisibility(View.GONE);
                timer.cancel();

                binding.timer.setVisibility(View.GONE);

                Random random = new Random();
                givenNumber = random.nextInt(60);
                binding.firstCardNum.setText(givenNumber+"");

                Random random2 = new Random();
                guessNumber = random2.nextInt(60);
                binding.secondCardNum.setText(guessNumber+"");

                binding.btnLow.setEnabled(true);
                binding.btnHigh.setEnabled(true);
                binding.result.setEnabled(true);

            }
        };
    }

    private void loadCoins() {

        firestore.collection("users").document(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserModel model = documentSnapshot.toObject(UserModel.class);
                if (documentSnapshot.exists()){

                    binding.totalCoin.setText(model.getCoin()+"");
                    CurrentCoin = model.getCoin();
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        Admob.showInterstitial(GuessNumberActivity.this,true);
        super.onBackPressed();
    }
}