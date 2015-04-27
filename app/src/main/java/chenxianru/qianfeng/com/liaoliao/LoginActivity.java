package chenxianru.qianfeng.com.liaoliao;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity implements ServiceConnection {
    /**
     * 从Service中取得
     */
    private ChatService.ChatController controller;
    private EditText txtUserName, txtUserPassWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = new Intent(this, ChatService.class);
        startService(intent);

        //启动之后，再绑定一下
        bindService(intent, this, BIND_AUTO_CREATE);

        txtUserName = (EditText) findViewById(R.id.login_userName);
        txtUserPassWorld = (EditText) findViewById(R.id.login_passWord);
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        //
        controller = (ChatService.ChatController) service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        controller = null;
    }

    @Override
    protected void onDestroy() {
        unbindService(this);
        super.onDestroy();
    }

    public void btnLoginOnClick(View v) {
        Log.d("------------>btnLoginOnClick",controller+"");

        if (controller != null) {
            String userName = txtUserName.getText().toString();
            String passWord = txtUserPassWorld.getText().toString();

            String userJID = controller.login(userName, passWord);
            if (userJID != null) {
                //TODO 登录成功

                Intent intent = new Intent(this, MainActivity.class);

                intent.putExtra("userJID",userJID);

                startActivity(intent);

                finish();
            } else {
                //TODO 提示错误
                Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
