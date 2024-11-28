package com.codebee.mytasks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolderClass> {

    private ArrayList<MyTask> myArr;
    private Context context;

    public TaskAdapter(ArrayList<MyTask> myArr) {
        this.myArr = myArr;
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task,parent,false);
        context = parent.getContext();
        return new ViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        if(myArr.get(position).getTimestamp() < System.currentTimeMillis()){
            holder.complete_img.setVisibility(View.VISIBLE);
        }else{
            holder.complete_img.setVisibility(View.GONE);
            holder.label_txt.setText(myArr.get(position).getLabel());
            holder.desc_txt.setText(myArr.get(position).getDescription());
            holder.date_txt.setText(myArr.get(position).getDate());
            holder.time_txt.setText(myArr.get(position).getTime());
        }
    }

    @Override
    public int getItemCount() {
        return myArr.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{

        public TextView label_txt,desc_txt,date_txt,time_txt;
        ImageView edit_img,delete_img,complete_img;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);

            label_txt = itemView.findViewById(R.id.item_task_label_text);
            desc_txt = itemView.findViewById(R.id.item_task_desc_text);
            date_txt = itemView.findViewById(R.id.item_task_date_text);
            time_txt = itemView.findViewById(R.id.item_task_time_text);
            edit_img = itemView.findViewById(R.id.item_task_edit_image);
            delete_img = itemView.findViewById(R.id.item_task_delete_image);
            complete_img = itemView.findViewById(R.id.item_task_completed_image);

            delete_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper db = new DatabaseHelper(context);
                    int delete = db.deleteData(myArr.get(getAdapterPosition()).getId());

                    if(delete > 0){
                        myArr.remove(getAdapterPosition());
                        notifyDataSetChanged();
                        notifyItemRangeChanged(getAdapterPosition(),myArr.size());
                        Toast.makeText(context,"Task deleted!",Toast.LENGTH_SHORT).show();
                        ((MainActivity)context).loadTasks();
                    }else{
                        Toast.makeText(context,"Unable to delete task!",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            edit_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,UpdateTaskActivity.class);
                    intent.putExtra("task",myArr.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

            complete_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"This task is completed.",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
