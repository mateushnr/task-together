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

import Model.UserGroup;
import Model.dao.UserDAO;
import Model.dao.UserGroupDAO;

public class ProfileActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPref";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private boolean loggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String loggedUserEmail = sharedPreferences.getString(KEY_EMAIL, null);

        if(loggedUserEmail != null){
            loggedIn = true;
        }

        UserGroupDAO userGroupDao = new UserGroupDAO(this);
        UserDAO userDao = new UserDAO(this);

        if(loggedIn){
            setContentView(R.layout.activity_profile);

            Button btnLogout = findViewById(R.id.btnLogout);

            TextView txvUserNameProfile = findViewById(R.id.txvUserNameProfile);
            TextView txvUserEmailProfile = findViewById(R.id.txvUserEmailProfile);
            TextView txvUserPhoneProfile = findViewById(R.id.txvUserPhoneProfile);

            txvUserNameProfile.setText(sharedPreferences.getString(KEY_NAME, null));
            txvUserEmailProfile.setText(sharedPreferences.getString(KEY_EMAIL, null));
            txvUserPhoneProfile.setText(sharedPreferences.getString(KEY_PHONE, null));

            btnLogout.setOnClickListener(v -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                finish();

                Intent intentLogout = new Intent(this, MainActivity.class);
                startActivity(intentLogout);

                Toast.makeText(this, "Deslogado com sucesso",
                        Toast.LENGTH_LONG).show();
            });
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

        List<UserGroup> userGroupsInvitations = new ArrayList<>();

        if(loggedUserEmail != null){
            userGroupsInvitations = userGroupDao.searchAllInvitations(userDao.searchByEmail(loggedUserEmail).getIdUser());

            amountNotification = userGroupsInvitations.size();
            if(amountNotification > 0){
                txvAmountNotification.setText("" + amountNotification);
                txvAmountNotification.setVisibility(View.VISIBLE);
            }
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