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

import com.example.tasktogether.R;

import java.util.List;

import Interface.RemoveMemberListener;
import Model.Task;
import Model.User;
import Model.UserTask;
import Model.dao.UserDAO;
import Model.dao.UserTaskDAO;

public class TaskResponsibleArrayAdapter extends ArrayAdapter<UserTask> {
    private LayoutInflater inflater;
    private String loggedUserEmail;

    public TaskResponsibleArrayAdapter(Activity activity, List<UserTask> items, String loggedUserEmail){
        super(activity, R.layout.item_responsible, items);
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.loggedUserEmail = loggedUserEmail;
    }

    public View getView(int position, View row, ViewGroup parent) {
        TaskResponsibleArrayAdapter.ViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.item_responsible, null);
            holder = new TaskResponsibleArrayAdapter.ViewHolder();
            holder.responsibleUsername = (TextView) row.findViewById(R.id.txvResponsibleUserName);

            row.setTag(holder);
        } else {
            holder = (TaskResponsibleArrayAdapter.ViewHolder) row.getTag();
        }

        UserTask userTask = getItem(position);
        UserDAO userDao = new UserDAO(this.getContext());

        if(userDao.searchByEmail(loggedUserEmail).getIdUser() == userTask.getIdUser()){
            holder.responsibleUsername.setText("Você");
        }else{
            holder.responsibleUsername.setText(userDao.searchById(userTask.getIdUser()).getName());
        }

        return row;
    }

    static class ViewHolder {
        public TextView responsibleUsername;
    }

}
