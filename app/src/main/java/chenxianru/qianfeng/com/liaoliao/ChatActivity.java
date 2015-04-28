package chenxianru.qianfeng.com.liaoliao;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import java.util.ArrayList;

import chenxianru.qianfeng.com.liaoliao.adapter.ChatMessageAdapter;
import chenxianru.qianfeng.com.liaoliao.model.ChatMessage;

/**
 * 聊天界面，从其他Activity传递参数，userJID ,代表需要聊天的内容
 */
public class ChatActivity extends ActionBarActivity implements ServiceConnection, MessageListener, PacketListener {
    private String userJID;
    /**
     * 从服务获取的Binder，用于进行消息的发送
     */
    private ChatService.ChatController controller;
    private Chat chat;
    private EditText txtContent;
    private String thread;
    private String body;
    private ListView listView;
    private ArrayList<ChatMessage> chatMessages;
    private ChatMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //接收联系人
        Intent intent = getIntent();
        userJID = intent.getStringExtra("userJID");
        //在Activity中显示标题。
        setTitle(userJID);
        //获取Chat主题，可能为空，因为自己点击进入ChatActivity时，是没有的
        thread = intent.getStringExtra("thread");
        //只有收消息的饿时候才有，
        body = intent.getStringExtra("body");


        //绑定服务。用于发送消息
        Intent service = new Intent(this, ChatService.class);
        //绑定服务
        // 参数一：Intent代表服务
        // 参数二：服务绑定的回调接口
        // 参数三：。
        bindService(service, this, BIND_AUTO_CREATE);

        txtContent = (EditText) findViewById(R.id.chat_message_chat);
        //实现类似聊天的样式，左侧是收到消息，右侧是收到消息
        listView = (ListView) findViewById(R.id.chat_message_list);


        chatMessages = new ArrayList<>();
        adapter = new ChatMessageAdapter(this, chatMessages);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        unbindService(this);
        super.onDestroy();
    }
////////////////////////////////////////////////

    //点击事件代码部分

    /**
     * 表情图片点击事件处理
     *
     * @param view
     */
    public void btnFaceOnClick(View view) {
        //TODO 处理表情点击
        //1.获取输入框现有的文本内容
        Editable text = txtContent.getText();

        //2. 准备一个字符串：这个字符串类型，不再是String
        //  而是SpannableString。
        SpannableStringBuilder sb = new SpannableStringBuilder(text);

        int id = view.getId();
        //TODO  添加笑脸了
        //1. 表情的文本显示（例如[偷笑]：）

        //获取即将添加的字符串的起始位置
        int start = sb.length();
        switch (id) {
            case R.id.chat_image1:

                sb.append("[微笑]");
                ImageSpan face1=new ImageSpan(this,R.drawable.face1);


                //第一个参数：what就是各种Span对象，也就是需要给字符串设置的样式
                // 第二个参数：设置字符串的起始位置，例如“I Love Android”如果给Love 设置样式那么起始位2
                // 第三个参数：通常可以指定为起始位置+ 需要设置样式的字符长度 因为第四个参数直接影响到这个字的设置
                // 第四个参数：代表第二个参数和迪桑参数的使用方式
                // 通常第四个参数 采用SPAN_INCLUSIVE_EXCLUSIVE 包含起始位置，但不包含结束位置
                // 最终的范围是从起始位置到结束位置-1。
                sb.setSpan(face1,start,start+4,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                break;
            case R.id.chat_image2:
                sb.append("[撇嘴]");
                ImageSpan face2=new ImageSpan(this,R.drawable.face2);
                //第一个参数：what就是各种Span对象，也就是需要给字符串设置的样式
                // 第二个参数：设置字符串的起始位置，例如“I Love Android”如果给Love 设置样式那么起始位2
                // 第三个参数：通常可以指定为起始位置+ 需要设置样式的字符长度 因为第四个参数直接影响到这个字的设置
                // 第四个参数：代表第二个参数和迪桑参数的使用方式
                // 通常第四个参数 采用SPAN_INCLUSIVE_EXCLUSIVE 包含起始位置，但不包含结束位置
                // 最终的范围是从起始位置到结束位置-1。
                sb.setSpan(face2,start,start+4,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                break;
            case R.id.chat_image3:
                sb.append("[色色]");
                ImageSpan face3=new ImageSpan(this,R.drawable.face3);
                //第一个参数：what就是各种Span对象，也就是需要给字符串设置的样式
                // 第二个参数：设置字符串的起始位置，例如“I Love Android”如果给Love 设置样式那么起始位2
                // 第三个参数：通常可以指定为起始位置+ 需要设置样式的字符长度 因为第四个参数直接影响到这个字的设置
                // 第四个参数：代表第二个参数和迪桑参数的使用方式
                // 通常第四个参数 采用SPAN_INCLUSIVE_EXCLUSIVE 包含起始位置，但不包含结束位置
                // 最终的范围是从起始位置到结束位置-1。
                sb.setSpan(face3,start,start+4,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                break;
            case R.id.chat_image4:
                sb.append("[擦汗]");
                ImageSpan face4=new ImageSpan(this,R.drawable.face4);
                //第一个参数：what就是各种Span对象，也就是需要给字符串设置的样式
                // 第二个参数：设置字符串的起始位置，例如“I Love Android”如果给Love 设置样式那么起始位2
                // 第三个参数：通常可以指定为起始位置+ 需要设置样式的字符长度 因为第四个参数直接影响到这个字的设置
                // 第四个参数：代表第二个参数和迪桑参数的使用方式
                // 通常第四个参数 采用SPAN_INCLUSIVE_EXCLUSIVE 包含起始位置，但不包含结束位置
                // 最终的范围是从起始位置到结束位置-1。
                sb.setSpan(face4,start,start+4,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                break;
        }
        txtContent.setText(sb);
        //将输入框中的光标移动到文字最后。
        txtContent.setSelection(sb.length());
    }


    /**
     * 发送按钮的点击事件
     *
     * @param view
     */
    public void btnSendOnClick(View view) {

        String content = txtContent.getText().toString();
        if (chat != null) {
            try {
                //  for (int i = 0; i < 20; i++) {
                chat.sendMessage(content);
//TODO 创建消息实体，显示在listView上面
                ChatMessage msg = new ChatMessage();
                //设置显示的文本
                msg.setBody(content);
                //类型是发送的类型
                msg.setSourceType(ChatMessage.SOURCE_TYPE_SEND);

                chatMessages.add(msg);
                adapter.notifyDataSetChanged();

                //  }
                txtContent.setText("");
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }

    }

    ////////////////////////////////////////////////
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        controller = (ChatService.ChatController) service;
        //TODO 1.绑定成功后，进行聊天会话的创建 Chat对象
        chat = controller.openChat(userJID, thread, this);

        //TODO 2.Controller向内部XMPPTCPConnection添加一个PacketListener
        controller.addPacketListener(this);


    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        //TODO 删除/停止监听数据包的内容

        controller.removePacketListener(this);
        if (chat != null) {
            chat.close();
        }
        controller = null;
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        //TODO 处理消息的发送和接收
        String from = message.getFrom();
        String to = message.getTo();
        String body = message.getBody();
        Log.d("--------->ChatActivity", "message from" + from + ":" + to + ":" + body);
Log.d("--------->",from);
    }

    /**
     * 接收消息，显示在listView上面
     *
     * @param packet
     * @throws SmackException.NotConnectedException
     */
    @Override
    public void processPacket(Packet packet) throws SmackException.NotConnectedException {
        if (packet instanceof Message) {
            Message msg = (Message) packet;

            String from = msg.getFrom();
            //因为listView会接收所有的消息，所以要检查消息的来源
            // 是否是当前的聊天人。
            if (from.startsWith(userJID)) {
                //TODO 显示listView信息
                ChatMessage chatMessage = new ChatMessage();

                chatMessage.setBody(msg.getBody());

                chatMessage.setFrom(from);

                chatMessage.setTo(msg.getTo());

                chatMessage.setSourceType(ChatMessage.SOURCE_TYPE_RECEIVED);

                chatMessage.setTime(System.currentTimeMillis());
                //添加消息，更新ListView adapter
                chatMessages.add(chatMessage);

                //TODO processPacket执行在子线程中，
                // listView更新在主线程更新。
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();

                    }
                });
            }
        }
    }
    /////////////////////////////////

}
