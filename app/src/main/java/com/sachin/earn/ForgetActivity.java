package com.sachin.earn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.sachin.earn.databinding.ActivityForgetBinding;

public class ForgetActivity extends AppCompatActivity {

    ActivityForgetBinding binding;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetBinding.inflate(getLayoutInflater());
        setStatusBarColor();
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");

        binding.btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = binding.edtForgetEmail.getText().toString();

                if (email.isEmpty()){

                    binding.edtForgetEmail.setError("Enter Valid Email");
                }else {

                    progressDialog.show();

                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                progressDialog.dismiss();

                                Toast.makeText(ForgetActivity.this, "Please Check Your Email", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgetActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {

                                progressDialog.dismiss();
                                Toast.makeText(ForgetActivity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        binding.BacktoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ForgetActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
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