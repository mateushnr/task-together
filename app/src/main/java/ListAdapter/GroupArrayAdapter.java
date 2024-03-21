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

import Model.Group;

public class GroupArrayAdapter extends ArrayAdapter<Group> {
    private LayoutInflater inflater;

    public GroupArrayAdapter(Activity activity, List<Group> items){
        super(activity, R.layout.item_group, items);
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View row, ViewGroup parent) {
        ViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.item_group, null);
            holder = new ViewHolder();
            holder.name = (TextView) row.findViewById(R.id.txvName);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Group g = getItem(position);
        holder.name.setText(g.getName());
        return row;
    }

    static class ViewHolder {
        public TextView name;
    }
}
