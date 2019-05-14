package cn.edu.cuit.chat_room;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.cuit.client.ClientMsg;
import cn.edu.cuit.operation.Add;
import cn.edu.cuit.proto.ProtoMsg;
import cn.edu.cuit.proto.ProtoMsg.Msg;
import cn.edu.cuit.proto.ProtoMsg.MsgType;
import cn.edu.cuit.proto.ProtoMsg.UserInfo;
import cn.edu.cuit.util.SPType;
import cn.edu.cuit.util.SharedPreferenceUtil;

import static cn.edu.cuit.tools.Tools.dateNow;

public class AddFriendActivity extends AppCompatActivity {
    UserAdapter adapter;
    private List<String> userList = new ArrayList<>();
    private Handler handler;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        handler = new Handler();
        listView = findViewById(R.id.listUser);
        adapter = new UserAdapter(this, R.layout.friend_item, userList);
        InitUserList initUserList = new InitUserList();
        initUserList.start();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String name = userList.get(position);

            ClientMsg clientMsg = new ClientMsg();
            ProtoMsg.Msg.Builder m = Msg.newBuilder();
            UserInfo.Builder info = UserInfo.newBuilder();
            SharedPreferenceUtil spu = new SharedPreferenceUtil(this);
            UserInfo userInfo = info.setName(spu.getString(SPType.userName, "")).build();
            Msg msg = m.setType(MsgType.ADD).setSendTime(dateNow()).setUserInfo(userInfo).setTargetName(name).build();
            clientMsg.setMsg(msg);

            AddThread addThread = new AddThread();
            addThread.start();
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
        Msg msg = m.setType(MsgType.USERS).setSendTime(dateNow()).build();
        clientMsg.setMsg(msg);
    }

    public class InitUserList extends Thread {
        @Override
        public void run() {
            while (true) {
                Msg msg = Add.getUsers();
                if (msg != null) {
                    for (Map.Entry<Integer, String> entry : msg.getFriendsMap().entrySet()) {
                        userList.add(entry.getValue());
                    }
                    Add.setUsers(null);
                    break;
                }
            }
            handler.post(() -> listView.setAdapter(adapter));
        }
    }

    public class AddThread extends Thread {
        @Override
        public void run() {
            if (Add.getUser() != null) {
                handler.post(() -> {
                    String msg;
                    if (Add.getUser().getTargetName().equals("")) {
                        msg = "添加失败";
                    } else {
                        msg = "添加成功";
                    }
                    Add.setUser(null);
                    Toast.makeText(AddFriendActivity.this, msg, Toast.LENGTH_SHORT).show();
                });
            }
        }
    }
}
