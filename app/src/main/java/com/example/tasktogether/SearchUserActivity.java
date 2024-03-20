package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Model.UserGroup;

public class SearchUserActivity extends AppCompatActivity {

    ArrayList<Integer> userIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        Intent intent = getIntent();
        userIds = intent.getIntegerArrayListExtra("userIds");
        userIds.add(99);

        Intent intentUpdated = new Intent();

        intentUpdated.putIntegerArrayListExtra("userIds", userIds);

        setResult(78, intentUpdated);

        // Update the Intent with the modified userIds
         // Set result code to indicate successful update

        // Finish the Activity

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intentUpdated = new Intent();
        intentUpdated.putIntegerArrayListExtra("userIds", userIds);
        setResult(78, intentUpdated);
    }
}