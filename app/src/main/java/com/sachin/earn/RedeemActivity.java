package com.sachin.earn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sachin.earn.Adapters.PaypalRedeemAdapter;
import com.sachin.earn.Adapters.RedeemAdapter;
import com.sachin.earn.Models.PaymentRequestModel;
import com.sachin.earn.Models.RedeemModel;
import com.sachin.earn.Models.UserModel;
import com.sachin.earn.databinding.ActivityRedeemBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RedeemActivity extends AppCompatActivity {

    ActivityRedeemBinding binding;
    ArrayList<RedeemModel> list;
    ArrayList<RedeemModel> paypalList;
    RedeemAdapter adapter;
    PaypalRedeemAdapter paypalRedeemAdapter;
    Dialog dialog;
    AppCompatButton cancelBtn, redeemBtn;
    ImageView paymentMethodLog;
    TextView paymentMethod;
    EditText edtAmount, edtNumber;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    int availableCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRedeemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getData();


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.esewa_payment_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
        }


        cancelBtn = dialog.findViewById(R.id.cancelBtn);
        redeemBtn = dialog.findViewById(R.id.redeemAmountBtn);
        edtAmount = dialog.findViewById(R.id.edtAmount);
        edtNumber = dialog.findViewById(R.id.tranNumber);
        paymentMethodLog = dialog.findViewById(R.id.trLogo);
        paymentMethod = dialog.findViewById(R.id.payMethods);


        binding.back.setOnClickListener(v -> onBackPressed());


        list = new ArrayList<>();
        list.add(new RedeemModel(10000, R.drawable.phonepe));
        list.add(new RedeemModel(15000, R.drawable.phonepe));
        list.add(new RedeemModel(25000, R.drawable.phonepe));
        list.add(new RedeemModel(40000, R.drawable.phonepe));
        list.add(new RedeemModel(45000, R.drawable.phonepe));
        list.add(new RedeemModel(50000, R.drawable.phonepe));


        binding.recyPaytm.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new RedeemAdapter(this, list, position -> {
            int requiredCoins = list.get(position).getCoins();
            checkCoinPaytm("Please collect more coins", requiredCoins);
        });
        binding.recyPaytm.setAdapter(adapter);


        paypalList = new ArrayList<>();
        paypalList.add(new RedeemModel(41500, R.drawable.paypal));
        paypalList.add(new RedeemModel(83000, R.drawable.paypal));
        paypalList.add(new RedeemModel(90000, R.drawable.paypal));
        paypalList.add(new RedeemModel(95000, R.drawable.paypal));


        binding.recyPaypal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        paypalRedeemAdapter = new PaypalRedeemAdapter(this, paypalList, position -> {
            int requiredCoins = paypalList.get(position).getCoins();
            checkCoin("Please collect more coins", requiredCoins);
        });
        binding.recyPaypal.setAdapter(paypalRedeemAdapter);


        cancelBtn.setOnClickListener(view -> dialog.dismiss());


        redeemBtn.setOnClickListener(view -> {
            String withdrawalMethod = paymentMethod.getText().toString().trim();
            String enteredAmount = edtAmount.getText().toString().trim();
            String enteredNumber = edtNumber.getText().toString().trim();

            if (withdrawalMethod.isEmpty() || enteredAmount.isEmpty() || enteredNumber.isEmpty()) {
                Toast.makeText(RedeemActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int requestCoin = Integer.parseInt(enteredAmount);
            uploadRedeemReq(withdrawalMethod, enteredNumber, requestCoin);
        });
    }

    private void getData() {
        firestore.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserModel model = documentSnapshot.toObject(UserModel.class);
                        if (model != null) {
                            availableCoin = model.getCoin();
                            binding.totalCoin.setText(String.valueOf(model.getCoin()));
                            binding.avilableCoins.setText(String.valueOf(model.getCoin()));
                        }
                    }
                });
    }

    private void uploadRedeemReq(String withdrawalMethod, String mobNumber, int requestCoin) {
        String date = new SimpleDateFormat("dd-MM-yy").format(Calendar.getInstance().getTime());

        PaymentRequestModel model = new PaymentRequestModel(
                withdrawalMethod,
                mobNumber,
                "false",
                date,
                requestCoin
        );

        firestore.collection("redeem")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("request")
                .document()
                .set(model)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        updateCoin(requestCoin);
                    } else {
                        Toast.makeText(RedeemActivity.this, "Failed to redeem", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateCoin(int requestCoin) {
        firestore.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coin", FieldValue.increment(-requestCoin))
                .addOnSuccessListener(unused -> {
                    firestore.collection("users")
                            .document(FirebaseAuth.getInstance().getUid())
                            .update("redeemStatus", "true")
                            .addOnSuccessListener(unused1 -> {
                                Toast.makeText(RedeemActivity.this, "Transaction completed. Check transaction history.", Toast.LENGTH_SHORT).show();
                                getData();
                                dialog.dismiss();
                            });
                });
    }

    public void checkCoin(String message, int coin) {
        if (availableCoin >= coin) {
            paymentMethodLog.setImageResource(R.drawable.paypal);
            paymentMethod.setText("Paypal");
            edtAmount.setText(String.valueOf(coin));
            edtNumber.setHint("Enter Paypal number");
            dialog.show();
        } else {
            Toast.makeText(RedeemActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void checkCoinPaytm(String message, int coin) {
        if (availableCoin >= coin) {
            paymentMethodLog.setImageResource(R.drawable.phonepe);
            paymentMethod.setText("PhonePe");
            edtAmount.setText(String.valueOf(coin));
            edtNumber.setHint("Enter PhonePe number");
            dialog.show();
        } else {
            Toast.makeText(RedeemActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
