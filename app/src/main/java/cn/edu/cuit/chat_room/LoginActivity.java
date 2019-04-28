package cn.edu.cuit.chat_room;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.cuit.client.ClientRun;
import cn.edu.cuit.operation.Login;
import cn.edu.cuit.proto.ProtoMsg;
import cn.edu.cuit.util.SPType;
import cn.edu.cuit.util.SharedPreferenceUtil;
import cn.edu.cuit.util.SharedPreferenceUtil.ContentValue;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private EditText userName;
    private EditText password;
    private CheckBox passwordCheck;
    private CheckBox autoLoginCheck;
    private Button loginButton;
    private Button logonButton;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.UserNameEditText);
        password = findViewById(R.id.PasswordEditText);
        passwordCheck = findViewById(R.id.PasswordCheckBox);
        autoLoginCheck = findViewById(R.id.LoginCheckBox);
        loginButton = findViewById(R.id.LoginButton);
        logonButton = findViewById(R.id.LogonButton);

        ConnectionThread ct = new ConnectionThread();
        ct.start();
        init();
        pd = new ProgressDialog(LoginActivity.this);

        loginButton.setOnClickListener(this);
        logonButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LoginButton:
                login();
                break;
            case R.id.LogonButton:

                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView==passwordCheck){
            if (!isChecked){
                autoLoginCheck.setChecked(false);
            }
        }else if(buttonView==autoLoginCheck){
            if(isChecked){
                passwordCheck.setChecked(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(pd!=null){
            if (pd.isShowing()){
                pd.cancel();
            }else {
                finish();
            }
        }else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (pd != null) {
            pd.cancel();
            pd = null;
        }
        super.onDestroy();
    }

    private class ConnectionThread extends Thread{
        @Override
        public void run() {
            ClientRun.Run();
        }
    }

    private void init(){
        //判断用户是否是第一次登陆
        if (isFirstLogin()){
            //取消选中记住密码框
            passwordCheck.setChecked(false);
            //取消选中记住自动登录框
            autoLoginCheck.setChecked(false);
        }

        //判断是否记住密码
        if (isRememberPassword()){
            //勾选记住密码框
            passwordCheck.setChecked(true);
            //填入记住的密码账号
            setUserNameAndPassword();
        }

        //判断是否自动登录
        if (isAutoLogin()){
            //选中自动登录框
            autoLoginCheck.setChecked(true);
            //登录
            login();
        }
    }

    //是否第一次登陆
    private boolean isFirstLogin() {
        SharedPreferenceUtil spu = new SharedPreferenceUtil(this);
        boolean first = spu.getBoolean(SPType.isFirstLogin, true);
        if (first) {
            spu.put(new ContentValue(SPType.isFirstLogin, false),
                    new ContentValue(SPType.isRememberPassword, false),
                    new ContentValue(SPType.isAutoLogin, false),
                    new ContentValue(SPType.userName, ""),
                    new ContentValue(SPType.password, ""));

            return true;
        }
        return false;
    }

    //是否记住密码
    private boolean isRememberPassword(){
        SharedPreferenceUtil spu = new SharedPreferenceUtil(this);
        return spu.getBoolean(SPType.isRememberPassword,false);
    }

    //将保存的在本地的密码用户名填入登录框
    private void setUserNameAndPassword(){
        userName.setText(getLocalUserName());
        password.setText(getLocalPassword());
    }

    //是否自动登录
    private boolean isAutoLogin(){
        SharedPreferenceUtil spu = new SharedPreferenceUtil(this);
        return spu.getBoolean(SPType.isAutoLogin,false);
    }

    //登录
    private void login() {
        if (getUserName().isEmpty()) {
            showToast("用户名为空!");
            return;
        }

        if (getPassword().isEmpty()) {
            showToast("密码为空!");
            return;
        }

        Login.sendUserInfo(getUserName(), getPassword());
        showLoading("登录中...");

        Thread loginThread = new Thread() {
            @Override
            public void run() {
                super.run();
                setLoginButtonClickable(false);

                if (isAllowLogin(getUserName(), getPassword())) {
                    hideLoading();
                    showToast("登陆成功!");
                    saveLoginValueToLocal();
                } else {
                    hideLoading();
                    showToast("账号或密码不正确!");
                }
                setLoginButtonClickable(true);
            }
        };
        loginThread.start();
    }

    //是否允许登录
    private boolean isAllowLogin(String userName,String password){
        ProtoMsg.UserInfo userInfo = Login.receiveUserInfo;
        return userInfo != null && (userName.equals(userInfo.getName()) && userInfo.getPassword().equals(password));
    }

    //设置登录按钮是否可用
    private void setLoginButtonClickable(boolean flag){
        loginButton.setClickable(flag);
    }

    //显示toast
    private void showToast(String msg){
        runOnUiThread(() -> Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show());
    }

    //显示登录状态
    private void showLoading(String msg){
        pd.setMessage(msg);
        pd.setCancelable(false);
        pd.show();
    }

    //隐藏登陆状态
    private void hideLoading(){
        pd.dismiss();
    }

    //获取输入框用户名
    private String getUserName(){
        return userName.getText().toString().trim();
    }

    //获取输入框密码
    private String getPassword(){
        return password.getText().toString().trim();
    }

    //获取本地保存用户名
    private String getLocalUserName(){
        SharedPreferenceUtil spu = new SharedPreferenceUtil(this);
        return spu.getString(SPType.userName,"");
    }

    //获取本地保存密码
    private String getLocalPassword(){
        SharedPreferenceUtil spu = new SharedPreferenceUtil(this);
        return spu.getString(SPType.password,"");
    }

    private void saveLoginValueToLocal(){
        SharedPreferenceUtil spu =new SharedPreferenceUtil(this);
        if(autoLoginCheck.isChecked()) {
            spu.put(new ContentValue(SPType.userName, getUserName()),
                    new ContentValue(SPType.password, getPassword()),
                    new ContentValue(SPType.isAutoLogin, true),
                    new ContentValue(SPType.isRememberPassword, true));
        }else if(passwordCheck.isChecked()){
            spu.put(new ContentValue(SPType.userName, getUserName()),
                    new ContentValue(SPType.password, getPassword()),
                    new ContentValue(SPType.isAutoLogin, false),
                    new ContentValue(SPType.isRememberPassword, true));
        }else if(!passwordCheck.isChecked()){
            spu.put(new ContentValue(SPType.userName, getUserName()),
                    new ContentValue(SPType.password, ""),
                    new ContentValue(SPType.isAutoLogin, false),
                    new ContentValue(SPType.isRememberPassword, false));
        }
    }
}
