package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Model.User;
import Model.UserGroup;
import Model.dao.GroupDAO;
import Model.dao.UserDAO;
import Model.dao.UserGroupDAO;

public class NotificationActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPref";
    private static final String KEY_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

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

        Intent intentNotification = getIntent();

        if(intentNotification.hasExtra("position")){
            int i = intentNotification.getIntExtra("position", 0);
            UserGroup ug = userGroupsInvitations.get(i);
            GroupDAO groupDao = new GroupDAO(this);

            User userSender = userDao.searchById(ug.getIdWhoInvited());

            TextView txvUserName = findViewById(R.id.txvUserName);
            txvUserName.setText(userSender.getName());

            TextView txvUserPhone = findViewById(R.id.txvUserPhone);
            txvUserPhone.setText(userSender.getPhone());

            String messageNotification = "O usuário " + userSender.getName() + " te convidou para fazer parte do grupo " + groupDao.searchById(ug.getIdGroup()).getName();
            TextView txvMessageNotification= findViewById(R.id.txvMessageNotification);
            txvMessageNotification.setText(messageNotification);

            Button btnConfirm = findViewById(R.id.btnConfirm);
            Button btnRefuse = findViewById(R.id.btnRefuse);

            Intent actionInvite = new Intent(this, MyGroupsActivity.class);

            btnConfirm.setOnClickListener(v -> {
                ug.setStatusParticipation("Membro");
                userGroupDao.edit(ug);

                finish();

                startActivity(actionInvite);

                Toast.makeText(this, "Convite aceito!",
                        Toast.LENGTH_SHORT).show();
            });

            btnRefuse.setOnClickListener(v -> {
                userGroupDao.delete(ug.getIdUserGroup());

                finish();

                startActivity(actionInvite);

                Toast.makeText(this, "Convite recusado!",
                        Toast.LENGTH_SHORT).show();
            });
        }

        Button btnNavGroup = findViewById(R.id.btnNavGroups);
        Button btnNavNotification = findViewById(R.id.btnNavNotification);
        Button btnNavProfile = findViewById(R.id.btnNavProfile);

        ImageButton imgButtonGoBack = findViewById(R.id.imgButtonGoBack);
        imgButtonGoBack.setOnClickListener(v -> finish());

        TextView txvNotificationUnderline = findViewById(R.id.txvNotificationUnderline);

        txvNotificationUnderline.setVisibility(View.VISIBLE);
        btnNavNotification.setTextColor(getResources().getColor(R.color.primary4));

        btnNavNotification.setOnClickListener(v -> finish());

        Intent intentGroups= new Intent(this, MyGroupsActivity.class);
        btnNavGroup.setOnClickListener(v -> startActivity(intentGroups));

        Intent intentProfile = new Intent(this, ProfileActivity.class);
        btnNavProfile.setOnClickListener(v -> startActivity(intentProfile));
    }
}