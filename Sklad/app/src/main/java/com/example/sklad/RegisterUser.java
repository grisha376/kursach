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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private TextView Home, registerUser;
    private EditText editTextTextPersonName,editTextEmailAddress2,editTextPassword2;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        Home = (TextView) findViewById(R.id.Home);
        Home.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextTextPersonName = (EditText) findViewById(R.id.Name);
        editTextEmailAddress2 = (EditText) findViewById(R.id.Email);
        editTextPassword2 = (EditText) findViewById(R.id.Password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Home:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String Email = editTextEmailAddress2.getText().toString().trim();
        String Name = editTextTextPersonName.getText().toString().trim();
        String Password = editTextPassword2.getText().toString().trim();

        if(Name.isEmpty()){
            editTextTextPersonName.setError("Заполните поле Имя");
            editTextTextPersonName.requestFocus();
            return;
        }
        if(Email.isEmpty()){
            editTextEmailAddress2.setError("Заполните поле Email ");
            editTextEmailAddress2.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editTextEmailAddress2.setError("Неправильный Email");
            editTextEmailAddress2.requestFocus();
            return;
        }

        if(Password.isEmpty()){
            editTextPassword2.setError("Заполните поле Пароль!");
            editTextPassword2.requestFocus();
            return;
        }

        if(Password.length() < 6){
            editTextPassword2.setError("Минимальная длина пароля должна быть 6 символов");
            editTextPassword2.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User (Name, Email);

                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "Пользователь был зарегистрирован ", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(RegisterUser.this, "Неудалось зарегистрироваться! Попробуйте еще раз", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                        }else{
                            Toast.makeText(RegisterUser.this, "Неудалось зарегистрироваться! Попробуйте еще раз", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}