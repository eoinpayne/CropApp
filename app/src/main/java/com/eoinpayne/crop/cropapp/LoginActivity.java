package com.eoinpayne.crop.cropapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {


    EditText Email, mPassword;
    Button login_btn;
    TextView signup_text;
    AlertDialog.Builder builder;
    HomeActivity.HomeBackgroundTask homeBackgroundTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        login_btn = (Button)findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //if NO data enetered..alert
                if (Email.getText().toString().equals("") || mPassword.getText().toString().equals(""))
                {
                    builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle(("Error, something went wrong"));
                    builder.setMessage("Please fill all fields");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });  //close positive listener
                    AlertDialog alertDialog = builder.create();  //here we display the alert dialogue
                    alertDialog.show();
                }



                else  //****else user provided proper data, we implement background operations! ***
                {
                    //start background task .. pass in RegisterActivity context to constructor in BackgroundTask.java
                    BackgroundTask backgroundTask = new BackgroundTask(LoginActivity.this);
                    backgroundTask.execute("login", Email.getText().toString(), mPassword.getText().toString());
//                    backgroundTask.execute("login", "z", "z");

                } //else



            }//onclick
        });





        signup_text = (TextView)findViewById(R.id.sign_up);
        signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating new intent for login activity and its target of RegisterActivity.class (go to regActivty,java)

//                new HomeActivity.HomeBackgroundTask().execute("display_gardens", UserID_String);
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });
    }
}
