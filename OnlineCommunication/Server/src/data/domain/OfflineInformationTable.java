package data.domain;

/**
 * @Author wl😹
 * @Version 1.0
 * @ClassName OfflineInformationTable
 * @Date 2023/9/1
 * 此类对应sql中的OfflineInformationTable表
 */

// Suppress prompts
//@SuppressWarnings("all")

public class OfflineInformationTable {
    private String sender;
    private String acceptor;
    private String content;
    private String date;
    private String DT;

    public OfflineInformationTable() {
    }

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
