package com.homework.grop.group_homework;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.homework.grop.group_homework.model.Message;
import com.homework.grop.group_homework.model.PullParser;

import java.io.InputStream;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements MessageAdapter.MyItemClickListener{
    private RecyclerView myNumbersListView;
    private MessageAdapter messageAdapter;
    private List<Message> messages;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   setMainScreen();
                    return true;
                case R.id.navigation_dashboard:
                    setContentView(R.layout.activity_personal_page);
                    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                    navigation.getMenu().getItem(2).setChecked(true);
                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                    return true;
                case R.id.navigation_notifications:
                    setMessageScreen();
                    return true;
            }
            return false;
        }
    };




    public void setMainScreen()
    {
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里申请权限

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },0);
                //开启拍照模式
                startActivity(new Intent(MainActivity.this,TakeCamera.class));
            }
        });
    }
    public void setMessageScreen()
    {
        setContentView(R.layout.activity_tips);
        myNumbersListView = findViewById(R.id.rv_list);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(1).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        try {
            InputStream assetInput = getAssets().open("data.xml");
            messages = PullParser.pull2xml(assetInput);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        messageAdapter =new MessageAdapter(messages,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myNumbersListView.setLayoutManager(layoutManager);
        myNumbersListView.setHasFixedSize(true);
        myNumbersListView.setAdapter(messageAdapter);
    }
    @Override
    public void onListItemClick(int position)
    {
        Intent intent=new Intent(this,ChatroomActivity.class);
        intent.putExtra("message",messages.get(position).getTitle());
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainScreen();

    }


    @Override
    public void onStop()
    {super.onStop();}

}
