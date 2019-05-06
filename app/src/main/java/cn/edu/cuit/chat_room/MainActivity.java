package cn.edu.cuit.chat_room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.edu.cuit.client.ClientMsg;
import cn.edu.cuit.operation.Main;
import cn.edu.cuit.proto.ProtoMsg.Msg;
import cn.edu.cuit.proto.ProtoMsg.MsgType;
import cn.edu.cuit.proto.ProtoMsg.UserInfo;
import cn.edu.cuit.util.SPType;
import cn.edu.cuit.util.SharedPreferenceUtil;

import static cn.edu.cuit.tools.Tools.dateNow;

public class MainActivity extends AppCompatActivity{
    private static List<String> friendList = new ArrayList<>();
    private ListView listView;
    private boolean backPress = false;
    private Handler handler;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new UserAdapter(MainActivity.this, R.layout.friend_item, friendList);
        listView = findViewById(R.id.list_friend);
        handler = new Handler();
        InitFriendList initFriendList = new InitFriendList();
        initFriendList.start();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String name = friendList.get(position);
            Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class);
            intent.putExtra("extra_name", name);
            MainActivity.this.startActivity(intent);
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //发送获取所有用户
        ClientMsg clientMsg = new ClientMsg();
        Msg.Builder m = Msg.newBuilder();
        UserInfo.Builder info = UserInfo.newBuilder();
        SharedPreferenceUtil spu = new SharedPreferenceUtil(this);
        UserInfo userInfo = info.setName(spu.getString(SPType.userName, "")).build();
        Msg msg = m.setType(MsgType.USERS).setSendTime(dateNow()).setUserInfo(userInfo).build();
        clientMsg.setMsg(msg);
    }

    @Override
    public void onBackPressed() {
        if (backPress) {
            finish();

            System.exit(0);
        } else {
            Toast.makeText(this, "再按一次退出！", Toast.LENGTH_SHORT).show();
            backPress = true;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    backPress = false;
                }
            }, 2000);
        }
    }

    public class InitFriendList extends Thread{
        @Override
        public void run() {
            while (true){
                Msg msg=Main.getUsers();
                if(msg!=null){
                    for(Map.Entry<Integer,String> entry : msg.getFriendsMap().entrySet()){
                        friendList.add(entry.getValue());
                    }
                    break;
                }
            }
            handler.post(() -> {
                listView.setAdapter(adapter);
            });
        }
    }
}
