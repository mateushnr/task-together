package ListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tasktogether.CreateGroupActivity;
import com.example.tasktogether.R;

import java.util.List;

import Interface.RemoveMemberListener;
import Model.UserGroup;

public class MemberArrayAdapter extends ArrayAdapter<UserGroup> {
    private final RemoveMemberListener listener;
    private LayoutInflater inflater;

    public MemberArrayAdapter(Activity activity, List<UserGroup> items, RemoveMemberListener listener){
        super(activity, R.layout.item_member_group, items);
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
    }

    public View getView(int position, View row, ViewGroup parent) {
        MemberArrayAdapter.ViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.item_member_group, null);
            holder = new MemberArrayAdapter.ViewHolder();
            holder.memberUsername = (TextView) row.findViewById(R.id.txvMemberUserName);
            holder.btnRemoveMember = (ImageButton) row.findViewById(R.id.btnRemoveMember);

            row.setTag(holder);
        } else {
            holder = (MemberArrayAdapter.ViewHolder) row.getTag();
        }
        UserGroup ug = getItem(position);
        holder.memberUsername.setText(ug.getUserName());
        holder.btnRemoveMember.setOnClickListener(v -> {
            listener.onRemoveMember(ug.getIdUser()); // Call onRemoveMember
        });

        return row;
    }

    static class ViewHolder {
        public TextView memberUsername;
        public ImageButton btnRemoveMember;
    }

}
