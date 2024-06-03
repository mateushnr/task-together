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

import java.util.List;
import java.util.Objects;

import Interface.RemoveMemberListener;
import Model.User;

public class MemberEditArrayAdapter extends ArrayAdapter<User> {
    private final RemoveMemberListener listener;
    private LayoutInflater inflater;
    private Context context;

    public MemberEditArrayAdapter(Activity activity, List<User> items, RemoveMemberListener listener){
        super(activity, R.layout.item_member_group_with_status, items);
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
        this.context = activity;
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    public View getView(int position, View row, ViewGroup parent) {
        MemberEditArrayAdapter.ViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.item_member_group_with_status, null);
            holder = new MemberEditArrayAdapter.ViewHolder();
            holder.memberUsername = (TextView) row.findViewById(R.id.txvMemberUserName);
            holder.btnRemoveMember = (ImageButton) row.findViewById(R.id.btnRemoveMember);
            holder.memberStatusParticipation = (TextView) row.findViewById(R.id.txvMemberStatusParticipation);

            row.setTag(holder);
        } else {
            holder = (MemberEditArrayAdapter.ViewHolder) row.getTag();
        }
        User u = getItem(position);
        holder.memberUsername.setText(u.getName());
        holder.memberStatusParticipation.setText(u.getStatusParticipation());

        int color = ContextCompat.getColor(context, R.color.yellow1);

        if(Objects.equals(u.getStatusParticipation(), "Membro")){
            color = ContextCompat.getColor(context, R.color.blue1);
        }else if(Objects.equals(u.getStatusParticipation(), "Pendente")){
            color = ContextCompat.getColor(context, R.color.orange2);
        }

        holder.memberStatusParticipation.setTextColor(color);

        holder.btnRemoveMember.setOnClickListener(v -> {
            listener.removeMember(u.getIdUser());
        });



        return row;
    }

    static class ViewHolder {
        public TextView memberUsername;
        public TextView memberStatusParticipation;
        public ImageButton btnRemoveMember;
    }

}
