package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;

import ListAdapter.NotificationArrayAdapter;
import Model.UserGroup;

public class MyNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification);

        ListView ltvNotificationList = findViewById(R.id.ltvNotificationList);
        ltvNotificationList.setEmptyView(findViewById(R.id.emptyNotificationList));

        UserGroup[] userGroups = {
                new UserGroup(1, 2,false, "pendente","Atividades Domésticas", "Mateus", "18 99999-9999"),
                new UserGroup(1, 3,false,"pendente","Lazer", "Jorge", "18 98888-8888"),
                new UserGroup(1, 4,false,"pendente","Compras do mês", "Lucas", "18 97777-7777"),
        };

        NotificationArrayAdapter adapter = new NotificationArrayAdapter(this, Arrays.asList(userGroups));

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

    }
}