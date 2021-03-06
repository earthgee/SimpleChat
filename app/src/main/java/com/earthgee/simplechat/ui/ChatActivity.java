package com.earthgee.simplechat.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.earthgee.simplechat.R;
import com.earthgee.simplechat.net.ConnectionManager;
import com.earthgee.simplechat.net.RequestSender;
import com.earthgee.simplechat.net.entity.ChatEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by earthgee on 17/2/21.
 */
public class ChatActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private EditText inputText;
    private Button sendBtn;

    private List<ChatEntity> contents=new ArrayList<>();
    private ChatAdapter mAdapter;

    private String userId;
    private String mUserId;

    private class ChatReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String from=intent.getStringExtra("from");
            String content=intent.getStringExtra("content");
            if(from.equals(userId)){
                contents.add(new ChatEntity(ChatEntity.TYPE_TEXT,ChatEntity.FROM_OTHER,content));
                mAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        }
    }

    private ChatReceiver chatReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userId=getIntent().getStringExtra("userId");
        mUserId= String.valueOf(ConnectionManager.getInstance().
                getClientCore().getCurrentUserId());

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView= (RecyclerView) findViewById(R.id.list);
        inputText= (EditText) findViewById(R.id.input);
        sendBtn= (Button) findViewById(R.id.send);

        initRecyclerView();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content=inputText.getText().toString();
                if(content!=null&&content.length()>0){
                    new RequestSender.SendTask
                            (userId,content,true){

                        @Override
                        protected void onPostExecute(Integer integer) {
                            if(integer==0){
                                //发送成功
                                contents.add(new ChatEntity(ChatEntity.TYPE_TEXT,ChatEntity.FROM_ME,content));
                                mAdapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                            }else{
                                Toast.makeText(ChatActivity.this,"发送失败",Toast.LENGTH_SHORT).show();;
                            }
                            hideKeyBoard();
                        }

                    }.execute();
                }
            }
        });
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter=new ChatAdapter());
    }

    private class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder=null;

            if(viewType==0){
                View contentView=LayoutInflater.from(ChatActivity.this).
                        inflate(R.layout.item_chat_1, parent, false);
                viewHolder=new FromMeViewHolder(contentView);
            }else if(viewType==1){
                View contentView=LayoutInflater.from(ChatActivity.this).
                        inflate(R.layout.item_chat_2, parent, false);
                viewHolder=new FromOtherViewHolder(contentView);
            }else{
                View contentView=LayoutInflater.from(ChatActivity.this).
                        inflate(R.layout.item_chat, parent, false);
                viewHolder=new ViewHolder
                        (contentView);
            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof FromMeViewHolder){
                ((FromMeViewHolder)holder).getContent().setText(contents.get(position).getContent());
            }else if(holder instanceof FromOtherViewHolder){
                ((FromOtherViewHolder)holder).getContent().setText(contents.get(position).getContent());
            }else{

            }
        }

        @Override
        public int getItemCount() {
            return contents.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(contents.get(position).getFrom()==ChatEntity.FROM_ME){
                return 0;
            }else if(contents.get(position).getFrom()==ChatEntity.FROM_OTHER){
                return 1;
            }

            return -1;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            TextView content;

            public ViewHolder(View itemView) {
                super(itemView);
                content= (TextView) itemView.findViewById(R.id.content);
            }
        }

        class FromMeViewHolder extends RecyclerView.ViewHolder{

            private TextView content;

            public FromMeViewHolder(View itemView) {
                super(itemView);
                content= (TextView) itemView.findViewById(R.id.text);
            }

            public TextView getContent() {
                return content;
            }
        }

        class FromOtherViewHolder extends RecyclerView.ViewHolder{

            private TextView content;

            public FromOtherViewHolder(View itemView) {
                super(itemView);
                content= (TextView) itemView.findViewById(R.id.text);
            }

            public TextView getContent() {
                return content;
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(chatReceiver==null){
            chatReceiver=new ChatReceiver();
        }
        registerReceiver(chatReceiver,new IntentFilter("com.earthgee.chat_text"));
    }

    @Override
    protected void onStop() {
        unregisterReceiver(chatReceiver);
        super.onStop();
    }

    private void hideKeyBoard(){
        InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction()== MotionEvent.ACTION_DOWN){
            View v =getCurrentFocus();
            if(isShouldHideInput(v,ev)){
                hideKeyBoard();
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v,MotionEvent event){
        if(v!=null&&(v instanceof EditText)){
            int[] location=new int[2];
            v.getLocationInWindow(location);
            int left=location[0];
            int top=location[1];
            int bottom=top+v.getHeight();
            int right=left+v.getWidth();
            if(event.getX()> left&&event.getX()<right&&
                    event.getY()>top&&event.getY()<bottom){
                return false;
            }else{
                return true;
            }
        }
        return true;
    }

}
