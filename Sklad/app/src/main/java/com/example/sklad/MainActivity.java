package com.example.sklad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView Register, ForgotPassword;
    private EditText editTextEmailAddress, editTextPassword;
    private Button button;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Register = (TextView) findViewById(R.id.Register);
        Register.setOnClickListener(this);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);


        editTextEmailAddress = (EditText) findViewById(R.id.Email);
        editTextPassword = (EditText) findViewById(R.id.Password);


        mAuth = FirebaseAuth.getInstance();

        ForgotPassword = (TextView) findViewById(R.id.forgotPassword);
        ForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Register:
                startActivity(new Intent(this, RegisterUser.class));
                break;


            case R.id.button:
                userLogin();
                break;

            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String Email = editTextEmailAddress.getText().toString().trim();
        String Password = editTextPassword.getText().toString().trim();

        if(Email.isEmpty()){
            editTextEmailAddress.setError("Зполните поле Email");
            editTextEmailAddress.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editTextEmailAddress.setError("Неправильный Email");
            editTextEmailAddress.requestFocus();
            return;
        }

        if(Password.isEmpty()){
            editTextPassword.setError("Заполните поле пароль");
            editTextPassword.requestFocus();
            return;
        }

        if(Password.length() < 6){
            editTextPassword.setError("Минимальная длина пароля должна быть 6 символов");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()) {
                        startActivity(new Intent(MainActivity.this, SkladActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Вам отправлено сообщение на почту, подтвердите электронный адрес", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}