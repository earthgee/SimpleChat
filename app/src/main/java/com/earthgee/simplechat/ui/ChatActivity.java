package com.earthgee.simplechat.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.earthgee.simplechat.R;
import com.earthgee.simplechat.net.ConnectionManager;
import com.earthgee.simplechat.net.RequestSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by earthgee on 17/2/21.
 */
public class ChatActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private EditText inputText;
    private Button sendBtn;

    private List<String> contents=new ArrayList<>();
    private ChatAdapter mAdapter;

    private String userId;
    private String mUserId;

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
                            (userId,content,ChatActivity.this){

                        @Override
                        protected void onPostExecute(Integer integer) {
                            if(integer==0){
                                //发送成功
                                contents.add(mUserId+":"+content);
                                mAdapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(ChatActivity.this,"发送失败",Toast.LENGTH_SHORT).show();;
                            }
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

    private class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

        @Override
        public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View contentView=LayoutInflater.from(ChatActivity.this).
                    inflate(R.layout.item_chat, parent, false);
            ViewHolder viewHolder=new ViewHolder
                    (contentView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {
            holder.content.setText(contents.get(position));
        }

        @Override
        public int getItemCount() {
            return contents.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            TextView content;

            public ViewHolder(View itemView) {
                super(itemView);
                content= (TextView) itemView.findViewById(R.id.content);
            }
        }

    }


}
