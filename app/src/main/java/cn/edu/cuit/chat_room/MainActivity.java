package cn.edu.cuit.chat_room;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private boolean backPress = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        if(backPress){
            finish();

            System.exit(0);
        }else {
            Toast.makeText(this,"再按一次退出！",Toast.LENGTH_SHORT).show();
            backPress=true;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    backPress=false;
                }
            },2000);
        }
    }
}
