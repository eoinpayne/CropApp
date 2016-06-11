package com.eoinpayne.crop.cropapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Hefty Balls on 05/06/2016.
 */
public class CatalogueActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accordion_catalogue);

        final Button findMagicBtn = (Button) findViewById(R.id.magic_btn);
        findMagicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout findMagicLl = (LinearLayout) findViewById(R.id.magic_layout);
//                findMagicBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arrow, 0);
                if (findMagicLl.getVisibility() == View.VISIBLE) {
//                    findMagicBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);
                    findMagicLl.setVisibility(View.GONE);
                } else {
//                    findMagicBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                    findMagicLl.setVisibility(View.VISIBLE);
                }

            }
        });



    } //close on create
} //close class
