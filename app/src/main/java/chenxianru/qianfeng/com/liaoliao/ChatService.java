package chenxianru.qianfeng.com.liaoliao;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ChatService extends Service {

    //1.创建XMPP连接 HttpURLConnection
    //              XMPPTCPConnection.
    private static XMPPTCPConnection conn;
    private String[] name = {"aa", "aaaaaa", "budaowen", "carry", "jiangxin619"
            , "kk", "kst", "leven"};

    public class ChatController extends Binder {
        /**
         * 停止监听器，不在接收消息，（对于外部界面而言）
         *
         * @param listener
         */
        public void removePacketListener(PacketListener listener) {
            if (listener != null) {
                if (conn != null) {
                    conn.removePacketListener(listener);
                }
            }

        }

        /**
         * 添加监听器接口（外部界面接收消息的时候，设置）
         *
         * @param listener
         */
        public void addPacketListener(PacketListener listener) {
            if (listener != null) {
                if (conn != null) {
                    conn.addPacketListener(listener, new MessageTypeFilter(Message.Type.chat));
                }
            }
        }


        /**
         * 用于开启聊天会话，主要在ChatActivity上使用，用于发送和接收消息，
         *
         * @param target   需要和谁聊天
         * @param thread
         * @param listener MessageListener 用来监听消息  @return 返回Chat 对象，可以通过Chat 调用SendMessage发送消息
         */
        public Chat openChat(String target, String thread, MessageListener listener) {

            Chat ret = null;

            if (target != null) {
                if (conn != null) {
                    if (conn.isAuthenticated()) {
                        //已经登录的情况
                        ChatManager chatManager = ChatManager.getInstanceFor(conn);
                        //创建聊天会话
                        ret = chatManager.createChat(target, null, listener);
                    }
                }
            }


            return ret;
        }


        /**
         * 给外部的LoginActivity 提供直接调用登录的功能
         *
         * @param userName
         * @param passWord
         * @return
         */
        public String login(String userName, String passWord) {
            String ret = null;
            if (userName != null && passWord != null) {
                if (conn != null) {
                    try {
                        if (!conn.isAuthenticated()) {
                            //登录
                            conn.login(userName, passWord);
                        }
                        ret = conn.getUser();
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    } catch (SmackException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return ret;
        }

        //获取当前用户登录账户中的联系人信息
        public List<RosterEntry> getRosterEntries() {
            List<RosterEntry> ret = null;

            if (conn != null) {
                //如果当前已经登录过，那么获取
                if (conn.isAuthenticated()) {
                    Roster roster = conn.getRoster();
                    if (roster != null) {
                        //获取联系人列表
                        Collection<RosterEntry> entries = roster.getEntries();
                        ret = new LinkedList<RosterEntry>();
                        //集合添加集合
                        ret.addAll(entries);
                    }
                }

            }
            return ret;
        }
    }

    //TODO  onbound可能在onStart前执行，
    @Override
    public void onCreate() {
        super.onCreate();
     /*   if (conn!=null){
            try {
                conn.disconnect();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
            conn=null;
        }*/


        //TODO 连接服务器
        // conn = new XMPPTCPConnection("10.0.154.16");
        conn = new XMPPTCPConnection("10.0.154.115");
    }

    public ChatService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.


        return new ChatController();
    }

    private ChatThread thread;

    /**
     * 服务的启动
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO 启动线程
        if (thread == null) {
            thread = new ChatThread();
            thread.start();

        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (thread != null) {
            thread.stopThread();
            thread = null;
        }

        super.onDestroy();
    }

    /**
     * 实际的线程聊天部分
     */
    class ChatThread extends Thread {


        //TODO 标识线程
        private boolean running;

        //TODO 停止线程；！！！
        public void stopThread() {
            running = false;
        }

        @Override
        public void run() {
            running = true;

            //TODO 进行实际的连接服务器操作
            try {
                //TODO SMark API 当中，大部分方法发生错误的时候，直接抛异常，！不报错
                conn.connect();


                //TODO 进行注册操作
                //获取账号管理器，进行注册的操作
                AccountManager accountManager = AccountManager.getInstance(conn);
                //注册账户
                try {
                  accountManager.createAccount("cxr", "19920405");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //TODO 登录账户，上一个注册成功的内容

                //登录都是在连接以后打开，因此登录方法 在XMPPTCPConnection 中
                // 用户名，密码.
                conn.login("cxr", "19920405");

                //获取用户最基本信息，（检查是否登录成功）
                //返回已经登录的用户 JID（也就是，发送消息的时候，收信人的格式）
                String user = conn.getUser();
                //TODO 打印用户信息。
                Log.d("--------->ChatThread", "Login user:" + user);



                //TODO 获取联系人列表
                //getRoster()会自动从服务器获取当前登录用户的联系人列表
                // 并返回的Roster对象。
                //所要的添加修改操作，都会影响到账户实际的联系人内容。
/*                Roster roster = conn.getRoster();
                //获取联系人个数
                int entryCount = roster.getEntryCount();

                //获取所要联系人信息
                Collection<RosterEntry> entries = roster.getEntries();
                //TODO 遍历每一个联系人信息。
                for (RosterEntry entry : entries) {
                    //昵称
                    String name = entry.getName();
                    //收发信息时候用到的内容
                    String user1 = entry.getUser();
                    //获取状态
                    RosterPacket.ItemStatus status = entry.getStatus();
                    //获取分组
                   // Collection<RosterGroup> groups = entry.getGroups();
                    Log.d("------->ChatThread", "Roster:" + user1);
                    Log.d("------->ChatThread", "Roster:" + name);

                }
                //   Log.d("","");
                //第一个参数是JID形式的，也就是 用户名@域名r
                // 第二个参数，添加联系人是 备注名称
                // 第三个参数 属于哪个组。
                for (int i = 0; i < name.length; i++) {
                    roster.createEntry(name[i]+"@10.0.154.195", name[i], null);
                }*/
                //TODO 接收消息
                // 向连接中添加数据包监听器，当服务器给客服端转发消息的时候
                // XMPPTCPConnection 会自动调用PacketListener的回调。

                //两个参数：第一个：数据包监听器，用于处理数据
                //        第二个：监听器要监听哪些类型的数据
                //   因为conn 内部都是数据包，例如 获取联系人，其实也是数据包的形式。
                PacketListener packetListener = new PacketListener() {
                    @Override
                    public void processPacket(Packet packet) throws SmackException.NotConnectedException {
                        //TODO 处理消息类型的数据包
                        //  因为Message 类型继承Packet所以要检查是否是Message。

                        if (packet instanceof Message) {
                            Message msg = (Message) packet;
                            //消息内容
                            String body = msg.getBody();
                            //会话主题
                            String subject = msg.getSubject();
                            //发给谁
                            String to = msg.getTo();
                            //从谁那里来
                            String from = msg.getFrom();
//聊天会话的主题，通过这个主题，就可以确定发送者创建的Chat对象了，
// 这个thread类似于，对讲机之间的联系。
                            String thread = msg.getThread();

                            Log.d("--------->processPacket", "Packet from:" + from + "to" + to + "body" + body);


                            //TODO 当收到消息，就模拟一下QQ的通知栏消息
                            NotificationManagerCompat managerCompat =
                                    NotificationManagerCompat.from(ChatService.this);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(ChatService.this);
                            builder.setContentTitle("您有新消息");
                            builder.setContentText(body);
                            Intent chatIntent = new Intent(
                                    getApplicationContext(),
                                    ChatActivity.class);
                            chatIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);

                            //使用userJID 给谁回复
                            chatIntent.putExtra("userJID", from);
                            //主题标识，进行两个账户之间的联系
                            chatIntent.putExtra("thread", thread);
                            //发送的内容
                            chatIntent.putExtra("body", body);


                            PendingIntent pendingIntent = PendingIntent.getActivity(
                                    ChatService.this,
                                    998,
                                    chatIntent,
                                    PendingIntent.FLAG_ONE_SHOT);

                            builder.setContentIntent(pendingIntent);
                            builder.setSmallIcon(R.drawable.ic_launcher);
                            Notification notification = builder.build();
                            //发送通知
                            managerCompat.notify((int) (System.currentTimeMillis()), notification);
                        }
                    }
                };
                //!!!!在开始之前，进行PacketListener的设置。
              conn.addPacketListener(packetListener, new MessageTypeFilter(Message.Type.chat));
                //穿件会话管理器
            /*    ChatManager chatManager = ChatManager.getInstanceFor(conn);
                //创建会话，需要给其他人发消息

                if (entries != null && entries.isEmpty()) {
                  *//*  Iterator<RosterEntry> iterator = entries.iterator();
                    RosterEntry rosterEntry = iterator.next();
                    String jid = rosterEntry.getUser();*//*
                    String jid = "vhly@10.0.154.195";
                    Log.d("-------->ChatThread", "send to:" + jid);


                    //创建聊天会话，有一个Chat的对象，进行会话的管理
                    // 当使用Chat进行发送消息的时候，会自动的通过底层的XMPPTCPCOnnection，
                    Chat chat = chatManager.createChat(jid, new MessageListener() {
                        @Override
                        public void processMessage(Chat chat, Message message) {
                            //TODO 要处理会话过程中的消息数据
                            //消息内容
                            String body = message.getBody();
                            //会话主题
                            String subject = message.getSubject();
                            //发给谁
                            String to = message.getTo();
                            //从谁那里来
                            String from = message.getFrom();

                            Log.d("--------->processPacket", "Message from:" + from + "to" + to + "body" + body);

                        }
                    });
                    //发送消息
                    chat.sendMessage("hello world by cxr from XMPP Client");

                }*/

                //TODO　进行循环，等待消息内容，以及要进行的发送处理。
                while (running) {
                    Thread.sleep(300);
                }
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        //TODO 关闭连接
                        conn.disconnect();
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }
                    conn = null;
                }
            }
        }
    }
}
