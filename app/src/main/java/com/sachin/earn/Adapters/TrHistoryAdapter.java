package com.sachin.earn.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sachin.earn.Models.PaymentRequestModel;
import com.sachin.earn.R;
import com.sachin.earn.databinding.ItemHistoryBinding;
import com.sachin.earn.databinding.ItemPaymentGetwayBinding;

import java.util.ArrayList;

public class TrHistoryAdapter extends RecyclerView.Adapter<TrHistoryAdapter.viewHolder> {

    Context context;
    ArrayList<PaymentRequestModel>list;

    public TrHistoryAdapter(Context context, ArrayList<PaymentRequestModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int itemType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        final PaymentRequestModel model = list.get(position);

        String status = model.getStatus();
        int currentCoin = model.getCoins();
        String methode = model.getPaymentMethod();

        holder.binding.payMethod.setText(model.getPaymentMethod());
        holder.binding.paymentDt.setText(model.getPaymentDetails());
        holder.binding.date.setText(model.getDate());

        double earn = currentCoin * 0.001;
        holder.binding.earn.setText("("+"₹ "+earn+""+")");

        if (status.equals("false")){

            holder.binding.payStatus.setText("pending");

        }else {

            holder.binding.payStatus.setText("success");
            holder.binding.payStatus.setBackgroundResource(R.drawable.sucessbtn);
        }

        if (methode.equals("Paypal")){

            holder.binding.logo.setImageResource(R.drawable.paypal);
        } else if (methode.equals("Paytm")) {

            holder.binding.logo.setImageResource(R.drawable.phonepe);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ItemHistoryBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemHistoryBinding.bind(itemView);
        }
    }
}
