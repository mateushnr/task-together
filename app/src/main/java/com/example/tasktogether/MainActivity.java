package com.example.tasktogether;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                String errorMessage = "";
                FormValidation validator = new FormValidation();


                if(validator.isEmpty(edtEmail.getText().toString()) || !validator.isEmailValid(edtEmail.getText().toString())){
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
            }
        });


    }
}