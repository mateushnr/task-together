package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import Model.User;
import Model.dao.UserDAO;
import validation.FormValidation;

public class SignUpActivity extends AppCompatActivity {

    private String lastCharEdtPhone = "";

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPref";

    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        if(sharedPreferences.getString(KEY_EMAIL, null) != null){
            Intent alreadyLoggedIntent = new Intent(this, MyGroupsActivity.class);
            startActivity(alreadyLoggedIntent);
        }

        EditText edtName = findViewById(R.id.edtSignUpName);
        EditText edtEmail = findViewById(R.id.edtSignUpEmail);
        EditText edtPhone = findViewById(R.id.edtSignUpPhone);
        EditText edtPassword = findViewById(R.id.edtSignUpPassword);

        TextView txvErrorName = findViewById(R.id.txvErrorSignUpName);
        TextView txvErrorEmail = findViewById(R.id.txvErrorSignUpEmail);
        TextView txvErrorPhone = findViewById(R.id.txvErrorSignUpPhone);
        TextView txvErrorPassword = findViewById(R.id.txvErrorSignUpPassword);

        Button btnSignUp = findViewById(R.id.btnSignUp);
        LinearLayout btnBackToLogin = findViewById(R.id.btnBackToLogin);

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int phoneLength = s.toString().length();

                if (phoneLength > 1){
                    lastCharEdtPhone = edtPhone.getText().toString().substring(phoneLength - 1);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int phoneLength = s.toString().length();

                if(phoneLength > 0){
                    if(!Character.isDigit(s.charAt(phoneLength - 1)) && s.charAt(phoneLength - 1) != ' ' && s.charAt(phoneLength - 1) != '-'){
                        edtPhone.getText().delete(phoneLength - 1, phoneLength);
                    }
                }

                if(phoneLength == 2){
                    if(!lastCharEdtPhone.equals(" ")){
                        edtPhone.append(" ");
                    }else{
                        edtPhone.getText().delete(phoneLength - 1, phoneLength);
                    }
                } else if(phoneLength == 8){
                    if(!lastCharEdtPhone.equals("-")){
                        edtPhone.append("-");
                    }else{
                        edtPhone.getText().delete(phoneLength - 1, phoneLength);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnSignUp.setOnClickListener(v -> {
            boolean isValid = true;
            String errorMessage;
            FormValidation validator = new FormValidation();

            if(validator.isEmpty(edtName.getText().toString())){
                isValid = false;
                edtName.requestFocus();

                errorMessage = "Nome vazio";

                txvErrorName.setText(errorMessage);
                txvErrorName.setVisibility(View.VISIBLE);
            }else{
                txvErrorName.setVisibility(View.INVISIBLE);
            }

            if(validator.isEmpty(edtEmail.getText().toString()) || validator.isEmailInvalid(edtEmail.getText().toString())){
                isValid = false;
                edtEmail.requestFocus();

                errorMessage = (validator.isEmpty(edtEmail.getText().toString())) ? "Email vazio" : "Insira um email válido";

                txvErrorEmail.setText(errorMessage);
                txvErrorEmail.setVisibility(View.VISIBLE);
            }else{
                txvErrorEmail.setVisibility(View.INVISIBLE);
            }

            if(validator.isEmpty(edtPhone.getText().toString()) || validator.isBelowMinLength(edtPhone.getText().toString(), 13)){
                isValid = false;
                edtPhone.requestFocus();

                errorMessage = (validator.isEmpty(edtPhone.getText().toString())) ? "Telefone vazio" : "Telefone inválido, preencha ate o final";

                txvErrorPhone.setText(errorMessage);
                txvErrorPhone.setVisibility(View.VISIBLE);
            }else{
                txvErrorPhone.setVisibility(View.INVISIBLE);
            }

            if(validator.isEmpty(edtPassword.getText().toString()) || validator.isBelowMinLength(edtPassword.getText().toString(), 6)){
                isValid = false;
                edtPassword.requestFocus();
                errorMessage = (validator.isEmpty(edtPassword.getText().toString())) ? "Senha vazia" : "A senha deve ter pelo menos 6 caracteres";
                txvErrorPassword.setText(errorMessage);
                txvErrorPassword.setVisibility(View.VISIBLE);
            } else{
                txvErrorPassword.setVisibility(View.INVISIBLE);
            }

            if(isValid){
                registerUser(edtName.getText().toString(),
                            edtEmail.getText().toString(),
                            edtPhone.getText().toString(),
                            edtPassword.getText().toString());
            }
        });

        btnBackToLogin.setOnClickListener(v -> finish());

    }

    public void registerUser(String user_name, String user_email, String user_phone, String user_password){
        UserDAO userDao = new UserDAO(this);
        User user = new User();

        user.setName(user_name);
        user.setEmail(user_email);
        user.setPhone(user_phone);
        user.setPassword(user_password);

        if(userDao.searchByPhone(user_phone) != null && userDao.searchByEmail(user_email) != null){
            Toast.makeText(this, "Telefone e email já cadastrado, tente um número e email diferente",
                    Toast.LENGTH_LONG).show();
        }else if(userDao.searchByPhone(user_phone) != null){
            Toast.makeText(this, "Telefone já cadastrado, tente um número diferente",
                    Toast.LENGTH_LONG).show();
        }else if(userDao.searchByEmail(user_email) != null){
            Toast.makeText(this, "Email já cadastrado, tente um email diferente",
                    Toast.LENGTH_LONG).show();
        }else{
            userDao.insert(user);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_NAME, user_name);
            editor.putString(KEY_EMAIL, user_email);
            editor.putString(KEY_PHONE, user_phone);

            editor.apply();

            Intent loginFromSignUpIntent = new Intent(this, MyGroupsActivity.class);
            startActivity(loginFromSignUpIntent);

            Toast.makeText(this, "Cadastrado com sucesso",
                    Toast.LENGTH_SHORT).show();
        }
    }
}