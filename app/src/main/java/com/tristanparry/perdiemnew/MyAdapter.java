package com.tristanparry.perdiemnew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.TaskViewHolder> {
    private ArrayList<TaskItem> taskList;
    private OnTaskClickInterface taskListener;

    public interface OnTaskClickInterface {
        void onEdit(int position);
        void onDelete(int position);
    }
    public void setOnTaskClickInterface(OnTaskClickInterface listener) {
        taskListener = listener;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTitle;
        public TextView taskDescription;
        public ImageButton editButton;
        public ImageButton deleteButton;
        public CheckBox checkBox;

        public TaskViewHolder(@NonNull View itemView, final OnTaskClickInterface listener) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            checkBox = itemView.findViewById(R.id.checkBox);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEdit(position);
                        }
                    }
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDelete(position);
                        }
                    }
                }
            });
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDelete(position);
                        }
                    }
                }
            });
        }
    }

    public MyAdapter(ArrayList<TaskItem> constructorTaskList) {
        taskList = constructorTaskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view, taskListener);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskItem currentTask = taskList.get(position);

        holder.taskTitle.setText(currentTask.getTaskTitle());
        holder.taskDescription.setText(currentTask.getTaskDescription());
        holder.checkBox.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}