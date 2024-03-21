package com.example.tasktogether;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Interface.RemoveMemberListener;
import ListAdapter.MemberArrayAdapter;
import Model.User;
import validation.FormValidation;

public class CreateGroupActivity extends AppCompatActivity implements RemoveMemberListener {

    ArrayList<Integer> membersUsersId;
    List<User> membersGroup;
    List<User> users;
    ListView ltvMemberGroupList;
    private boolean loggedIn = true;
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

        dialog = new Dialog(CreateGroupActivity.this);
        dialog.setContentView(R.layout.dialog_logged_out_warning);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        btnDialogCancel = dialog.findViewById(R.id.btnCloseDialog);

        btnDialogCancel.setOnClickListener(v -> dialog.dismiss());

        users = new ArrayList<User>();
        membersGroup = new ArrayList<User>();
        membersUsersId = new ArrayList<Integer>();

        users.add(new User(1, "Mateus", null,null, "18 99999-9999"));
        users.add(new User(2, "Jorge",null,null, "18 98888-8888"));
        users.add(new User(3, "Lucas",null,null, "18 97777-7777"));
        users.add(new User(4, "Leticia",null,null, "18 97777-6666"));
        users.add(new User(5, "Fernando",null,null, "18 95555-5555"));
        users.add(new User(6, "Pedro",null,null, "18 94444-4444"));
        users.add(new User(7, "Amanda",null,null, "18 94444-3333"));
        users.add(new User(8, "Helena",null,null, "18 95555-2222"));
        users.add(new User(9, "Maria",null,null, "18 99999-1111"));


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
                System.out.println("valido");
            }else{
                System.out.println("invalido");
            }
        });

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