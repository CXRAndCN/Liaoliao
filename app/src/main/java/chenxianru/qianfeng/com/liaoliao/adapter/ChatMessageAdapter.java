package chenxianru.qianfeng.com.liaoliao.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chenxianru.qianfeng.com.liaoliao.R;
import chenxianru.qianfeng.com.liaoliao.model.ChatMessage;

/**
 * Created by aaa on 15-4-24.
 */

/**
 * 主要用于聊天信息展现和listView的显示。分为左侧和右侧两部分
 */
public class ChatMessageAdapter extends BaseAdapter {
    //上下文
    private Context context;
    /**
     * 当前聊天信息列表
     */
   private List<ChatMessage>messages;
    private LayoutInflater inflater;

    public ChatMessageAdapter(Context context, List<ChatMessage> messages) {
        this.context = context;
        this.messages = messages;
        inflater=LayoutInflater.from(context);
    }

    /**
     * 获取所有数据调试
      * @return
     */
    @Override
    public int getCount() {
        int ret=0;
        if (messages != null) {
           ret=messages.size();
        }
        return ret;
    }

    /**
     * 获取指定牵引的实际数据对象
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        Object ret=null;
        if (messages != null) {
            ret=messages.get(position);
        }
        return ret;
    }

    /**
     * 获取数据的ID ，对于CursorAdapter这个方法返回的是数据库记录的id
     * 另一个应用就是ListView设置为可以多选的情况下。
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    //

    /**
     * 告诉listView 内部的布局一共有多少种。
     * @return
     */
    @Override
    public int getViewTypeCount() {
        //对于2 主要是聊天主要有发送和接收两种布局
        // 左侧接收的，右侧发送的。
        return 2;

    }

    /**
     * 每次listView先是Item的时候，都先问一下adapter，
     * 指定为指定位置的Item是什么类型
     * @param position  格局位置，获取数据的类型。
     * @return int 注意：返回的数值必须是0 到 getViewCount—1
     */
    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = messages.get(position);
        int ret=0;
        int sourceType = chatMessage.getSourceType();
        //对于发送出去的 消息显示在右侧，指定返回类型为1
        if (sourceType==ChatMessage.SOURCE_TYPE_SEND){
            ret=1;
        }else if (sourceType==ChatMessage.SOURCE_TYPE_RECEIVED){
            //对于收到的 消息显示在右侧，指定返回类型为0
            ret=0;
        }
        return ret;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
        //TODO 1.获取消息，判断消息的类型，根据类型来进行内容的设置
        ChatMessage chatMessage = messages.get(position);

        //获取来源类型，根据来源类型进行不同布局的加载与显示
        int sourceType = chatMessage.getSourceType();

        if (sourceType==ChatMessage.SOURCE_TYPE_RECEIVED){
            //TODO 收到的，就显示在左侧
           if (convertView!=null){
               ret=convertView;
           }else {
               //LayoutInflater
               ret= inflater.inflate(R.layout.item_chat_left,parent,false);

           }
            //TODO 显示消息内容（左侧的）
            TextView txtMessage = (TextView) ret.findViewById(R.id.chat_message_left);
            txtMessage.setText(chatMessage.getBody());

            //聊天消息表情
            //1.找到字符串中所有的【】包含的内容
            new SpannableString(chatMessage.getBody());
            //[偷笑][擦汗]
            //0   34    7

            //正则表达式 如何查找【】包含的内容

            System.currentTimeMillis();

        }else if (sourceType==ChatMessage.SOURCE_TYPE_SEND){
            //TODO 发送的，显示在右侧
            if (convertView != null) {
                ret=convertView;
            }else {
                ret=inflater.inflate(R.layout.item_chat_right,parent,false);
            }
            //TODO 显示消息（右侧的）
            TextView txtMessage = (TextView) ret.findViewById(R.id.chat_message_right);
            txtMessage.setText(chatMessage.getBody());

        }



        return ret;
    }
}
