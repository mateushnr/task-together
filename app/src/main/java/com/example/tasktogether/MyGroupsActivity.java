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

import ListAdapter.GroupArrayAdapter;
import Model.Group;
import Model.UserGroup;

public class MyGroupsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        TextView txvAmountNotification = findViewById(R.id.txvAmountNotification);
        int amountNotification = 0;

        UserGroup[] userGroups = {
                new UserGroup(1, 1,false, "pendente","Atividades Domésticas", "Mateus", "18 99999-9999"),
                new UserGroup(1, 2,false,"pendente","Lazer", "Jorge", "18 98888-8888"),
                new UserGroup(1, 3,false,"pendente","Compras do mês", "Lucas", "18 97777-7777"),
        };

        amountNotification = userGroups.length;
        if(amountNotification > 0){
            txvAmountNotification.setText("" + amountNotification);
            txvAmountNotification.setVisibility(View.VISIBLE);
        }

        ListView ltvGroupList = findViewById(R.id.ltvGroupList);
        ltvGroupList.setEmptyView(findViewById(R.id.emptyGroupList));

        Group[] groups = {
                new Group(1, "Atividades Domésticas", "Grupo para organizar as atividades domésticas da familia"),
                new Group(2, "Lazer", "Grupo para organizar os horários de lazer da familia"),
                new Group(3, "Compras do mês", "Grupo para organizar as compras do mês da familia"),
        };

        GroupArrayAdapter adapter = new GroupArrayAdapter(this, Arrays.asList(groups));

        ltvGroupList.setAdapter(adapter);

        ltvGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentGroupItem = new Intent(getBaseContext(), GroupActivity.class);
                intentGroupItem.putExtra("position", position);
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