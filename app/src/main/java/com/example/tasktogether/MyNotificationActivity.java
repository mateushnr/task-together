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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ListAdapter.NotificationArrayAdapter;
import Model.UserGroup;
import Model.dao.UserDAO;
import Model.dao.UserGroupDAO;

public class MyNotificationActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPref";
    private static final String KEY_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String loggedUserEmail = sharedPreferences.getString(KEY_EMAIL, null);

        UserGroupDAO userGroupDao = new UserGroupDAO(this);

        UserDAO userDao = new UserDAO(this);

        ListView ltvNotificationList = findViewById(R.id.ltvNotificationList);
        ltvNotificationList.setEmptyView(findViewById(R.id.emptyNotificationList));

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

        NotificationArrayAdapter adapter = new NotificationArrayAdapter(this, userGroupsInvitations);

        ltvNotificationList.setAdapter(adapter);

        ltvNotificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentNotificationItem = new Intent(getBaseContext(), NotificationActivity.class);
                intentNotificationItem.putExtra("position", position);
                startActivity(intentNotificationItem);
            }
        });

        Button btnNavGroup = findViewById(R.id.btnNavGroups);
        Button btnNavNotification = findViewById(R.id.btnNavNotification);
        Button btnNavProfile = findViewById(R.id.btnNavProfile);

        TextView txvNotificationUnderline = findViewById(R.id.txvNotificationUnderline);

        txvNotificationUnderline.setVisibility(View.VISIBLE);
        btnNavNotification.setTextColor(getResources().getColor(R.color.primary4));

        btnNavNotification.setClickable(false);

        Intent intentGroups= new Intent(this, MyGroupsActivity.class);
        btnNavGroup.setOnClickListener(v -> startActivity(intentGroups));

        Intent intentProfile = new Intent(this, ProfileActivity.class);
        btnNavProfile.setOnClickListener(v -> startActivity(intentProfile));
    }
}