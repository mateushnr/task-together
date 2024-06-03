package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ListAdapter.GroupArrayAdapter;
import Model.Group;
import Model.UserGroup;
import Model.dao.GroupDAO;
import Model.dao.UserDAO;
import Model.dao.UserGroupDAO;

public class MyGroupsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPref";
    private static final String KEY_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String loggedUserEmail = sharedPreferences.getString(KEY_EMAIL, null);

        UserGroupDAO userGroupDao = new UserGroupDAO(this);
        UserDAO userDao = new UserDAO(this);

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

        ListView ltvGroupList = findViewById(R.id.ltvGroupList);
        ltvGroupList.setEmptyView(findViewById(R.id.emptyGroupList));

        GroupDAO groupDao = new GroupDAO(this);

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

        GroupArrayAdapter adapter = new GroupArrayAdapter(this, groups);

        ltvGroupList.setAdapter(adapter);

        List<Group> finalGroups = groups;
        ltvGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentGroupItem = new Intent(getBaseContext(), GroupActivity.class);
                Group selectedGroup = finalGroups.get(position);
                intentGroupItem.putExtra("selectedGroup", selectedGroup.getIdGroup());
                startActivity(intentGroupItem);
            }
        });



        Button btnCreateGroup = findViewById(R.id.btnCreateGroup);

        Intent intentCreateGroup = new Intent(this, CreateGroupActivity.class);
        btnCreateGroup.setOnClickListener(v -> startActivity(intentCreateGroup));

        Button btnNavGroup = findViewById(R.id.btnNavGroups);
        Button btnNavNotification = findViewById(R.id.btnNavNotification);
        Button btnNavProfile = findViewById(R.id.btnNavProfile);

        TextView txvGroupUnderline = findViewById(R.id.txvGroupsUnderline);

        txvGroupUnderline.setVisibility(View.VISIBLE);
        btnNavGroup.setTextColor(getResources().getColor(R.color.primary4));

        btnNavGroup.setClickable(false);

        Intent intentNotification = new Intent(this, MyNotificationActivity.class);
        btnNavNotification.setOnClickListener(v -> startActivity(intentNotification));

        Intent intentProfile = new Intent(this, ProfileActivity.class);
        btnNavProfile.setOnClickListener(v -> startActivity(intentProfile));

    }
}