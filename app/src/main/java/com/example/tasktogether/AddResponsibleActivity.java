package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Interface.AddMemberListener;
import ListAdapter.UserToInviteArrayAdapter;
import Model.User;
import Model.dao.UserDAO;
import Model.dao.UserGroupDAO;

public class AddResponsibleActivity extends AppCompatActivity implements AddMemberListener {
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPref";
    private static final String KEY_EMAIL = "email";

    ArrayList<Integer> userIds;
    ArrayList<Integer> userIdMembers;
    ListView ltvResponsiblesToAddList;
    List<User> users;
    List<User> filteredUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_responsible);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String loggedUserEmail = sharedPreferences.getString(KEY_EMAIL, null);

        Intent intent = getIntent();
        userIds = intent.getIntegerArrayListExtra("userIds");
        userIdMembers = intent.getIntegerArrayListExtra("userIdMembers");

        UserDAO userDao = new UserDAO(this);
        UserGroupDAO userGroupDao = new UserGroupDAO(this);

        User loggedUser = userDao.searchByEmail(loggedUserEmail);

        users = userDao.searchAll();

        filteredUsers = new ArrayList<User>();


        for(int userIdMember: userIdMembers ){
            boolean alreadyResponsible = false;

            for(int userId: userIds){
                if (userIdMember == userId) {
                    alreadyResponsible = true;
                    break;
                }
            }

            if(!alreadyResponsible){
                User user = userDao.searchById(userIdMember);

                if(user != null){
                    if(userIdMember == loggedUser.getIdUser()){
                        user.setName("Você");
                    }
                    filteredUsers.add(user);
                }
            }
        }

        ltvResponsiblesToAddList = findViewById(R.id.ltvResponsiblesToAddList);
        ltvResponsiblesToAddList.setEmptyView(findViewById(R.id.emptyResponsiblesToAddList));

        UserToInviteArrayAdapter adapter = new UserToInviteArrayAdapter(this, filteredUsers, this);
        ltvResponsiblesToAddList.setAdapter(adapter);


        ImageButton imgButtonGoBack = findViewById(R.id.imgButtonGoBack);
        imgButtonGoBack.setOnClickListener(v -> finish());
    }

    @Override
    public void addMember(int idUserToAdd) {
        boolean userAlreadyAdded = false;

        for(Integer userId: userIds){
            if(userId == idUserToAdd){
                userAlreadyAdded = true;
                break;
            }
        }

        if(!userAlreadyAdded){
            userIds.add(idUserToAdd);

            Intent intentUpdated = new Intent();
            intentUpdated.putIntegerArrayListExtra("userIds", userIds);
            setResult(1, intentUpdated);
        }


        for(int i = 0; i < filteredUsers.size(); i++){
            int currentUserId = filteredUsers.get(i).getIdUser();
            if(currentUserId == idUserToAdd){
                filteredUsers.remove(i);
                break;
            }
        }

        ((UserToInviteArrayAdapter) ltvResponsiblesToAddList.getAdapter()).notifyDataSetChanged();
    }

}