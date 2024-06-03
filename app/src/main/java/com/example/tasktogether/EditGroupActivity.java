package com.example.tasktogether;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import ListAdapter.MemberEditArrayAdapter;
import Model.Group;
import Model.User;
import Model.UserGroup;
import Model.dao.GroupDAO;
import Model.dao.UserDAO;
import Model.dao.UserGroupDAO;
import validation.FormValidation;

public class EditGroupActivity extends AppCompatActivity implements RemoveMemberListener {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPref";
    private static final String KEY_EMAIL = "email";

    ArrayList<Integer> membersUsersId;
    List<UserGroup> currentUserGroupMembers;
    List<User> membersGroup;
    List<User> users;
    ListView ltvMemberEditGroupList;
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
        setContentView(R.layout.activity_edit_group);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String loggedUserEmail = sharedPreferences.getString(KEY_EMAIL, null);

        if(loggedUserEmail != null){
            loggedIn = true;
        }

        dialog = new Dialog(EditGroupActivity.this);
        dialog.setContentView(R.layout.dialog_logged_out_warning);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        btnDialogCancel = dialog.findViewById(R.id.btnCloseDialog);

        btnDialogCancel.setOnClickListener(v -> dialog.dismiss());

        UserDAO userDao = new UserDAO(this);
        GroupDAO groupDao = new GroupDAO(this);
        UserGroupDAO userGroupDao = new UserGroupDAO(this);

        users = userDao.searchAll();

        Group groupToEdit;

        Intent intentEditGroup = getIntent();

        if(intentEditGroup.hasExtra("idGroupToEdit")){
            int idGroupToEdit = intentEditGroup.getIntExtra("idGroupToEdit", 0);

            groupToEdit = groupDao.searchById(idGroupToEdit);
        } else {
            groupToEdit = null;
        }

        membersGroup = new ArrayList<User>();
        membersUsersId = new ArrayList<Integer>();

        Button btnLeaveGroup = findViewById(R.id.btnLeaveGroup);
        Button btnDeleteGroup = findViewById(R.id.btnDeleteGroup);

        currentUserGroupMembers = userGroupDao.searchAllByGroup(groupToEdit.getIdGroup());

        if(loggedIn){
            User currentUser = userDao.searchByEmail(loggedUserEmail);

            if(currentUser != null){
                for(UserGroup groupMember: currentUserGroupMembers){
                    if(currentUser.getIdUser() != groupMember.getIdUser()){
                        User user = userDao.searchById(groupMember.getIdUser());
                        user.setStatusParticipation(groupMember.getStatusParticipation());
                        membersGroup.add(user);

                        membersUsersId.add(groupMember.getIdUser());
                    }
                }

                btnLeaveGroup.setOnClickListener(v -> {
                    for(UserGroup groupMember: currentUserGroupMembers){
                        if(currentUser.getIdUser() == groupMember.getIdUser()){
                            userGroupDao.delete(groupMember.getIdUserGroup());
                        }
                    }

                    Intent intentGroups= new Intent(this, MyGroupsActivity.class);
                    startActivity(intentGroups);
                });
            }
        }else{
            btnLeaveGroup.setVisibility(View.GONE);
        }

        btnDeleteGroup.setOnClickListener(v -> {
            for(UserGroup groupMember: currentUserGroupMembers){
                userGroupDao.delete(groupMember.getIdUserGroup());
            }

            groupDao.delete(groupToEdit.getIdGroup());

            Intent intentGroups= new Intent(this, MyGroupsActivity.class);
            startActivity(intentGroups);
        });


        ltvMemberEditGroupList = findViewById(R.id.ltvMemberEditGroupList);
        ltvMemberEditGroupList.setEmptyView(findViewById(R.id.emptyMemberEditGroupList));

        MemberEditArrayAdapter adapter = new MemberEditArrayAdapter(this, membersGroup, this);

        ltvMemberEditGroupList.setAdapter(adapter);

        ImageButton imgButtonGoBack = findViewById(R.id.imgButtonGoBack);
        imgButtonGoBack.setOnClickListener(v -> finish());

        EditText edtGroupName = findViewById(R.id.edtGroupName);
        EditText edtGroupDescription = findViewById(R.id.edtGroupDescription);

        TextView txvErrorGroupName = findViewById(R.id.txvErrorGroupName);
        TextView txvErrorGroupDescription = findViewById(R.id.txvErrorGroupDescription);

        TextView txvGroupName = findViewById(R.id.txvGroupName);

        txvGroupName.setText(groupToEdit.getName());
        edtGroupName.setText(groupToEdit.getName());
        edtGroupDescription.setText(groupToEdit.getDescription());

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

        Button btnEditGroupConfirm = findViewById(R.id.btnEditGroupConfirm);

        btnEditGroupConfirm.setOnClickListener(v -> {
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
                editGroup(edtGroupName.getText().toString(), edtGroupDescription.getText().toString(), groupToEdit, loggedUserEmail);
            }
        });
    }

    public void editGroup(String group_name, String group_description, Group groupToEdit, String currentUserEmail){
        UserDAO userDao = new UserDAO(this);
        GroupDAO groupDao = new GroupDAO(this);
        UserGroupDAO userGroupDao = new UserGroupDAO(this);

        groupToEdit.setName(group_name);
        groupToEdit.setDescription(group_description);

        groupDao.edit(groupToEdit);

        if(currentUserEmail != null){
            int currentUserId = userDao.searchByEmail(currentUserEmail).getIdUser();

            for(UserGroup currentUserGroupMember: currentUserGroupMembers){
                boolean wasRemoved = true;

                for(User user: membersGroup){
                    if(currentUserGroupMember.getIdUser() == user.getIdUser()
                            || currentUserGroupMember.getIdUser() == currentUserId){
                        wasRemoved = false;
                    }
                }

                if(wasRemoved){
                    userGroupDao.delete(currentUserGroupMember.getIdUserGroup());
                }
            }

            if(!membersGroup.isEmpty()){
                for (User member : membersGroup) {
                    boolean alreadyIsMember = false;

                    for(UserGroup currentUserGroupMember: currentUserGroupMembers){
                        if(member.getIdUser() == currentUserGroupMember.getIdUser()){
                            alreadyIsMember = true;
                        }
                    }

                    if(!alreadyIsMember){
                        UserGroup userGroup = new UserGroup();

                        userGroup.setIdGroup(groupToEdit.getIdGroup());
                        userGroup.setIdUser(member.getIdUser());
                        userGroup.setIdWhoInvited(userDao.searchByEmail(currentUserEmail).getIdUser());
                        userGroup.setAccessLevel("Membro");
                        userGroup.setStatusParticipation("Pendente");

                        userGroupDao.insert(userGroup);
                    }
                }
            }
        }

        Intent groupEditedIntent = new Intent(this, GroupActivity.class);
        groupEditedIntent.putExtra("selectedGroup", groupToEdit.getIdGroup());

        startActivity(groupEditedIntent);

        Toast.makeText(this, "Grupo editado!",
                Toast.LENGTH_SHORT).show();
    }

    private void updateMembers() {
        membersGroup.clear();

        if (membersUsersId != null && membersUsersId.size() > 0) {
            for (Integer userId : membersUsersId) {
                for (User user : users) {
                    if (user.getIdUser() == userId) {
                        user.setStatusParticipation("Convidar");

                        for(UserGroup currentUserInGroup: currentUserGroupMembers){
                            if(user.getIdUser() == currentUserInGroup.getIdUser()){
                                user.setStatusParticipation(currentUserInGroup.getStatusParticipation());
                            }
                        }
                        membersGroup.add(user);
                        break;
                    }
                }
            }
        }

        ((MemberEditArrayAdapter) ltvMemberEditGroupList.getAdapter()).notifyDataSetChanged();
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

        ((MemberEditArrayAdapter) ltvMemberEditGroupList.getAdapter()).notifyDataSetChanged();
    }
}