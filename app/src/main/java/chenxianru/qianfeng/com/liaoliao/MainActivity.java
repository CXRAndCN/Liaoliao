package chenxianru.qianfeng.com.liaoliao;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jivesoftware.smack.RosterEntry;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements ServiceConnection, AdapterView.OnItemClickListener {

    private TextView txtUserJID;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> data;

    //服务调用接口
    private ChatService.ChatController chatController;
    private List<RosterEntry> rosterEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtUserJID = (TextView) findViewById(R.id.main_user_id);

        Intent intent = getIntent();
        String userJID = intent.getStringExtra("userJID");
        txtUserJID.setText(userJID);

        //联系人列表
        ListView listView = (ListView) findViewById(R.id.main_roster_list);

        data = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                data
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);


        Intent service = new Intent(this, ChatService.class);
        bindService(service, this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateRosterList();
    }

    private void updateRosterList() {
        if (chatController != null) {
            //每次显示的时候，及时获取联系人列表，进行刷新操作
            //获取联系人信息
            rosterEntries = chatController.getRosterEntries();
            data.clear();
            for (RosterEntry entry : rosterEntries) {
                String user = entry.getUser();

                data.add(user);
            }

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        //解除绑定
        unbindService(this);
        super.onDestroy();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        chatController = (ChatService.ChatController) service;
        updateRosterList();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        chatController = null;
    }

    //添加联系人
    public void btnAddUser(View view) {


    }

    /**
     * 点击联系人。启动会话
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO 获取联系人的JID
        //联系人信息
        RosterEntry entry = rosterEntries.get(position);

        //获取联系人账号
        String userJID = entry.getUser();
        //开启聊天会话界面
        Intent intent = new Intent(this, ChatActivity.class);

        intent.putExtra("userJID", userJID);

        startActivity(intent);

    }
}
