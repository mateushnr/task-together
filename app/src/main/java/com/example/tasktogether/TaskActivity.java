package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ListAdapter.TaskArrayAdapter;
import ListAdapter.TaskResponsibleArrayAdapter;
import Model.Group;
import Model.Task;
import Model.User;
import Model.UserGroup;
import Model.UserTask;
import Model.dao.GroupDAO;
import Model.dao.TaskDAO;
import Model.dao.UserDAO;
import Model.dao.UserGroupDAO;
import Model.dao.UserTaskDAO;

public class TaskActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPref";
    private static final String KEY_EMAIL = "email";

    ListView ltvTaskResponsiblesList;
    int idSelectedTask;
    List<UserTask> taskResponsibles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String loggedUserEmail = sharedPreferences.getString(KEY_EMAIL, null);

        UserDAO userDao = new UserDAO(this);
        UserGroupDAO userGroupDao = new UserGroupDAO(this);
        TaskDAO taskDao = new TaskDAO(this);
        UserTaskDAO userTaskDao = new UserTaskDAO(this);

        TextView txvAmountNotification = findViewById(R.id.txvAmountNotification);
        int amountNotification = 0;

        List<UserGroup> userGroupsInvitations;

        if(loggedUserEmail != null){
            userGroupsInvitations = userGroupDao.searchAllInvitations(userDao.searchByEmail(loggedUserEmail).getIdUser());

            amountNotification = userGroupsInvitations.size();
            if(amountNotification > 0){
                txvAmountNotification.setText("" + amountNotification);
                txvAmountNotification.setVisibility(View.VISIBLE);
            }
        }

        Intent intentGroup = getIntent();

        if(intentGroup.hasExtra("selectedTask")){
            idSelectedTask = intentGroup.getIntExtra("selectedTask", 0);
            Task selectedTask = taskDao.searchById(idSelectedTask);

            TextView txvTaskName = findViewById(R.id.txvTaskName);
            TextView txvInfoStatus = findViewById(R.id.txvInfoStatus);
            TextView txvInfoLimitDate = findViewById(R.id.txvInfoLimitDate);
            TextView txvTaskDescriptionInfo = findViewById(R.id.txvTaskDescriptionInfo);

            txvTaskName.setText(selectedTask.getName());

            int colorText;
            int colorStatusContainer;

            switch(selectedTask.getStatus().toString()){
                case "Pendente":{
                    colorText = ContextCompat.getColor(this, R.color.dark_yellow);
                    colorStatusContainer = ContextCompat.getColor(this, R.color.light_yellow);
                    break;
                }
                case "Atrasada":{
                    colorText = ContextCompat.getColor(this, R.color.dark_red);
                    colorStatusContainer = ContextCompat.getColor(this, R.color.light_red);
                    break;
                }
                case "Concluida":{
                    colorText = ContextCompat.getColor(this, R.color.dark_green);
                    colorStatusContainer = ContextCompat.getColor(this, R.color.light_green);
                    break;
                }
                default: {
                    colorText = ContextCompat.getColor(this, R.color.primary7);
                    colorStatusContainer = ContextCompat.getColor(this, R.color.primary1);
                }
            }

            txvInfoStatus.setTextColor(colorText);
            txvInfoStatus.setBackgroundTintList(ColorStateList.valueOf(colorStatusContainer));
            txvInfoStatus.setText("Tarefa " + selectedTask.getStatus());

            txvInfoLimitDate.setText(selectedTask.getLimitDate());
            txvTaskDescriptionInfo.setText(selectedTask.getDescription());

            taskResponsibles = userTaskDao.searchAllByTask(idSelectedTask);
            TextView txvEmptyResponsibles = findViewById(R.id.txvEmptyResponsibles);

            if(loggedUserEmail == null){
                txvEmptyResponsibles.setText("Apenas você é responsável pela tarefa");
            }

            ltvTaskResponsiblesList = findViewById(R.id.ltvTaskResponsibleList);
            ltvTaskResponsiblesList.setEmptyView(findViewById(R.id.emptyTaskResponsibleList));

            TaskResponsibleArrayAdapter adapter = new TaskResponsibleArrayAdapter(this, taskResponsibles, loggedUserEmail);
            ltvTaskResponsiblesList.setAdapter(adapter);

            Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
            Button btnDeleteTask = findViewById(R.id.btnDeleteTask);

            Intent intentGoBackToTaskLisk = new Intent(this, GroupActivity.class);
            intentGoBackToTaskLisk.putExtra("selectedGroup", selectedTask.getIdGroup());

            if(Objects.equals(selectedTask.getStatus(), "Concluida")){
                btnCompleteTask.setVisibility(View.GONE);
            }else{
                btnCompleteTask.setOnClickListener(v -> {
                    selectedTask.setStatus("Concluida");

                    taskDao.edit(selectedTask);


                    startActivity(intentGoBackToTaskLisk );
                });
            }

            btnDeleteTask.setOnClickListener(v -> {
                if(loggedUserEmail != null){
                    ArrayList<UserTask> responsiblesToDelete = userTaskDao.searchAllByTask(selectedTask.getIdTask());

                    for(UserTask responsibleToDelete: responsiblesToDelete){
                        userTaskDao.delete(responsibleToDelete.getIdUserTask());
                    }
                }

                taskDao.delete(idSelectedTask);
                startActivity(intentGoBackToTaskLisk );
            });

            ImageButton imgButtonEdtTask = findViewById((R.id.imgButtonEdtTask));

            imgButtonEdtTask.setOnClickListener(v -> {
                Intent intentEditTask = new Intent(getBaseContext(), EditTaskActivity.class);
                intentEditTask.putExtra("idTaskToEdit", selectedTask.getIdTask());

                startActivity(intentEditTask);
            });
        }


        ImageButton imgButtonGoBack = findViewById(R.id.imgButtonGoBack);
        imgButtonGoBack.setOnClickListener(v -> finish());

        Button btnNavGroup = findViewById(R.id.btnNavGroups);
        Button btnNavNotification = findViewById(R.id.btnNavNotification);
        Button btnNavProfile = findViewById(R.id.btnNavProfile);

        TextView txvGroupUnderline = findViewById(R.id.txvGroupsUnderline);

        txvGroupUnderline.setVisibility(View.VISIBLE);
        btnNavGroup.setTextColor(getResources().getColor(R.color.primary4));

        btnNavGroup.setClickable(false);

        Intent intentNotification = new Intent(this, MyNotificationActivity.class);
        btnNavNotification.setOnClickListener(v -> startActivity(intentNotification));

        Intent intentProfile = new Intent(this, ProfileActivity.class);
        btnNavProfile.setOnClickListener(v -> startActivity(intentProfile));
    }
}