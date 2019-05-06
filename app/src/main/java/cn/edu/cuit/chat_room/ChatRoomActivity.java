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

import java.util.Timer;
import java.util.TimerTask;

import cn.edu.cuit.client.ClientMsg;
import cn.edu.cuit.proto.ProtoMsg;
import cn.edu.cuit.util.SPType;
import cn.edu.cuit.util.SharedPreferenceUtil;

import static cn.edu.cuit.operation.ChatMsg.list;
import static cn.edu.cuit.tools.Tools.dateNow;

public class ChatRoomActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText editText;
    private Button button;
    private MsgAdapter adapter;
    private String friendName;
    public static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Intent intent =getIntent();
        friendName =intent.getStringExtra("extra_name");
        name = getLocalName();

        timer.schedule(timerTask,0,500);

        ActionBar actionBar =getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        recyclerView = findViewById(R.id.msg_recycler_view);
        editText = findViewById(R.id.input_text);
        button = findViewById(R.id.send_button);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MsgAdapter(list);
        recyclerView.setAdapter(adapter);


        button.setOnClickListener(v -> {
            String content = editText.getText().toString();
            if (!"".equals(content)){
                ProtoMsg.Msg.Builder msg = ProtoMsg.Msg.newBuilder();
                ProtoMsg.UserInfo.Builder info = ProtoMsg.UserInfo.newBuilder();
                ProtoMsg.UserInfo userInfo =info.setName(name).build();
                ProtoMsg.Msg m =msg
                        .setContent(content)
                        .setTargetName(friendName)
                        .setSendTime(dateNow())
                        .setUserInfo(userInfo)
                        .setType(ProtoMsg.MsgType.MSG)
                        .build();
                ClientMsg clientMsg = new ClientMsg();
                clientMsg.setMsg(m);

                list.add(m);
                //当新消息是刷新RecyclerView中的显示
                adapter.notifyItemInserted(list.size() - 1);
                //将RecyclerView定位到最后一行
                recyclerView.scrollToPosition(list.size() - 1);
                //清空输入框内容
                editText.setText("");
            }
        });
    }

    private String getLocalName(){
        SharedPreferenceUtil spu = new SharedPreferenceUtil(this);
        return spu.getString(SPType.userName, "");
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(Message message) {
            if (message.what == 1) {
                update();
            }
        }
      void update(){

          //将RecyclerView定位到最后一行
          recyclerView.scrollToPosition(list.size() - 1);
      }
    };
    Timer timer = new Timer();
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what=1;
            handler.sendMessage(message);
        }
    };
}
