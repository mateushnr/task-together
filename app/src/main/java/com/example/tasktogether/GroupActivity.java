package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Model.Group;
import Model.UserGroup;
import Model.dao.GroupDAO;
import Model.dao.UserDAO;
import Model.dao.UserGroupDAO;

public class GroupActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPref";
    private static final String KEY_EMAIL = "email";

    Button navAllTasks;
    Button navPendingTasks;
    Button navCompletedTasks;
    Button navLateTasks;

    TextView txvAllTasksUnderline;
    TextView txvPendingTasksUnderline;
    TextView txvCompletedTasksUnderline;
    TextView txvLateTasksUnderline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String loggedUserEmail = sharedPreferences.getString(KEY_EMAIL, null);

        UserGroupDAO userGroupDao = new UserGroupDAO(this);
        UserDAO userDao = new UserDAO(this);
        GroupDAO groupDao = new GroupDAO(this);

        TextView txvAmountNotification = findViewById(R.id.txvAmountNotification);
        int amountNotification = 0;

        List<UserGroup> userGroupsInvitations = new ArrayList<>();

        if(loggedUserEmail != null){
            userGroupsInvitations = userGroupDao.searchAllInvitations(userDao.searchByEmail(loggedUserEmail).getIdUser());

            amountNotification = userGroupsInvitations.size();
            if(amountNotification > 0){
                txvAmountNotification.setText("" + amountNotification);
                txvAmountNotification.setVisibility(View.VISIBLE);
            }
        }

        List<Group> groups = new ArrayList<>();

        if(loggedUserEmail == null){
            groups = groupDao.searchAllOffline();
        }else{
            List<UserGroup> userGroupUserIsMember = userGroupDao.searchAllWhereUserIsMember(userDao.searchByEmail(loggedUserEmail).getIdUser());

            for (UserGroup userGroup : userGroupUserIsMember) {
                int groupId = userGroup.getIdGroup();
                Group group = groupDao.searchById(groupId);

                if (group != null) {
                    groups.add(group);
                }
            }
        }

        Intent intentGroup = getIntent();

        if(intentGroup.hasExtra("position")){
            int i = intentGroup.getIntExtra("position", 0);
            Group g = groups.get(i);
            TextView txvName = findViewById(R.id.txvGroupName);
            txvName.setText("" + g.getName());

        }

        navAllTasks = findViewById(R.id.navAllTasks);
        navPendingTasks = findViewById(R.id.navPendingTasks);
        navCompletedTasks = findViewById(R.id.navCompletedTasks);
        navLateTasks = findViewById(R.id.navLateTasks);

        txvAllTasksUnderline = findViewById(R.id.txvAllTasksUnderline);
        txvPendingTasksUnderline = findViewById(R.id.txvPendingTasksUnderline);
        txvCompletedTasksUnderline = findViewById(R.id.txvCompletedTasksUnderline);
        txvLateTasksUnderline = findViewById(R.id.txvLateTasksUnderline);

        navAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTaskNavClick(v);
            }
        });
        navPendingTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTaskNavClick(v);
            }
        });
        navCompletedTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTaskNavClick(v);
            }
        });
        navLateTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTaskNavClick(v);
            }
        });

        Button btnNavGroup = findViewById(R.id.btnNavGroups);
        Button btnNavNotification = findViewById(R.id.btnNavNotification);
        Button btnNavProfile = findViewById(R.id.btnNavProfile);

        ImageButton imgButtonGoBack = findViewById(R.id.imgButtonGoBack);
        imgButtonGoBack.setOnClickListener(v -> finish());

        TextView txvGroupUnderline = findViewById(R.id.txvGroupsUnderline);

        txvGroupUnderline.setVisibility(View.VISIBLE);
        btnNavGroup.setTextColor(getResources().getColor(R.color.primary4));

        btnNavGroup.setClickable(false);

        Intent intentNotification = new Intent(this, MyNotificationActivity.class);
        btnNavNotification.setOnClickListener(v -> startActivity(intentNotification));

        Intent intentProfile = new Intent(this, ProfileActivity.class);
        btnNavProfile.setOnClickListener(v -> startActivity(intentProfile));
    }

    public void handleTaskNavClick(View view) {
        int id = view.getId();

        txvAllTasksUnderline.setVisibility(View.INVISIBLE);
        txvPendingTasksUnderline.setVisibility(View.INVISIBLE);
        txvCompletedTasksUnderline.setVisibility(View.INVISIBLE);
        txvLateTasksUnderline.setVisibility(View.INVISIBLE);

        navAllTasks.setTextColor(getResources().getColor(R.color.gray7));
        navPendingTasks.setTextColor(getResources().getColor(R.color.gray7));
        navCompletedTasks.setTextColor(getResources().getColor(R.color.gray7));
        navLateTasks.setTextColor(getResources().getColor(R.color.gray7));

        String clickedButtonName = getResources().getResourceEntryName(id);

        switch (clickedButtonName) {
            case "navAllTasks":
                txvAllTasksUnderline.setVisibility(View.VISIBLE);
                navAllTasks.setTextColor(getResources().getColor(R.color.primary7));
                break;
            case "navPendingTasks":
                txvPendingTasksUnderline.setVisibility(View.VISIBLE);
                navPendingTasks.setTextColor(getResources().getColor(R.color.primary7));
                break;
            case "navCompletedTasks":
                txvCompletedTasksUnderline.setVisibility(View.VISIBLE);
                navCompletedTasks.setTextColor(getResources().getColor(R.color.primary7));
                break;
            case "navLateTasks":
                txvLateTasksUnderline.setVisibility(View.VISIBLE);
                navLateTasks.setTextColor(getResources().getColor(R.color.primary7));
                break;
        }
    }
}