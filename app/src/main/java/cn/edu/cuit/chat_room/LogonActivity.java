package cn.edu.cuit.chat_room;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.cuit.operation.Logon;
import cn.edu.cuit.proto.ProtoMsg.Msg;

import static cn.edu.cuit.operation.Logon.goToLogon;
import static cn.edu.cuit.operation.Logon.isRightForPassword;
import static java.lang.System.currentTimeMillis;

public class LogonActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userName;
    private EditText phone;
    private EditText email;
    private EditText password1;
    private EditText password2;
    private Button login;
    private Button logon;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        userName=findViewById(R.id.NameEditText);
        phone=findViewById(R.id.PhoneEditText);
        email=findViewById(R.id.EmailEditText);
        password1=findViewById(R.id.PasswordEditText1);
        password2=findViewById(R.id.PasswordEditText2);
        login=findViewById(R.id.Login);
        logon=findViewById(R.id.Logon);
        login.setOnClickListener(this);
        logon.setOnClickListener(this);
        progressDialog = new ProgressDialog(LogonActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Login:
                startActivity(new Intent(LogonActivity.this,LoginActivity.class));
                break;
            case R.id.Logon:
                startLogon(
                        password1.getText().toString().trim(),
                        password2.getText().toString().trim()
                );
                break;
        }
    }

    private void startLogon(String psd1, String psd2){
        if(isRightForPassword(psd1,psd2)){
            goToLogon(
                    userName.getText().toString().trim(),
                    phone.getText().toString().trim(),
                    email.getText().toString().trim(),
                    password1.getText().toString().trim()
            );
            progressDialog.setMessage("注册中...");
            progressDialog.show();

            Thread getLogonMsg = new Thread(){
                @Override
                public void run() {
                    switch (getLogonState(userName.getText().toString().trim(),psd1)){
                        case SUCCESS:
                            showToast("注册成功!");
                            break;
                        case FAILURE:
                            showToast("注册失败！");
                            break;
                        case EXIT:
                            showToast("用户名已存在！");
                    }
                    progressDialog.dismiss();
                }
            };
            getLogonMsg.start();
        }else {
            showToast("两次输入的密码不正确！");
        }
    }

    private logonType getLogonState(String name, String password) {
        Msg msg;

        long startTime = currentTimeMillis();
        long tempTime = currentTimeMillis() - startTime;
        while (true) {
            msg = Logon.getReceiveLogonMsg();

            if (tempTime > 5000) {
                if (msg == null) {
                    return logonType.FAILURE;
                } else if (msg.getUserInfo().getId() == -1) {
                    return logonType.EXIT;
                }
            } else if (msg != null) {
                if (name.equals(msg.getUserInfo().getName()) && password.equals(msg.getUserInfo().getPassword())) {
                    return logonType.SUCCESS;
                }
            }
            tempTime = currentTimeMillis() - startTime;
        }
    }

    private enum logonType{
        SUCCESS,
        FAILURE,
        EXIT
    }

    //显示toast
    private void showToast(String msg) {
        runOnUiThread(() -> Toast.makeText(LogonActivity.this, msg, Toast.LENGTH_SHORT).show());
    }
}
