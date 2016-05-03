package com.eoinpayne.crop.cropapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    //create edit text variable
    EditText Name, Email, mPassword, ConfirmPassword;
    //declare variable for register button
    Button register_btn;
    //declaring builder for alert box
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //initialise these varaibles
        Name = (EditText)findViewById(R.id.registration_name);
        Email = (EditText)findViewById(R.id.registration_email);
        mPassword = (EditText)findViewById(R.id.registration_password);
        ConfirmPassword = (EditText)findViewById(R.id.registration_confirm_password);
        register_btn = (Button)findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //if NO data enetered..alert
                if (Name.getText().toString().equals("") || Email.getText().toString().equals("") ||
                        mPassword.getText().toString().equals(""))
                {
                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle(("Error, something went wrong"));
                    builder.setMessage("Please fill all fields");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });  //close positive listener
                    AlertDialog alertDialog = builder.create();  //here we display the alert dialogue
                    alertDialog.show();
                }
                //if data entered, but passwords DON'T match..alert
                else if (!(mPassword.getText().toString().equals(ConfirmPassword.getText().toString())))
                {
                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle(("Error, something went wrong"));
                    builder.setMessage("Passwords do not match");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            mPassword.setText("");  //reset fields to blank
                            ConfirmPassword.setText("");
                        }
                    });  //close positive listener
                    AlertDialog alertDialog = builder.create();  //here we display the alert dialogue
                    alertDialog.show();
                }
                else  //****else user provided proper data, we implement background operations! ***
                {
                    //start background task .. pass in RegisterActivity context to constructor in BackgroundTask.java
                    BackgroundTask backgroundTask = new BackgroundTask(RegisterActivity.this);
                    backgroundTask.execute("register", Name.getText().toString(),
                            Email.getText().toString(), mPassword.getText().toString());

                } //else

            } //on click
        }); //register btn  onclicklistener
    } //on create
} //class
