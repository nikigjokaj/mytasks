package com.codebee.mytasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText mFullName, mEmail, mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn, mTerms, gender;
    ProgressBar progressBar2;
    FirebaseAuth fAuth;
    CheckBox mCheckBox;
    RadioButton male, female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName   = findViewById(R.id.Name);
        mEmail      = findViewById(R.id.Email);
        mPassword   = findViewById(R.id.Password);
        mLoginBtn   = findViewById(R.id.login);
        mRegisterBtn= findViewById(R.id.registerBtn);
        mTerms      = findViewById(R.id.Terms);
        mCheckBox   = findViewById(R.id.checkBox);
        fAuth       = FirebaseAuth.getInstance();
        male        = findViewById(R.id.male);
        female      = findViewById(R.id.female);
        gender      = findViewById(R.id.GenderTxt);

        progressBar2= findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);


        /*
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        */


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                final String checkbox = mCheckBox.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }


                if (mCheckBox.isChecked()) {
                } else {
                    mCheckBox.setError("You must agree with the Terms of Service");
                    return;
                }

                if (male.isChecked() || female.isChecked()) {
                } else {
                    gender.setError("Must choose a gender");
                    return;
                }


                progressBar2.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });


        mTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TermsOfSeviceActivity.class));
            }
        });
    }
}