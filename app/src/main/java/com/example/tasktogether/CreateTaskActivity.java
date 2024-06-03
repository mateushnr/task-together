package com.example.tasktogether;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import Interface.RemoveMemberListener;
import ListAdapter.MemberArrayAdapter;
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
import validation.FormValidation;

public class CreateTaskActivity extends AppCompatActivity implements RemoveMemberListener {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPref";
    private static final String KEY_EMAIL = "email";

    User loggedUser;
    Group selectedGroup;
    ArrayList<Integer> userIdMembers;
    ArrayList<Integer> responsiblesUsersId;
    List<User> responsiblesForTask;
    List<User> users;
    ListView ltvTaskResponsibleList;
    private boolean loggedIn = false;
    TextView edtTaskLimitDate;
    private String lastCharEdtLimitDate = "";

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if(result.getResultCode() == 1){
                Intent intentResult = result.getData();
                responsiblesUsersId = intentResult.getIntegerArrayListExtra("userIds");
                updateResponsibles();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String loggedUserEmail = sharedPreferences.getString(KEY_EMAIL, null);

        LinearLayout llTaskResponsiblesContainerOnline = findViewById(R.id.llTaskResponsiblesContainerOnline);
        LinearLayout llTaskResponsiblesContainerOffline = findViewById(R.id.llTaskResponsiblesContainerOffline);

        UserDAO userDao = new UserDAO(this);
        GroupDAO groupDao = new GroupDAO(this);
        UserGroupDAO userGroupDao = new UserGroupDAO(this);

        responsiblesForTask = new ArrayList<User>();
        responsiblesUsersId = new ArrayList<Integer>();
        userIdMembers = new ArrayList<Integer>();

        loggedUser = null;

        if(loggedUserEmail != null){
            loggedUser = userDao.searchByEmail(loggedUserEmail);
            loggedIn = true;
            loggedUser.setName("Você");

            responsiblesForTask.add(loggedUser);
            responsiblesUsersId.add(loggedUser.getIdUser());
        }else{
            llTaskResponsiblesContainerOffline.setVisibility(View.VISIBLE);
            llTaskResponsiblesContainerOnline.setVisibility(View.GONE);
        }

        Intent intentGroup = getIntent();

        ArrayList<UserGroup> membersGroupList;

        selectedGroup = null;

        if(intentGroup.hasExtra("selectedGroup")){
            int idSelectedGroup = intentGroup.getIntExtra("selectedGroup", 0);
            selectedGroup = groupDao.searchById(idSelectedGroup);

            membersGroupList = userGroupDao.searchAllByGroupWhereIsMember(idSelectedGroup);

            for(UserGroup memberGroup: membersGroupList){
                userIdMembers.add(memberGroup.getIdUser());
            }
        }

        users = userDao.searchAll();

        ltvTaskResponsibleList = findViewById(R.id.ltvTaskResponsibleList);
        ltvTaskResponsibleList.setEmptyView(findViewById(R.id.emptyTaskResponsibleList));

        MemberArrayAdapter adapter = new MemberArrayAdapter(this, responsiblesForTask, this);

        ltvTaskResponsibleList.setAdapter(adapter);

        ImageButton imgButtonGoBack = findViewById(R.id.imgButtonGoBack);
        imgButtonGoBack.setOnClickListener(v -> finish());

        EditText edtTaskName = findViewById(R.id.edtTaskName);
        EditText edtTaskDescription = findViewById(R.id.edtTaskDescription);
        edtTaskLimitDate = findViewById(R.id.edtTaskLimitDate);
        ImageButton imgBtnCalendarIcon = findViewById(R.id.imgBtnCalendarIcon);
        TextView txvErrorTaskName = findViewById(R.id.txvErrorTaskName);
        TextView txvErrorTaskDescription = findViewById(R.id.txvErrorTaskDescription);
        TextView txvErrorTaskLimitDate = findViewById(R.id.txvErrorTaskLimitDate);

        Button btnAddResponsible = findViewById(R.id.btnAddResponsible);

        imgBtnCalendarIcon.setOnClickListener(v -> {
            openDatePickerDialog();
        });

        edtTaskLimitDate.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;
            private boolean deleting;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                deleting = count > 0;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) {
                    return;
                }

                isFormatting = true;

                int length = s.length();
                if (!deleting && (length == 3 || length == 6)) {
                    s.insert(length - 1, "/");
                } else if (deleting && (length == 2 || length == 5)) {
                    s.delete(length - 1, length);
                }

                isFormatting = false;
            }
        });

        btnAddResponsible.setOnClickListener(v -> {
            if(loggedIn){
                Intent intentAddResponsible = new Intent(getBaseContext(), AddResponsibleActivity.class);

                intentAddResponsible.putIntegerArrayListExtra("userIds", responsiblesUsersId);
                intentAddResponsible.putIntegerArrayListExtra("userIdMembers", userIdMembers);

                activityLauncher.launch(intentAddResponsible);
            }
        });

        Button btnCreateTask = findViewById(R.id.btnCreateTask);

        btnCreateTask.setOnClickListener(v -> {
            boolean isValid = true;
            String errorMessage;
            FormValidation validator = new FormValidation();

            if(validator.isEmpty(edtTaskName.getText().toString())){
                isValid = false;
                edtTaskName.requestFocus();

                errorMessage = "Nome da tarefa vazio";

                txvErrorTaskName.setText(errorMessage);
                txvErrorTaskName.setVisibility(View.VISIBLE);
            }else{
                txvErrorTaskName.setVisibility(View.INVISIBLE);
            }

            if(validator.isEmpty(edtTaskDescription.getText().toString())){
                isValid = false;
                edtTaskDescription.requestFocus();
                errorMessage = "Descrição da tarefa vazia";
                txvErrorTaskDescription.setText(errorMessage);
                txvErrorTaskDescription.setVisibility(View.VISIBLE);
            } else{
                txvErrorTaskDescription.setVisibility(View.INVISIBLE);
            }

            if(validator.isEmpty(edtTaskLimitDate.getText().toString())){
                isValid = false;
                edtTaskLimitDate.requestFocus();
                errorMessage = "Descrição da tarefa vazia";
                txvErrorTaskLimitDate.setText(errorMessage);
                txvErrorTaskLimitDate.setVisibility(View.VISIBLE);
            } else{
                txvErrorTaskLimitDate.setVisibility(View.INVISIBLE);
            }

            if(isValid){
                registerTask(edtTaskName.getText().toString(), edtTaskDescription.getText().toString(), edtTaskLimitDate.getText().toString(), loggedUserEmail);
            }
        });

    }

    public void openDatePickerDialog(){
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String formattedMonth = String.valueOf(month + 1);
                String formattedDayOfMonth= String.valueOf(dayOfMonth);

                if(month < 10)
                    formattedMonth = "0" + formattedMonth;

                if(dayOfMonth < 10)
                    formattedDayOfMonth = "0" + formattedDayOfMonth;

                edtTaskLimitDate.setText(formattedDayOfMonth + "/" + formattedMonth + "/" + String.valueOf(year));
            }
        }, currentYear, currentMonth, currentDay);

        dialog.show();
    }

    public void registerTask(String task_name, String task_description, String task_limit_date, String ownerEmail){
        TaskDAO taskDAO = new TaskDAO(this);
        UserTaskDAO userTaskDao = new UserTaskDAO(this);

        Task task = new Task();
        task.setName(task_name);
        task.setDescription(task_description);
        task.setLimitDate(task_limit_date);
        task.setIdGroup(selectedGroup.getIdGroup());

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date limitDate = sdf.parse(task_limit_date);
            if (limitDate.before(currentDate)) {
                task.setStatus("Atrasada");
            } else {
                task.setStatus("Pendente");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            task.setStatus("Pendente");
        }

        Task taskCreated = taskDAO.insert(task);

        if(ownerEmail != null){
            if(!responsiblesUsersId.isEmpty()){
                for (int idResponsible : responsiblesUsersId) {
                    UserTask userTask = new UserTask();

                    userTask.setIdTask(taskCreated.getIdTask());
                    userTask.setIdUser(idResponsible);

                    userTaskDao.insert(userTask);
                }
            }
        }

        Intent taskCreatedIntent = new Intent(this, GroupActivity.class);
        taskCreatedIntent.putExtra("selectedGroup", selectedGroup.getIdGroup());
        startActivity(taskCreatedIntent);

        Toast.makeText(this, "Tarefa criada com sucesso!",
                Toast.LENGTH_LONG).show();
    }

    private void updateResponsibles() {
        responsiblesForTask.clear();

        if (responsiblesUsersId != null && responsiblesUsersId.size() > 0) {
            for (Integer userId : responsiblesUsersId) {
                for (User user : users) {
                    if (user.getIdUser() == userId) {
                        if(user.getIdUser() == userId && user.getIdUser() == loggedUser.getIdUser()){
                            user.setName("Você");
                        }
                        responsiblesForTask.add(user);
                        break;
                    }
                }
            }
        }

        ((MemberArrayAdapter) ltvTaskResponsibleList.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void removeMember(int idUserToDelete) {

        for(int i = 0; i < responsiblesUsersId.size(); i++){
            int currentUserId = responsiblesUsersId.get(i);
            if(currentUserId == idUserToDelete){
                responsiblesUsersId.remove(i);
                updateResponsibles();
                break;
            }
        }

        ((MemberArrayAdapter) ltvTaskResponsibleList.getAdapter()).notifyDataSetChanged();
    }
}