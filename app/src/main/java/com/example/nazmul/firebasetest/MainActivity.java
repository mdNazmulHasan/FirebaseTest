package com.example.nazmul.firebasetest;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "signupPage";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    TextView secondaryText;
    boolean login=true;
    EditText emailET, pswrdET;
    Button button;
    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        secondaryText= (TextView) findViewById(R.id.secondaryLinkText);
        emailET= (EditText) findViewById(R.id.emailET);
        pswrdET = (EditText) findViewById(R.id.emailET);
        button= (Button) findViewById(R.id.submitBtn);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }
    @Override
    public void onStart() {
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

    public void loginOrSignUp(View view) {
        email=emailET.getText().toString().trim();
        password=pswrdET.getText().toString().trim();
        if(login){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }else {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    // Name, email address, and profile photo Url
                                    String name = user.getDisplayName();
                                    String email = user.getEmail();
                                    Uri photoUrl = user.getPhotoUrl();
                                    Toast.makeText(MainActivity.this, ""+name, Toast.LENGTH_SHORT).show();
                                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                                    // authenticate with your backend server, if you have one. Use
                                    // FirebaseUser.getToken() instead.
                                    String uid = user.getUid();
                                }
                            }

                            // ...
                        }
                    });
            Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
            Toast.makeText(this, "signup", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeState(View view) {
        if(login){
            login=false;
            secondaryText.setText("Already have an account! Log in");
            button.setText("Sign up");

        }else {
            login=true;
            secondaryText.setText("Not Registered Yet! Sign Up");
            button.setText("Login");
        }
    }
}
