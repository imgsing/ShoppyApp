package com.example.akal.shoppyapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OpenScreen extends AppCompatActivity {
    private static final String TAG = OpenScreen.class.getSimpleName();
    private Button loginButton;
    private EditText user,pass;
    private String username,password;
    private TextView newUser;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);
        loginButton = (Button) findViewById(R.id.loginButton);
        user =(EditText) findViewById(R.id.usernameLogin);
        pass =(EditText) findViewById(R.id.passwordLogin);
        setInputs(true);
        newUser = (TextView) findViewById(R.id.newUserRegistration);
        progressBar = (ProgressBar) findViewById(R.id.loginPageProgressBar);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d(TAG,"onAuthStateChanged:Signed_in"+user.getUid());
                    Toast.makeText(getApplicationContext(),"Welcome!",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(),MainAppPage.class));
                    finish();
                }else {
                    Log.d(TAG,"onAuthStateChanged:signed_out");
                }
            }

            };

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    username = user.getText().toString();
                    password = pass.getText().toString();
                    setInputs(false);
                    signIn(username, password);

                }
            });
            newUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newUserReg = new Intent(OpenScreen.this, newUser.class);
                    startActivity(newUserReg);
                    finish();
                }
            });


        }
        @Override
        public void onStart(){
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            setInputs(true);
                        }

                        // ...
                    }
                });

    }
    private void setInputs(Boolean val){
        user.setEnabled(val);
        pass.setEnabled(val);
    }
}
