package com.ent.ticktask.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.ent.ticktask.R;
import com.ent.ticktask.Model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList = new ArrayList<>();
    private OnTaskClickListener listener;

    // اینترفیس برای کلیک روی آیتم‌ها (بعداً برای ویرایش/تیک زدن استفاده می‌کنیم)
    public interface OnTaskClickListener {
        void onTaskClick(Task task);

        void onTaskCheckedChange(Task task, boolean isChecked);
    }

    public TaskAdapter(OnTaskClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task, listener);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // متد آپدیت لیست با استفاده از DiffUtil (برای پرفورمنس بهتر)
    public void setTasks(List<Task> newTasks) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new TaskDiffCallback(taskList, newTasks));
        taskList.clear();
        taskList.addAll(newTasks);
        diffResult.dispatchUpdatesTo(this);
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        CheckBox checkboxCompleted;
        TextView textTitle, textDueDate;
        View viewPriorityIndicator;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
//            checkboxCompleted = itemView.findViewById(R.id.checkboxCompleted);
//            textTitle = itemView.findViewById(R.id.textTitle);
//            textCategory = itemView.findViewById(R.id.textCategory);
//            textDueDate = itemView.findViewById(R.id.textDueDate);
//            viewPriorityIndicator = itemView.findViewById(R.id.viewPriorityIndicator);
        }

        void bind(Task task, OnTaskClickListener listener) {
            textTitle.setText(task.getTitle());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            textDueDate.setText(sdf.format(task.getDueDate()));

            // جلوگیری از فراخوانی listener هنگام bind شدن مجدد (مشکل رایج RecyclerView)
            checkboxCompleted.setOnCheckedChangeListener(null);
            checkboxCompleted.setChecked(task.isCompleted());

            // رنگ نوار اولویت
            int colorRes;
            switch (task.getPriority()) {
                case 2:
                    colorRes = R.color.priority_high;
                    break;
                case 1:
                    colorRes = R.color.priority_medium;
                    break;
                default:
                    colorRes = R.color.priority_low;
            }
            viewPriorityIndicator.setBackgroundColor(
                    itemView.getContext().getResources().getColor(colorRes)
            );

            cardView.setOnClickListener(v -> listener.onTaskClick(task));
            checkboxCompleted.setOnCheckedChangeListener((buttonView, isChecked) ->
                    listener.onTaskCheckedChange(task, isChecked)
            );
        }
    }

    // کلاس کمکی برای مقایسه هوشمند لیست‌ها (فقط آیتم‌های تغییر کرده رو آپدیت می‌کنه)
    static class TaskDiffCallback extends DiffUtil.Callback {
        private final List<Task> oldList;
        private final List<Task> newList;

        TaskDiffCallback(List<Task> oldList, List<Task> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Task oldTask = oldList.get(oldItemPosition);
            Task newTask = newList.get(newItemPosition);
            return oldTask.getTitle().equals(newTask.getTitle())
                    && oldTask.isCompleted() == newTask.isCompleted()
                    && oldTask.getPriority() == newTask.getPriority();
        }
    }
}