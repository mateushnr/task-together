package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Interface.AddMemberListener;
import ListAdapter.UserToInviteArrayAdapter;
import Model.User;

public class SearchUserActivity extends AppCompatActivity implements AddMemberListener {
    ArrayList<Integer> userIds;
    ListView ltvUserList;
    List<User> users;
    List<User> filteredUsers;

    private String lastCharEdtPhone = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        users = new ArrayList<User>();
        filteredUsers = new ArrayList<User>();

        users.add(new User(1, "Mateus", null,null, "18 99999-9999"));
        users.add(new User(2, "Jorge",null,null, "18 98888-8888"));
        users.add(new User(3, "Lucas",null,null, "18 97777-7777"));
        users.add(new User(4, "Leticia",null,null, "18 97777-6666"));
        users.add(new User(5, "Fernando",null,null, "18 95555-5555"));
        users.add(new User(6, "Pedro",null,null, "18 94444-4444"));
        users.add(new User(7, "Amanda",null,null, "18 94444-3333"));
        users.add(new User(8, "Helena",null,null, "18 95555-2222"));
        users.add(new User(9, "Maria",null,null, "18 99999-1111"));

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

                        if(user.getPhone() != null && userNotAdded && user.getPhone().contains(s.toString())){
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