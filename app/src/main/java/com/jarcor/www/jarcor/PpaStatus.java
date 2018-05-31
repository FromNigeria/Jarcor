package com.jarcor.www.jarcor;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PpaStatus extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mPpa;
    private Button mPpaBtn;

    //Firebase
    private DatabaseReference mPpaDatabase;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ppa_status);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mPpaDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mToolbar = (Toolbar)findViewById(R.id.status_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Update Current PPA location");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String ppa_value = getIntent().getStringExtra("ppa_value");

        mPpa = (EditText)findViewById(R.id.ppa_input);
        mPpaBtn = (Button)findViewById(R.id.save_changesppa);

        mPpa.setText(ppa_value);


        mPpaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgress = new ProgressDialog(PpaStatus.this);
                mProgress.setTitle("Updating");
                mProgress.setMessage("please wait while we update your changes");
                mProgress.show();

                String ppa_location = mPpa.getText().toString();

                mPpaDatabase.child("ppa_location").setValue(ppa_location).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            mProgress.dismiss();
                        }
                        else{
                            Toast.makeText(PpaStatus.this, "Error saving your changes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
