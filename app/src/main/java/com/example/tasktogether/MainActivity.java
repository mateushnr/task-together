package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import validation.FormValidation;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                System.out.println("valido");
            }else{
                System.out.println("invalido");
            }
        });

        txvLinkSignUp.setOnClickListener(v -> startActivity(intentSignUp));
        btnContinueWithoutLogin.setOnClickListener(v -> startActivity(intentMyGroups));

    }
}