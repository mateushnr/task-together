package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import Model.User;
import Model.dao.UserDAO;
import validation.FormValidation;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPref";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        if(sharedPreferences.getString(KEY_EMAIL, null) != null){
            Intent alreadyLoggedIntent = new Intent(this, MyGroupsActivity.class);
            startActivity(alreadyLoggedIntent);
        }

        EditText edtEmail = findViewById(R.id.edtLoginEmail);
        EditText edtPassword = findViewById(R.id.edtLoginPassword);
        TextView txvErrorEmail = findViewById(R.id.txvErrorEmail);
        TextView txvErrorPassword = findViewById(R.id.txvErrorPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        TextView txvLinkSignUp = findViewById(R.id.txvLinkSignUp);
        LinearLayout btnContinueWithoutLogin = findViewById(R.id.btnContinueWithoutLogin);

        Intent intentSignUp = new Intent(this, SignUpActivity.class);
        Intent intentMyGroups = new Intent(this, MyGroupsActivity.class);

        btnLogin.setOnClickListener(v -> {
            boolean isValid = true;
            String errorMessage;
            FormValidation validator = new FormValidation();

            if(validator.isEmpty(edtEmail.getText().toString()) || validator.isEmailInvalid(edtEmail.getText().toString())){
                isValid = false;
                edtEmail.requestFocus();

                errorMessage = (validator.isEmpty(edtEmail.getText().toString())) ? "Email vazio" : "Insira um email válido";

                txvErrorEmail.setText(errorMessage);
                txvErrorEmail.setVisibility(View.VISIBLE);
            }else{
                txvErrorEmail.setVisibility(View.INVISIBLE);
            }

            if(validator.isEmpty(edtPassword.getText().toString())){
                isValid = false;
                edtPassword.requestFocus();
                errorMessage = "Senha vazia";
                txvErrorPassword.setText(errorMessage);
                txvErrorPassword.setVisibility(View.VISIBLE);
            } else{
                txvErrorPassword.setVisibility(View.INVISIBLE);
            }

            if(isValid){
                authUser(edtEmail.getText().toString(), edtPassword.getText().toString());
            }
        });

        txvLinkSignUp.setOnClickListener(v -> startActivity(intentSignUp));
        btnContinueWithoutLogin.setOnClickListener(v -> startActivity(intentMyGroups));

    }

    public void authUser(String user_email, String user_password){
        UserDAO userDao = new UserDAO(this);

        User userFound = userDao.searchByEmail(user_email);

        if(userFound == null){
            Toast.makeText(this, "Email ou senha incorreto",
                    Toast.LENGTH_LONG).show();
        }else{
            if(!Objects.equals(userFound.getPassword(), user_password)){
                Toast.makeText(this, "Email ou senha incorreto",
                        Toast.LENGTH_LONG).show();
            }else{
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_NAME, userFound.getName());
                editor.putString(KEY_EMAIL, userFound.getEmail());
                editor.putString(KEY_PHONE, userFound.getPhone());

                editor.apply();

                Intent loginIntent = new Intent(this, MyGroupsActivity.class);
                startActivity(loginIntent);

                Toast.makeText(this, "Logado com sucesso",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}