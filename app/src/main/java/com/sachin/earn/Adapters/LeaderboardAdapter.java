package com.sachin.earn.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sachin.earn.Models.TasksModel;
import com.sachin.earn.Models.UserModel;
import com.sachin.earn.R;
import com.sachin.earn.databinding.RvLeaderboardBinding;
import com.sachin.earn.databinding.RvTasksDesignBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.viewHolder> {

    Context context;
    ArrayList<UserModel> list;
    private int pos=1;

    RankListener listener;

    public LeaderboardAdapter(Context context, ArrayList<UserModel> list, RankListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rv_leaderboard, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        UserModel model = list.get(position);

        holder.binding.rankName.setText(model.getName());
        holder.binding.rankCoin.setText(String.valueOf(model.getCoin()));
        holder.binding.rankNum.setText(pos+"");

        Picasso.get()
                .load(model.getProfile())
                .placeholder(R.drawable.friend_2)
                .into(holder.binding.profileImage);

        pos++;

        if (position==0){

            holder.binding.cardView.setVisibility(View.GONE);
            holder.binding.rankNum.setVisibility(View.GONE);
        }

        else if (position==1){

            holder.binding.cardView.setVisibility(View.GONE);
            holder.binding.rankNum.setVisibility(View.GONE);
        }
        else if (position==2){

            holder.binding.cardView.setVisibility(View.GONE);
            holder.binding.rankNum.setVisibility(View.GONE);
        }

        listener.rankItem(position,model.getName(),model.getProfile(),model.getCoin(),list);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RvLeaderboardBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = RvLeaderboardBinding.bind(itemView);
        }
    }

    public interface  RankListener{


        public void rankItem(int position, String name, String profile, int coin, ArrayList<UserModel> list);
    }
}
