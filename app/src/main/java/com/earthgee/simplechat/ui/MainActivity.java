package com.earthgee.simplechat.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.earthgee.simplechat.R;

/**
 * Created by earthgee on 17/2/13.
 */
public class MainActivity extends AppCompatActivity{

    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().setTitle(getIntent().getIntExtra("userId",-1));
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("我的userId:"+String.valueOf(getIntent().getIntExtra("userId",-1)));

        floatingActionButton= (FloatingActionButton) findViewById(R.id.add_chat);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddNewChatActivity.class));
            }
        });
    }
}
