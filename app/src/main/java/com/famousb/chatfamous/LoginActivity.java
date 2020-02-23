package com.famousb.chatfamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;

    //firebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmailView = findViewById(R.id.emailEditText);
        mPasswordView = findViewById(R.id.passwordEditText);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if(actionId == R.id.login_form || actionId == EditorInfo.IME_NULL){
                        attemptLogin();
                    return true;
                }
                return false;
            }
        });

        //Get the instance of firebase
        mAuth = FirebaseAuth.getInstance();
    }


    public void Login(View view) {
        attemptLogin();
    }

    //Login validation
    private void attemptLogin(){
        mEmailView.setError(null);
        mPasswordView.setError(null);


        View focusView = null;
        boolean cancel = false;

        final String emailvalue = mEmailView.getText().toString();
        final String passwordValue = mPasswordView.getText().toString();

        if(TextUtils.isEmpty(emailvalue) || !isEmailValid(emailvalue)){
            mEmailView.setError("invalid email address");
            focusView = mEmailView;
            cancel = true;
        }else if(!isPasswordValid(passwordValue) || TextUtils.isEmpty(passwordValue)){
            mPasswordView.setError("Password not valid!");
            focusView = mPasswordView;
            cancel = true;
        }

        if(cancel){

            //if there was an error, focus the first form field with error
            focusView.requestFocus();

        }else {

            //Login existing user
            loginExistingUser();
            //Toast.makeText(this, "Login in progress....", Toast.LENGTH_SHORT).show();

        }
    }


    private boolean isEmailValid(String email){

        return email.contains("@");
    }

    private boolean isPasswordValid(String password){

        //String enterPassword = mPasswordView.getText().toString();
        return password.length() > 5;

    }

    private void loginExistingUser(){
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("ChatFamous", "signinWithEmail onComplete: " + task.isSuccessful());

                Toast.makeText(getApplicationContext(), "Login in progress....", Toast.LENGTH_SHORT).show();

                if(!task.isSuccessful()){

                    Log.d("ChatFamous", "signinWitheamil onFailed!: " + task.getException());
                    //Toast.makeText(LoginActivity.this, "invalid email/password", Toast.LENGTH_SHORT).show();
                    showErrorBox("invalid email/password");
                }else{
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    private void  showErrorBox(String message){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
