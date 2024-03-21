package ListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tasktogether.R;

import java.util.List;

import Model.UserGroup;

public class NotificationArrayAdapter extends ArrayAdapter<UserGroup> {
    private LayoutInflater inflater;

    public NotificationArrayAdapter(Activity activity, List<UserGroup> items){
        super(activity, R.layout.item_notification, items);
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View row, ViewGroup parent) {
        NotificationArrayAdapter.ViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.item_notification, null);
            holder = new NotificationArrayAdapter.ViewHolder();
            holder.senderNumber = (TextView) row.findViewById(R.id.txvSenderNumber);
            row.setTag(holder);
        } else {
            holder = (NotificationArrayAdapter.ViewHolder) row.getTag();
        }
        UserGroup ug = getItem(position);
        holder.senderNumber.setText(ug.getUserPhone());
        return row;
    }

    static class ViewHolder {
        public TextView senderNumber;
    }

}
