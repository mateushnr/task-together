package com.example.tasktogether;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Interface.RemoveMemberListener;
import ListAdapter.MemberArrayAdapter;
import Model.Group;
import Model.User;
import Model.UserGroup;
import Model.dao.GroupDAO;
import Model.dao.UserDAO;
import Model.dao.UserGroupDAO;
import validation.FormValidation;

public class CreateGroupActivity extends AppCompatActivity implements RemoveMemberListener {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPref";
    private static final String KEY_EMAIL = "email";

    ArrayList<Integer> membersUsersId;
    List<User> membersGroup;
    List<User> users;
    ListView ltvMemberGroupList;
    private boolean loggedIn = false;
    Dialog dialog;
    ImageButton btnDialogCancel;

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if(result.getResultCode() == 1){
                Intent intentResult = result.getData();
                membersUsersId = intentResult.getIntegerArrayListExtra("userIds");
                updateMembers();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String loggedUserEmail = sharedPreferences.getString(KEY_EMAIL, null);

        if(loggedUserEmail != null){
            loggedIn = true;
        }

        dialog = new Dialog(CreateGroupActivity.this);
        dialog.setContentView(R.layout.dialog_logged_out_warning);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        btnDialogCancel = dialog.findViewById(R.id.btnCloseDialog);

        btnDialogCancel.setOnClickListener(v -> dialog.dismiss());

        UserDAO userDao = new UserDAO(this);

        users = userDao.searchAll();

        membersGroup = new ArrayList<User>();
        membersUsersId = new ArrayList<Integer>();

        ltvMemberGroupList = findViewById(R.id.ltvMemberGroupList);
        ltvMemberGroupList.setEmptyView(findViewById(R.id.emptyMemberGroupList));

        MemberArrayAdapter adapter = new MemberArrayAdapter(this, membersGroup, this);

        ltvMemberGroupList.setAdapter(adapter);

        ImageButton imgButtonGoBack = findViewById(R.id.imgButtonGoBack);
        imgButtonGoBack.setOnClickListener(v -> finish());

        EditText edtGroupName = findViewById(R.id.edtGroupName);
        EditText edtGroupDescription = findViewById(R.id.edtGroupDescription);
        TextView txvErrorGroupName = findViewById(R.id.txvErrorGroupName);
        TextView txvErrorGroupDescription = findViewById(R.id.txvErrorGroupDescription);

        Button btnAddMember = findViewById(R.id.btnAddMember);

        btnAddMember.setOnClickListener(v -> {
            if(loggedIn){
                Intent intentAddMember = new Intent(getBaseContext(), SearchUserActivity.class);

                intentAddMember.putIntegerArrayListExtra("userIds", membersUsersId);
                activityLauncher.launch(intentAddMember);
            }else{
                dialog.show();
            }
        });

        Button btnCreateGroupConfirm = findViewById(R.id.btnCreateGroupConfirm);

        btnCreateGroupConfirm.setOnClickListener(v -> {
            boolean isValid = true;
            String errorMessage;
            FormValidation validator = new FormValidation();

            if(validator.isEmpty(edtGroupName.getText().toString())){
                isValid = false;
                edtGroupName.requestFocus();

                errorMessage = "Nome do grupo vazio";

                txvErrorGroupName.setText(errorMessage);
                txvErrorGroupName.setVisibility(View.VISIBLE);
            }else{
                txvErrorGroupName.setVisibility(View.INVISIBLE);
            }

            if(validator.isEmpty(edtGroupDescription.getText().toString())){
                isValid = false;
                edtGroupDescription.requestFocus();
                errorMessage = "Descrição do grupo vazia";
                txvErrorGroupDescription.setText(errorMessage);
                txvErrorGroupDescription.setVisibility(View.VISIBLE);
            } else{
                txvErrorGroupDescription.setVisibility(View.INVISIBLE);
            }

            if(isValid){
                registerGroup(edtGroupName.getText().toString(), edtGroupDescription.getText().toString(), loggedUserEmail);
            }
        });

    }

    public void registerGroup(String group_name, String group_description, String ownerEmail){
        UserDAO userDao = new UserDAO(this);
        GroupDAO groupDao = new GroupDAO(this);
        UserGroupDAO userGroupDao = new UserGroupDAO(this);

        Group group = new Group();
        group.setName(group_name);
        group.setDescription(group_description);

        if(ownerEmail == null){
            group.setType("offline");

            groupDao.insert(group);
        }else{
            group.setType("compartilhavel");

            Group createdGroup = groupDao.insert(group);

            UserGroup userGroupOwner = new UserGroup();
            userGroupOwner.setIdGroup(createdGroup.getIdGroup());
            userGroupOwner.setIdUser(userDao.searchByEmail(ownerEmail).getIdUser());
            userGroupOwner.setAccessLevel("Admin");
            userGroupOwner.setStatusParticipation("Membro");

            userGroupDao.insert(userGroupOwner);

            if(!membersGroup.isEmpty()){
                for (User member : membersGroup) {
                    UserGroup userGroup = new UserGroup();

                    userGroup.setIdGroup(createdGroup.getIdGroup());
                    userGroup.setIdUser(member.getIdUser());
                    userGroup.setIdWhoInvited(userDao.searchByEmail(ownerEmail).getIdUser());
                    userGroup.setAccessLevel("Membro");
                    userGroup.setStatusParticipation("Pendente");

                    userGroupDao.insert(userGroup);
                }
            }
        }

        Intent groupCreatedIntent = new Intent(this, MyGroupsActivity.class);
        startActivity(groupCreatedIntent);

        Toast.makeText(this, "Grupo criado com sucesso!",
                Toast.LENGTH_LONG).show();
    }

    private void updateMembers() {
        membersGroup.clear();

        if (membersUsersId != null && membersUsersId.size() > 0) {
            for (Integer userId : membersUsersId) {
                for (User user : users) {
                    if (user.getIdUser() == userId) {
                        membersGroup.add(user);
                        break;
                    }
                }
            }
        }

        ((MemberArrayAdapter) ltvMemberGroupList.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void removeMember(int idUserToDelete) {

        for(int i = 0; i < membersUsersId.size(); i++){
            int currentUserId = membersUsersId.get(i);
            if(currentUserId == idUserToDelete){
                membersUsersId.remove(i);
                updateMembers();
                break;
            }
        }

        ((MemberArrayAdapter) ltvMemberGroupList.getAdapter()).notifyDataSetChanged();
    }
}