package com.earthgee.simplechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.earthgee.simplechat.R;

/**
 * Created by earthgee on 17/2/21.
 */
public class AddNewChatActivity extends AppCompatActivity{

    private EditText addUserId;
    private Button addChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        addUserId= (EditText) findViewById(R.id.et_add_user_id);
        addChat= (Button) findViewById(R.id.btn_chat);

        addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId=addUserId.getText().toString();
                Intent intent=new Intent(AddNewChatActivity.this,ChatActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
                finish();
            }
        });

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
