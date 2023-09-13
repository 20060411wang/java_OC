package data;

import java.io.Serializable;

/**
 * @Author wl😹
 * @Version 1.0
 * @ClassName Packet
 * @Date 2023/8/31
 */

// Suppress prompts
//@SuppressWarnings("all")

public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sender; // 发送者
    private String acceptor; // 接收者
    private String content; // 内容
    private String date; // 时间
    private String DT; // 数据包类型

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDT() {
        return DT;
    }

    public void setDT(String DT) {
        this.DT = DT;
    }
}
