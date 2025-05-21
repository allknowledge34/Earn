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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sachin.earn.Models.UserModel;
import com.sachin.earn.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setStatusBarColor();
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Your Account");
        progressDialog.setMessage("Your Account Is Creating");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.edtName.getText().toString();
                String email = binding.edtEmail.getText().toString();
                String password = binding.edtPassword.getText().toString();

                if (name.isEmpty()){

                    binding.edtName.setError("Enter Your Name");
                } else if (email.isEmpty()) {

                    binding.edtEmail.setError("Enter Your Valid Email");
                }else if (password.isEmpty()) {

                    binding.edtPassword.setError("Enter Password");
                }else {

                    progressDialog.show();
                    storeUserData(name,email,password);
                }
            }
        });
    }

    private void storeUserData(String name, String email, String password) {

        auth.createUserWithEmailAndPassword(binding.edtEmail.getText().toString(),binding.edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    String referCode = email.substring(0,email.lastIndexOf("@"));
                    UserModel model = new UserModel(name,email,password,
                            "https://firebasestorage.googleapis.com/v0/b/earning-b8942.firebasestorage.app/o/account.png?alt=media&token=0ef08dd9-6b13-4da2-a39f-500cff3cf4f0",
                            referCode,
                            "false",
                            5,
                            5,
                            5);

                    String id = task.getResult().getUser().getUid();

                    firestore.collection("users").document(id).set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                progressDialog.dismiss();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(SignUpActivity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
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