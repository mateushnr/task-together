package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import ListAdapter.GroupArrayAdapter;
import ListAdapter.MemberArrayAdapter;
import ListAdapter.TaskArrayAdapter;
import Model.Group;
import Model.Task;
import Model.User;
import Model.UserGroup;
import Model.dao.GroupDAO;
import Model.dao.TaskDAO;
import Model.dao.UserDAO;
import Model.dao.UserGroupDAO;

public class GroupActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPref";
    private static final String KEY_EMAIL = "email";

    Button navAllTasks;
    Button navPendingTasks;
    Button navCompletedTasks;
    Button navLateTasks;

    TextView txvAllTasksUnderline;
    TextView txvPendingTasksUnderline;
    TextView txvCompletedTasksUnderline;
    TextView txvLateTasksUnderline;

    int idSelectedGroup;
    List<Task> filteredTasks = new ArrayList<>();
    ListView ltvTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String loggedUserEmail = sharedPreferences.getString(KEY_EMAIL, null);

        UserGroupDAO userGroupDao = new UserGroupDAO(this);
        UserDAO userDao = new UserDAO(this);
        GroupDAO groupDao = new GroupDAO(this);
        TaskDAO taskDao = new TaskDAO(this);

        filteredTasks = null;

        Button btnLeaveGroupForMembers = findViewById(R.id.btnLeaveGroupForMembers);
        btnLeaveGroupForMembers.setVisibility(View.GONE);

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

        Button btnCreateTask = findViewById(R.id.btnCreateTask);

        ImageButton imgButtonEdtGroup = findViewById((R.id.imgButtonEdtGroup));

        Intent intentGroup = getIntent();

        if(intentGroup.hasExtra("selectedGroup")){
            idSelectedGroup = intentGroup.getIntExtra("selectedGroup", 0);
            Group selectedGroup = groupDao.searchById(idSelectedGroup);
            TextView txvName = findViewById(R.id.txvGroupName);
            txvName.setText("" + selectedGroup.getName());

            checkAndUpdateTasksStatus(idSelectedGroup);

            btnCreateTask.setOnClickListener(v -> {
                Intent intentCreateTask = new Intent(getBaseContext(), CreateTaskActivity.class);
                intentCreateTask.putExtra("selectedGroup", selectedGroup.getIdGroup());

                startActivity(intentCreateTask);
            });

            User currentUser = null;
            UserGroup currentUserGroup;

            if(loggedUserEmail != null){
                currentUser = userDao.searchByEmail(loggedUserEmail);
                currentUserGroup = userGroupDao.searchByUserIdAndGroupId(currentUser.getIdUser(), selectedGroup.getIdGroup());

                if(!Objects.equals(currentUserGroup.getAccessLevel(), "Admin")){
                    imgButtonEdtGroup.setVisibility(View.GONE);
                    btnLeaveGroupForMembers.setVisibility(View.VISIBLE);

                    btnLeaveGroupForMembers.setOnClickListener(v -> {
                        userGroupDao.delete(currentUserGroup.getIdUserGroup());

                        Intent intentGroups= new Intent(this, MyGroupsActivity.class);
                        startActivity(intentGroups);
                    });
                }

            } else {
                currentUserGroup = null;
            }

            imgButtonEdtGroup.setOnClickListener(v -> {
                Intent intentEditGroup = new Intent(getBaseContext(), EditGroupActivity.class);
                intentEditGroup.putExtra("idGroupToEdit", selectedGroup.getIdGroup());

                startActivity(intentEditGroup);
            });

            filteredTasks = taskDao.searchAllByGroup(idSelectedGroup);

            ltvTaskList = findViewById(R.id.ltvTaskList);
            ltvTaskList.setEmptyView(findViewById(R.id.emptyTaskList));

            TaskArrayAdapter adapter = new TaskArrayAdapter(this, filteredTasks);
            ltvTaskList.setAdapter(adapter);

            ltvTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intentTaskItem = new Intent(getBaseContext(), TaskActivity.class);
                    Task selectedTask = filteredTasks.get(position);
                    intentTaskItem.putExtra("selectedTask", selectedTask.getIdTask());
                    startActivity(intentTaskItem);
                }
            });
        }

        navAllTasks = findViewById(R.id.navAllTasks);
        navPendingTasks = findViewById(R.id.navPendingTasks);
        navCompletedTasks = findViewById(R.id.navCompletedTasks);
        navLateTasks = findViewById(R.id.navLateTasks);

        txvAllTasksUnderline = findViewById(R.id.txvAllTasksUnderline);
        txvPendingTasksUnderline = findViewById(R.id.txvPendingTasksUnderline);
        txvCompletedTasksUnderline = findViewById(R.id.txvCompletedTasksUnderline);
        txvLateTasksUnderline = findViewById(R.id.txvLateTasksUnderline);

        navAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTaskNavClick(v, idSelectedGroup);
            }
        });
        navPendingTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTaskNavClick(v, idSelectedGroup);
            }
        });
        navCompletedTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTaskNavClick(v, idSelectedGroup);
            }
        });
        navLateTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTaskNavClick(v, idSelectedGroup);
            }
        });

        Button btnNavGroup = findViewById(R.id.btnNavGroups);
        Button btnNavNotification = findViewById(R.id.btnNavNotification);
        Button btnNavProfile = findViewById(R.id.btnNavProfile);

        ImageButton imgButtonGoBack = findViewById(R.id.imgButtonGoBack);
        imgButtonGoBack.setOnClickListener(v -> finish());

        TextView txvGroupUnderline = findViewById(R.id.txvGroupsUnderline);

        txvGroupUnderline.setVisibility(View.VISIBLE);
        btnNavGroup.setTextColor(getResources().getColor(R.color.primary4));

        btnNavGroup.setClickable(false);

        Intent intentNotification = new Intent(this, MyNotificationActivity.class);
        btnNavNotification.setOnClickListener(v -> startActivity(intentNotification));

        Intent intentProfile = new Intent(this, ProfileActivity.class);
        btnNavProfile.setOnClickListener(v -> startActivity(intentProfile));
    }

    public void checkAndUpdateTasksStatus(int selectedGroupId){
        TaskDAO taskDAO = new TaskDAO(this);

        ArrayList<Task> groupTasks = taskDAO.searchAllByGroup(selectedGroupId);

        for(Task task: groupTasks){
            if(Objects.equals(task.getStatus(), "Pendente")){
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date limitDate = sdf.parse(task.getLimitDate().toString());
                    if (limitDate.before(currentDate)) {
                        task.setStatus("Atrasada");

                        taskDAO.edit(task);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void handleTaskNavClick(View view, int selectedGroupId) {
        int id = view.getId();

        txvAllTasksUnderline.setVisibility(View.INVISIBLE);
        txvPendingTasksUnderline.setVisibility(View.INVISIBLE);
        txvCompletedTasksUnderline.setVisibility(View.INVISIBLE);
        txvLateTasksUnderline.setVisibility(View.INVISIBLE);

        navAllTasks.setTextColor(getResources().getColor(R.color.gray7));
        navPendingTasks.setTextColor(getResources().getColor(R.color.gray7));
        navCompletedTasks.setTextColor(getResources().getColor(R.color.gray7));
        navLateTasks.setTextColor(getResources().getColor(R.color.gray7));

        String clickedButtonName = getResources().getResourceEntryName(id);

        TaskDAO taskDAO = new TaskDAO(this);

        switch (clickedButtonName) {
            case "navAllTasks":
                txvAllTasksUnderline.setVisibility(View.VISIBLE);
                navAllTasks.setTextColor(getResources().getColor(R.color.primary7));

                filteredTasks = taskDAO.searchAllByGroup(selectedGroupId);
                break;
            case "navPendingTasks":
                txvPendingTasksUnderline.setVisibility(View.VISIBLE);
                navPendingTasks.setTextColor(getResources().getColor(R.color.primary7));

                filteredTasks = taskDAO.searchByGroupAndStatus(selectedGroupId, "Pendente");
                break;
            case "navCompletedTasks":
                txvCompletedTasksUnderline.setVisibility(View.VISIBLE);
                navCompletedTasks.setTextColor(getResources().getColor(R.color.primary7));

                filteredTasks = taskDAO.searchByGroupAndStatus(selectedGroupId, "Concluida");
                break;
            case "navLateTasks":
                txvLateTasksUnderline.setVisibility(View.VISIBLE);
                navLateTasks.setTextColor(getResources().getColor(R.color.primary7));

                filteredTasks = taskDAO.searchByGroupAndStatus(selectedGroupId, "Atrasada");
                break;
        }

        TaskArrayAdapter adapter = (TaskArrayAdapter) ltvTaskList.getAdapter();
        adapter.updateTaskList(filteredTasks);
        adapter.notifyDataSetChanged();
    }
}