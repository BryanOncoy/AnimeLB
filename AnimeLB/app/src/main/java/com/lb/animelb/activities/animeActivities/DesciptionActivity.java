package com.lb.animelb.activities.animeActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.lb.animelb.R;

public class DesciptionActivity extends AppCompatActivity {
    public static final String DESCRIPTION = "com.lb.animelb.DESCRIPTION";

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desciption);

        // Hooks
        textView= findViewById(R.id.textView11);

        String sDescrip = getIntent().getStringExtra(DESCRIPTION);
        textView.setText(sDescrip);
    }
}