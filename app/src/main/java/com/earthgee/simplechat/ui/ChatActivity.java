package com.earthgee.simplechat.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.earthgee.simplechat.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView= (RecyclerView) findViewById(R.id.list);
        inputText= (EditText) findViewById(R.id.input);
        sendBtn= (Button) findViewById(R.id.send);

        initRecyclerView();
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
