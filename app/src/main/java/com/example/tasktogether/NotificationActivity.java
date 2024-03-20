package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Model.UserGroup;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        TextView txvAmountNotification = findViewById(R.id.txvAmountNotification);
        int amountNotification = 0;

        List<UserGroup> userGroups = new ArrayList<UserGroup>();

        userGroups.add(new UserGroup(1, 1, false, "pendente","Atividades Domésticas", "Mateus", "18 99999-9999"));
        userGroups.add(new UserGroup(1, 2,false,"pendente","Lazer", "Jorge", "18 98888-8888"));
        userGroups.add(new UserGroup(1, 3,false,"pendente","Compras do mês", "Lucas", "18 97777-7777"));

        amountNotification = userGroups.size();
        if(amountNotification > 0){
            txvAmountNotification.setText("" + amountNotification);
            txvAmountNotification.setVisibility(View.VISIBLE);
        }

        Intent intentNotification = getIntent();

        if(intentNotification.hasExtra("position")){
            int i = intentNotification.getIntExtra("position", 0);
            UserGroup ug = userGroups.get(i);

            TextView txvUserName = findViewById(R.id.txvUserName);
            txvUserName.setText("" + ug.getUserName());

            TextView txvUserPhone = findViewById(R.id.txvUserPhone);
            txvUserPhone.setText("" + ug.getUserPhone());

            String messageNotification = "O usuário " + ug.getUserName() + " te convidou para fazer parte do grupo " + ug.getGroupName();
            TextView txvMessageNotification= findViewById(R.id.txvMessageNotification);
            txvMessageNotification.setText(messageNotification);
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