package com.example.tasktogether;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Interface.RemoveMemberListener;
import ListAdapter.MemberArrayAdapter;
import Model.UserGroup;
import validation.FormValidation;

public class CreateGroupActivity extends AppCompatActivity implements RemoveMemberListener {

    List<UserGroup> membersGroup = new ArrayList<UserGroup>();
    ListView ltvMemberGroupList;


    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            System.out.println("---" + "testOn result");

            if(result.getResultCode() == 78){
                Intent intentresult = result.getData();
                ArrayList<Integer> updatedUserIds = intentresult.getIntegerArrayListExtra("userIds");
                System.out.println("---" + updatedUserIds);
            }

            // Update your membersGroup list with the updated userIds (optional)
            // ...
        }

    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        membersGroup.add(new UserGroup(1, 1, false, "pendente","Atividades Domésticas", "Mateus", "18 99999-9999"));
        membersGroup.add(new UserGroup(2, 2,false,"pendente","Lazer", "Jorge", "18 98888-8888"));
        membersGroup.add(new UserGroup(3, 3,false,"pendente","Compras do mês", "Lucas", "18 97777-7777"));

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
            Intent intentAddMember = new Intent(getBaseContext(), SearchUserActivity.class);
            ArrayList<Integer> userIds = new ArrayList<>();

            for (UserGroup userGroup : membersGroup) {
                userIds.add(userGroup.getIdUser());
            }
            intentAddMember.putIntegerArrayListExtra("userIds", userIds);
            activityLauncher.launch(intentAddMember);
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

    public void removeMember(int idUser){
        for (int i = membersGroup.size() - 1; i >= 0; i--) {
            UserGroup userGroup = membersGroup.get(i);
            if (userGroup.getIdUser() == idUser) {
                membersGroup.remove(i);
                break;
            }
        }
    }

    @Override
    public void onRemoveMember(int idUser) {
        removeMember(idUser);
        ((MemberArrayAdapter) ltvMemberGroupList.getAdapter()).notifyDataSetChanged();
    }
}