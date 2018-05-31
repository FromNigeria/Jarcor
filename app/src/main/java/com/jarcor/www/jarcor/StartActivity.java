package com.jarcor.www.jarcor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jarcor.www.jarcor.customfonts.MyTextView;

public class StartActivity extends AppCompatActivity {

    TextView getstarted;
    TextView signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);


        getstarted = (MyTextView)findViewById(R.id.getstarted);
        signin = (MyTextView)findViewById(R.id.signin);

        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(it);

            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(it);


            }
        });
    }
}
