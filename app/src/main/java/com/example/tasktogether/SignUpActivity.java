package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import validation.FormValidation;

public class SignUpActivity extends AppCompatActivity {

    private String lastCharEdtPhone = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
                System.out.println("valido");
            }else{
                System.out.println("invalido");
            }
        });

        btnBackToLogin.setOnClickListener(v -> finish());

    }
}