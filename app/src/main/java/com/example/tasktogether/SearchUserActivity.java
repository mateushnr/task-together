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

public class SearchUserActivity extends AppCompatActivity implements AddMemberListener {
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPref";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";

    ArrayList<Integer> userIds;
    ListView ltvUserList;
    List<User> users;
    List<User> filteredUsers;

    private String lastCharEdtPhone = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        UserDAO userDao = new UserDAO(this);

        users = userDao.searchAll();
        filteredUsers = new ArrayList<User>();

        ltvUserList = findViewById(R.id.ltvUserList);
        UserToInviteArrayAdapter adapter = new UserToInviteArrayAdapter(this, filteredUsers, this);
        ltvUserList.setAdapter(adapter);

        Intent intent = getIntent();
        userIds = intent.getIntegerArrayListExtra("userIds");

        EditText edtSearchUse = findViewById(R.id.edtSearchUser);

        edtSearchUse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int phoneLength = s.toString().length();

                if (phoneLength > 1){
                    lastCharEdtPhone = edtSearchUse.getText().toString().substring(phoneLength - 1);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int phoneLength = s.toString().length();

                if(phoneLength > 0){
                    if(!Character.isDigit(s.charAt(phoneLength - 1)) && s.charAt(phoneLength - 1) != ' ' && s.charAt(phoneLength - 1) != '-'){
                        edtSearchUse.getText().delete(phoneLength - 1, phoneLength);
                    }
                }

                if(phoneLength == 2){
                    if(!lastCharEdtPhone.equals(" ")){
                        edtSearchUse.append(" ");
                    }else{
                        edtSearchUse.getText().delete(phoneLength - 1, phoneLength);
                    }
                } else if(phoneLength == 8){
                    if(!lastCharEdtPhone.equals("-")){
                        edtSearchUse.append("-");
                    }else{
                        edtSearchUse.getText().delete(phoneLength - 1, phoneLength);
                    }
                }

                if(phoneLength > 4){
                    filteredUsers.clear();
                    boolean userNotAdded;

                    for(User user : users){
                        userNotAdded = true;

                        for(Integer userId : userIds){
                            if(user.getIdUser() == userId){
                                userNotAdded = false;
                                break;
                            }
                        }

                        if(user.getPhone() != null && userNotAdded && user.getPhone().contains(s.toString()) && !Objects.equals(sharedPreferences.getString(KEY_PHONE, null), user.getPhone())){
                            filteredUsers.add(user);
                        }
                    }

                    ((UserToInviteArrayAdapter) ltvUserList.getAdapter()).notifyDataSetChanged();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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

        ((UserToInviteArrayAdapter) ltvUserList.getAdapter()).notifyDataSetChanged();
    }

}