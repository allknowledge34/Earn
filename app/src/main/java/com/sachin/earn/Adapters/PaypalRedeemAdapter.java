package com.sachin.earn.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sachin.earn.Models.RedeemModel;
import com.sachin.earn.R;
import com.sachin.earn.databinding.ItemPaymentGetwayBinding;

import java.util.ArrayList;

public class PaypalRedeemAdapter extends RecyclerView.Adapter<PaypalRedeemAdapter.viewHolder> {

    Context context;
    ArrayList<RedeemModel>list;
    AddListener listener;

    public PaypalRedeemAdapter(Context context, ArrayList<RedeemModel> list, AddListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_payment_getway,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        RedeemModel model = list.get(position);

        holder.binding.btnWithd.setText(model.getCoins()+"");
        holder.binding.paymentIcon.setImageResource(model.getIcon());

        holder.binding.btnWithd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onLongClick(position);

            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ItemPaymentGetwayBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemPaymentGetwayBinding.bind(itemView);
        }
    }

    public interface AddListener{

        public void onLongClick(int position);

    }

}
