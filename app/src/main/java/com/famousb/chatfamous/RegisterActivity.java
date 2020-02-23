package com.famousb.chatfamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class RegisterActivity extends AppCompatActivity {

    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";

    private EditText usernameView;
    private EditText emailView;
    private EditText passwordView;
    private EditText cPasswordView;


    //Firebase Auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameView = findViewById(R.id.usernameEditText);
        emailView = findViewById(R.id.emailEditText2);
        passwordView = findViewById(R.id.passwordEditText2);
        cPasswordView = findViewById(R.id.cPasswordEditText2);

        cPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == R.id.register_form || actionId == EditorInfo.IME_NULL){
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        //Firebase instance
        mAuth = FirebaseAuth.getInstance();

    }

    public void registerButton(View view) {

        attemptRegistration();

    }

    private void attemptRegistration(){
        emailView.setError(null);
        passwordView.setError(null);
        cPasswordView.setError(null);

        View focusView = null;
        boolean cancel = false;


        final String vPassword = passwordView.getText().toString();
        final String vEmail = emailView.getText().toString();
        String verifyPassword = cPasswordView.getText().toString();
        final String vUsername = usernameView.getText().toString();

        if(!isPasswordValid(vPassword)){
            passwordView.setError("The password must be grater than 5");
            focusView = passwordView;
            cancel = true;

        }else if(!isEmailValid(vEmail)){
            emailView.setError("The email address is invalid");
            focusView = emailView;
            cancel = true;

        }else if (TextUtils.isEmpty(verifyPassword) || !vPassword.equals(verifyPassword)) {
            cPasswordView.setError("password not matched!");
            focusView = cPasswordView;
            cancel = true;

        }else if (TextUtils.isEmpty(vUsername)) {
            usernameView.setError("Username is empty!");
            focusView = usernameView;
            cancel = true;
        }

        if(cancel){
            //if there was an error, focus the first form field with error
            focusView.requestFocus();

        }else{

            //call create Firebase user
            createNewFirebaseUser();
        }
    }

    //validate email
    private boolean isEmailValid(String email){

        return email.contains("@");
    }

    private boolean isPasswordValid(String password){

        String confirmPassword = cPasswordView.getText().toString();
        return  confirmPassword.equals(password) && password.length() > 5;
    }

    //create new firebase users
    private void createNewFirebaseUser(){
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("ChatFamous", "createUser onComplete: " + task.isSuccessful());
                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                    //showSuccessBox("Registration Successful");
                if(!task.isSuccessful()){
                    Log.d("ChatFamous", "onCreate User failed");
                    showErrorBox("Registration Failed!!");
                    //Toast.makeText(RegisterActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }else {
                    saveDisplayName();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    //Show error dialog box
    private  void showErrorBox(String message){
        new AlertDialog.Builder(this)
                .setTitle("oOOP")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    //showing success box
    private  void showSuccessBox(String message){
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    //save username local: local storage using SHAREDPREFERENCES

    private void saveDisplayName(){
        String displayName = usernameView.getText().toString();
        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS, 0);
        prefs.edit().putString(DISPLAY_NAME_KEY, displayName).apply();
    }

}
