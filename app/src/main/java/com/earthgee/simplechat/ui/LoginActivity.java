package com.earthgee.simplechat.ui;

import android.content.Context;
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
    }

    private void login(){
        if(!checkNetState()){
            Toast.makeText(this,"请检查网络连接",Toast.LENGTH_SHORT).show();
        }

        String userName=userNameLayout.getText().toString();
        String password=passwordLayout.getText().toString();
        waitProgress.setVisibility(View.VISIBLE);
        RequestSender.getInstance().sendLoginRequest(userName,password,this);
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
    public void onSendSuccess() {

    }

    @Override
    public void onSendFail() {

    }
}

