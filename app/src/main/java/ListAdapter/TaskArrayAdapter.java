package ListAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.tasktogether.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Interface.RemoveMemberListener;
import Model.Task;
import Model.User;
import Model.UserTask;
import Model.dao.TaskDAO;
import Model.dao.UserTaskDAO;

public class TaskArrayAdapter extends ArrayAdapter<Task> {
    private LayoutInflater inflater;
    private Context context;
    private List<Task> tasks;

    public TaskArrayAdapter(Activity activity, List<Task> items){
        super(activity, R.layout.item_task, items);
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = activity;
        this.tasks = items;
    }

    public void updateTaskList(List<Task> newTasks) {
        this.tasks.clear();
        this.tasks.addAll(newTasks);
        notifyDataSetChanged();
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    public View getView(int position, View row, ViewGroup parent) {
        TaskArrayAdapter.ViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.item_task, null);
            holder = new TaskArrayAdapter.ViewHolder();

            holder.imgTaskIconStatus = (ImageView) row.findViewById(R.id.imgTaskIconStatus);
            holder.txvTaskName = (TextView) row.findViewById(R.id.txvTaskName);
            holder.txvInfoStatus = (TextView) row.findViewById(R.id.txvInfoStatus);
            holder.txvInfoResponsibles = (TextView) row.findViewById(R.id.txvInfoResponsibles);
            holder.iconClockLimitTime = (ImageView) row.findViewById(R.id.iconClockLimitTime);
            holder.txvInfoLimitDate = (TextView) row.findViewById(R.id.txvInfoLimitDate);

            row.setTag(holder);
        } else {
            holder = (TaskArrayAdapter.ViewHolder) row.getTag();
        }

        UserTaskDAO userTaskDao = new UserTaskDAO(this.getContext());
        Task task = getItem(position);

        ArrayList<UserTask> responsibles = userTaskDao.searchAllByTask(task.getIdTask());

        int colorText;
        int colorStatusContainer;

        switch(task.getStatus().toString()){
            case "Pendente":{
                colorText = ContextCompat.getColor(context, R.color.yellow1);
                colorStatusContainer = ContextCompat.getColor(context, R.color.light_yellow);
                break;
            }
            case "Atrasada":{
                colorText = ContextCompat.getColor(context, R.color.red1);
                colorStatusContainer = ContextCompat.getColor(context, R.color.light_red);
                break;
            }
            case "Concluida":{
                colorText = ContextCompat.getColor(context, R.color.green1);
                colorStatusContainer = ContextCompat.getColor(context, R.color.light_green);
                break;
            }
            default: {
                colorText = ContextCompat.getColor(context, R.color.primary4);
                colorStatusContainer = ContextCompat.getColor(context, R.color.primary1);
            }
        }

        holder.imgTaskIconStatus.setImageTintList(ColorStateList.valueOf(colorText));
        holder.txvTaskName.setText(task.getName());

        holder.txvInfoStatus.setText(task.getStatus());
        holder.txvInfoStatus.setBackgroundTintList(ColorStateList.valueOf(colorStatusContainer));

        if(responsibles != null)
            holder.txvInfoResponsibles.setText(responsibles.toArray().length + " responsáveis");
        else
            holder.txvInfoResponsibles.setText("0 responsáveis");

        holder.iconClockLimitTime.setImageTintList(ColorStateList.valueOf(colorText));
        holder.txvInfoLimitDate.setText(task.getLimitDate());

        return row;
    }

    static class ViewHolder {
        public ImageView imgTaskIconStatus;
        public TextView txvTaskName;
        public TextView txvInfoStatus;
        public TextView txvInfoResponsibles;
        public ImageView iconClockLimitTime;
        public TextView txvInfoLimitDate;
    }

}
