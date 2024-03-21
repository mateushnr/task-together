package ListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tasktogether.R;

import java.util.List;

import Interface.AddMemberListener;
import Model.User;

public class UserToInviteArrayAdapter extends ArrayAdapter<User> {
    private final AddMemberListener listener;
    private LayoutInflater inflater;

    public UserToInviteArrayAdapter(Activity activity, List<User> items, AddMemberListener listener){
        super(activity, R.layout.item_user_to_invite, items);
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
    }

    public View getView(int position, View row, ViewGroup parent) {
        UserToInviteArrayAdapter.ViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.item_user_to_invite, null);
            holder = new UserToInviteArrayAdapter.ViewHolder();
            holder.UserName = (TextView) row.findViewById(R.id.txvUserNameToInvite);
            holder.UserPhone = (TextView) row.findViewById(R.id.txvUserPhoneToInvite);
            holder.btnAddMember = (ImageButton) row.findViewById(R.id.btnAddUserMember);

            row.setTag(holder);
        } else {
            holder = (UserToInviteArrayAdapter.ViewHolder) row.getTag();
        }
        User u = getItem(position);
        holder.UserName.setText(u.getName());
        holder.UserPhone.setText(u.getPhone());
        holder.btnAddMember.setOnClickListener(v -> {
            listener.addMember(u.getIdUser());
        });

        return row;
    }

    static class ViewHolder {
        public TextView UserName;
        public TextView UserPhone;
        public ImageButton btnAddMember;
    }

}

