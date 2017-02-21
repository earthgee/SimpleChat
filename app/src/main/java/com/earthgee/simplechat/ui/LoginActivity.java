package com.earthgee.simplechat.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.earthgee.simplechat.R;
import com.earthgee.simplechat.net.ISendListener;
import com.earthgee.simplechat.net.RequestSender;

/**
 * A login screen that offers login via username/password.
 * 使用as自带loginactivity节省时间
 */
public class LoginActivity extends AppCompatActivity implements ISendListener{

    private ProgressBar waitProgress;
    private AutoCompleteTextView userNameLayout;
    private EditText passwordLayout;
    private Button loginBtn;

    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

                waitProgress.setVisibility(View.GONE);
                int code = intent.getIntExtra("code", -1);
                if (code == 0) {
                    Intent intent2=new Intent(LoginActivity.this, MainActivity.class);
                    intent2.putExtra("userId",intent.getIntExtra("userId",-1));
                    startActivity(intent2);
                    finish();
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        waitProgress= (ProgressBar) findViewById(R.id.login_progress);
        userNameLayout = (AutoCompleteTextView) findViewById(R.id.email);
        passwordLayout= (EditText) findViewById(R.id.password);
        loginBtn= (Button) findViewById(R.id.email_sign_in_button);
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        IntentFilter intentFilter=new IntentFilter("com.earthgee.login");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void login(){
        if(!checkNetState()){
            Toast.makeText(this,"请检查网络连接",Toast.LENGTH_SHORT).show();
        }

        String userName=userNameLayout.getText().toString();
        String password=passwordLayout.getText().toString();
        waitProgress.setVisibility(View.VISIBLE);
        RequestSender.getInstance().sendLoginRequest(userName, password, this);
    }

    private boolean checkNetState(){
        boolean flag=false;
        ConnectivityManager manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager.getActiveNetworkInfo()!=null){
            flag=manager.getActiveNetworkInfo().isAvailable();
        }
        return flag;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onSendSuccess() {

    }

    @Override
    public void onSendFail() {

    }
}

