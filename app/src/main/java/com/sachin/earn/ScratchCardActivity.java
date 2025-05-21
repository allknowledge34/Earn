package com.sachin.earn;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sachin.earn.AdmobAds.Admob;
import com.sachin.earn.Models.UserModel;
import com.sachin.earn.databinding.ActivityScratchCardBinding;

import java.util.Random;

public class ScratchCardActivity extends AppCompatActivity {

    ActivityScratchCardBinding binding;
    CountDownTimer timer;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    private int currentCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScratchCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadCoins();
        resetTimer();

        Admob.loadBannerAd(findViewById(R.id.bannerAds),ScratchCardActivity.this);
        Random random = new Random();
        int val = random.nextInt(100);
        binding.scratchCoin.setText(val+"");

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        binding.scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {

                Admob.showRewarded(ScratchCardActivity.this, true);
                Random randoms = new Random();
                int val = randoms.nextInt(100);
                binding.scratchCoin.setText(val+"");

                int scratchedCoin = Integer.parseInt(binding.scratchCoin.getText().toString());

                firestore.collection("users").document(auth.getUid()).update("coin", FieldValue.increment(scratchedCoin));

                Toast.makeText(ScratchCardActivity.this,val+ "Coin Added Successfully", Toast.LENGTH_SHORT).show();

                loadCoins();
                binding.scratchTime.setVisibility(View.VISIBLE);
                timer.start();
            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                if (percent>0.5){

                    Log.d("Reveal Percentags","onRevealPercentChangedListener:"+ String.valueOf(percent));
                }
            }
        });
    }

    private void loadCoins() {

        firestore.collection("users").document(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserModel model = documentSnapshot.toObject(UserModel.class);
                if (documentSnapshot.exists()){

                    binding.totalCoin.setText(model.getCoin()+"");
                    currentCoins = model.getCoin();
                }
            }
        });
    }

    private void resetTimer(){

        timer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {

                binding.scratchTime.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {

                binding.scratchView.mask();
                timer.cancel();
                binding.scratchTime.setVisibility(View.GONE);
            }
        };
    }

    @Override
    public void onBackPressed(){
        Admob.showInterstitial(ScratchCardActivity.this,true);
        super.onBackPressed();
    }
}