package com.sachin.earn;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sachin.earn.AdmobAds.Admob;
import com.sachin.earn.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setStatusBarColor();
        setContentView(binding.getRoot());

        Admob.loadInterstitial(this);
        Admob.LoadVideoRewarded(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }

    private void setStatusBarColor() {
        Window window = getWindow();
        int statusBarColor = ContextCompat.getColor(this,R.color.lightBlue);
        window.setStatusBarColor(statusBarColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}