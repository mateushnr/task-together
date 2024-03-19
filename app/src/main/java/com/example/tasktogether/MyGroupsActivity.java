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

public class MyGroupsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);


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

        Button btnNavGroup = findViewById(R.id.btnNavGroups);
        Button btnNavNotification = findViewById(R.id.btnNavNotification);
        Button btnNavProfile = findViewById(R.id.btnNavProfile);

        TextView txvGroupUnderline = findViewById(R.id.txvGroupsUnderline);

        txvGroupUnderline.setVisibility(View.VISIBLE);
        btnNavGroup.setTextColor(getResources().getColor(R.color.primary4));

        Intent intentNotification = new Intent(this, MyNotificationActivity.class);
        btnNavNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentNotification);
            }
        });
    }
}