package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Model.Group;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        List<Group> groups = new ArrayList<Group>();

        groups.add(new Group(1, "Atividades Domésticas", "Grupo para organizar as atividades domésticas da familia"));
        groups.add(new Group(2, "Estudos", "Grupo para organizar os horários de estudo dos participantes"));
        groups.add(new Group(3, "Compras do mês", "Grupo para organizar as compras do mês da familia"));

        Intent intentGroup = getIntent();

        if(intentGroup.hasExtra("position")){
            int i = intentGroup.getIntExtra("position", 0);
            Group g = groups.get(i);
            TextView txvName = findViewById(R.id.txvGroupName);
            txvName.setText("" + g.getName());

            TextView txvDescription = findViewById(R.id.txvGroupDescription);
            txvDescription.setText("" + g.getDescription());
        }

    }
}