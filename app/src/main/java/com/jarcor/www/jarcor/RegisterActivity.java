package com.jarcor.www.jarcor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jarcor.www.jarcor.customfonts.MyTextView;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MyTextView signinhere;
    Typeface fonts1;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText mDisplayName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mCreateBtn;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        signinhere = (MyTextView) findViewById(R.id.signinhere);

        signinhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(it);


            }
        });

        mProgress = new ProgressDialog(this);

        //Iitializing the Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        mDisplayName = (EditText)findViewById(R.id.regDisplayname);
        mEmail = (EditText)findViewById(R.id.regEmail);
        mPassword = (EditText)findViewById(R.id.regPassword);
        mCreateBtn = (Button)findViewById(R.id.regButton);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String displayname = mDisplayName.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();


                if (!TextUtils.isEmpty(displayname) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){

                    mProgress.setTitle("Registering Account");
                    mProgress.setMessage("Please wait......while  Registering your account");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();
                    register_user(displayname, email, password);
                }


            }
        });

    }

    private void register_user(final String displayname, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();
                    //Below code is our main root directory on our server
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", displayname);
                    userMap.put("status", "Hi there, iLove this App.hehe!");
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");
                    userMap.put("ppa_location", "default");
                    userMap.put("device_token", device_token);


                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                mProgress.dismiss();

                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();

                            }
                        }
                    });

                } else{

                    mProgress.hide();
                    //if the user didn't register sucessfully, show him his error instead of crashing the app
                    Toast.makeText(RegisterActivity.this, "Registration failed, try to sign again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
