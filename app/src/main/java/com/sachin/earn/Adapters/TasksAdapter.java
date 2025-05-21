package com.sachin.earn.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sachin.earn.FlipCardActivity;
import com.sachin.earn.GuessNumberActivity;
import com.sachin.earn.LuckyActivity;
import com.sachin.earn.Models.TasksModel;
import com.sachin.earn.R;
import com.sachin.earn.ReferActivity;
import com.sachin.earn.ScratchCardActivity;
import com.sachin.earn.SpinnerActivity;
import com.sachin.earn.WatchVideoActivity;
import com.sachin.earn.databinding.RvTasksDesignBinding;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.viewHolder> {

    Context context;
    ArrayList<TasksModel>list;

    public TasksAdapter(Context context, ArrayList<TasksModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rv_tasks_design, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksAdapter.viewHolder holder, int postion) {

        TasksModel model = list.get(postion);

        holder.binding.tasksName.setText(model.getName());
        holder.binding.tasksImage.setImageResource(model.getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (postion==0){
                    Intent intent = new Intent(context, SpinnerActivity.class);
                    context.startActivity(intent);
                } else if (postion==1) {

                    Intent intent = new Intent(context, ScratchCardActivity.class);
                    context.startActivity(intent);
                }
                else if (postion==2){

                    Intent intent = new Intent(context, GuessNumberActivity.class);
                    context.startActivity(intent);
                }
                else if (postion==3){

                    Intent intent = new Intent(context, FlipCardActivity.class);
                    context.startActivity(intent);
                }
                else if (postion==4){

                    Intent intent = new Intent(context, LuckyActivity.class);
                    context.startActivity(intent);
                }else if(postion==5){
                    Intent intent = new Intent(context, WatchVideoActivity.class);
                    context.startActivity(intent);

                }else if (postion==6){

                    Intent intent = new Intent(context, ReferActivity.class);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RvTasksDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = RvTasksDesignBinding.bind(itemView);
        }
    }
}
