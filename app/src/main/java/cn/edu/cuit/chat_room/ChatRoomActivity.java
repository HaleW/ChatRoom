package cn.edu.cuit.chat_room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.edu.cuit.client.ClientMsg;
import cn.edu.cuit.util.SPType;
import cn.edu.cuit.util.SharedPreferenceUtil;

import static cn.edu.cuit.operation.ChatMsg.getReceiveMsg;
import static cn.edu.cuit.operation.ChatMsg.list;
import static cn.edu.cuit.operation.ChatMsg.setReceiveMsg;
import static cn.edu.cuit.tools.Tools.dateNow;

public class ChatRoomActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText editText;
    private TextView titleText;

    private Button button;
    private MsgAdapter adapter;
    private String friendName;
    public static String name;
    private List<Msg> msgList;
    private Timer timer;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                update();
            }
        }

        void update() {
            if (getReceiveMsg() != null && name.equals(getReceiveMsg().getTargetName())) {
                msgList.add(getReceiveMsg());
            }
            setReceiveMsg(null);
            recyclerView.scrollToPosition(msgList.size() - 1);
        }
    };
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Intent intent = getIntent();
        friendName = intent.getStringExtra("extra_name");
        name = getLocalName();

        msgList = new ArrayList<>();

        timer = new Timer();
        timer.schedule(timerTask, 0, 500);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        titleText = findViewById(R.id.title_text);
        recyclerView = findViewById(R.id.msg_recycler_view);
        editText = findViewById(R.id.input_text);
        button = findViewById(R.id.send_button);

        titleText.setText(friendName);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        for (Msg m : list) {
            if (friendName.equals(m.getTargetName()) || name.equals(m.getTargetName())) {
                msgList.add(m);
            }
        }

        adapter = new MsgAdapter(msgList);
        recyclerView.setAdapter(adapter);

        button.setOnClickListener(v -> {
            String content = editText.getText().toString();
            if (!"".equals(content)) {
                ProtoMsg.Msg.Builder msg = ProtoMsg.Msg.newBuilder();
                ProtoMsg.UserInfo.Builder info = ProtoMsg.UserInfo.newBuilder();
                ProtoMsg.UserInfo userInfo = info.setName(name).build();
                ProtoMsg.Msg m = msg
                        .setContent(content)
                        .setTargetName(friendName)
                        .setSendTime(dateNow())
                        .setUserInfo(userInfo)
                        .setType(ProtoMsg.MsgType.MSG)
                        .build();
                ClientMsg clientMsg = new ClientMsg();
                clientMsg.setMsg(m);

                list.add(m);
                msgList.add(m);
                //当新消息是刷新RecyclerView中的显示
                adapter.notifyItemInserted(msgList.size() - 1);
                //将RecyclerView定位到最后一行
                recyclerView.scrollToPosition(msgList.size() - 1);
                //清空输入框内容
                editText.setText("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private String getLocalName() {
        SharedPreferenceUtil spu = new SharedPreferenceUtil(this);
        return spu.getString(SPType.userName, "");
    }
}
