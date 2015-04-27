package chenxianru.qianfeng.com.liaoliao.model;

/**
 * Created by aaa on 15-4-24.
 */

/**
 * 用于描述聊天的消息
 */
public class ChatMessage {
    /**
     * 发出去的
     */
    public static final int SOURCE_TYPE_SEND=0;
    /**
     * 收到的
     */
    public static final int SOURCE_TYPE_RECEIVED=1;
    /**
     * 发消息的人
     */
    private String from;
    /**
     * 消息发给谁
     */
    private String to;
    /**
     * 消息的内容
     */
    private String body;
    /**
     * 接收或者是发送的时间
     */
    private long time;
    /**
     * 消息的来源类型，代表是发出去的还是就收的
     * 可选择：0是发出去
     * 1是收到的
     */
    private int sourceType;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }
}
