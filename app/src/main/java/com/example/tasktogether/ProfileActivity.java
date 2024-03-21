package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import Model.UserGroup;

public class ProfileActivity extends AppCompatActivity {

    private boolean loggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(loggedIn){
            setContentView(R.layout.activity_profile);

        }else{
            setContentView(R.layout.activity_profile_logged_out);

            TextView txvLinkLogin = findViewById(R.id.txvLinkLogin);
            Button btnLinkToSignUp = findViewById(R.id.btnLinkToSignUp);

            Intent intentLogin = new Intent(this, MainActivity.class);
            txvLinkLogin.setOnClickListener(v -> startActivity(intentLogin));

            Intent intentSignUp= new Intent(this, SignUpActivity.class);
            btnLinkToSignUp.setOnClickListener(v -> startActivity(intentSignUp));
        }

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

        Button btnNavGroup = findViewById(R.id.btnNavGroups);
        Button btnNavNotification = findViewById(R.id.btnNavNotification);
        Button btnNavProfile = findViewById(R.id.btnNavProfile);

        TextView txvProfileUnderline = findViewById(R.id.txvProfileUnderline);

        txvProfileUnderline.setVisibility(View.VISIBLE);
        btnNavProfile.setTextColor(getResources().getColor(R.color.primary4));

        btnNavProfile.setClickable(false);

        Intent intentGroups= new Intent(this, MyGroupsActivity.class);
        btnNavGroup.setOnClickListener(v -> startActivity(intentGroups));

        Intent intentNotification = new Intent(this, MyNotificationActivity.class);
        btnNavNotification.setOnClickListener(v -> startActivity(intentNotification));

    }
}