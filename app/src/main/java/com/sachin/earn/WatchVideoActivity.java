package com.sachin.earn;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sachin.earn.AdmobAds.Admob;
import com.sachin.earn.AdmobAds.AdsUnit;
import com.sachin.earn.Models.UserModel;
import com.sachin.earn.databinding.ActivityWatchVideoBinding;

import java.util.Random;

public class WatchVideoActivity extends AppCompatActivity {

    ActivityWatchVideoBinding binding;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWatchVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Admob.loadBannerAd(findViewById(R.id.bannerAd),WatchVideoActivity.this);


        loadData();
        binding.watchVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showRewarded(true);
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

    }



    void showRewarded(boolean isReload){

        if (AdsUnit.mRewardedAd != null) {

            AdsUnit.mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d(TAG, "Ad was clicked.");
                }
                @Override
                public void onAdDismissedFullScreenContent() {

                    if (isReload){

                        AdsUnit.mRewardedAd = null;
                        Admob.LoadVideoRewarded(WatchVideoActivity.this);
                    }
                    loadData();

                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    Log.e(TAG, "Ad failed to show fullscreen content.");
                    AdsUnit.mRewardedAd = null;
                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "Ad showed fullscreen content.");
                }
            });


            AdsUnit.mRewardedAd.show(WatchVideoActivity.this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                    updateCoins();
                }


            });

        }

    }

    public void updateCoins() {

        Random random = new Random();
        int val = random.nextInt(200);


        firestore
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coin", FieldValue.increment(val));

        Toast.makeText(this, val+" Coins Added Successfully", Toast.LENGTH_SHORT).show();

    }

    private void loadData() {

        firestore.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
        Admob.showInterstitial(WatchVideoActivity.this,true);
        super.onBackPressed();
    }
}