package data.domain;

/**
 * @Author wl😹
 * @ClassName OfflineFileInformation
 * @Date 2023/9/11
 * 此类对应数据库中的offlineFileInformation表
 */

// Suppress prompts
//@SuppressWarnings("all")

public class OfflineFileInformation {
    private String sender;
    private String acceptor;
    private byte[] file;
    private String fileName;
    private String DT;
    private String Date;

    public OfflineFileInformation() {
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

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getDT() {
        return DT;
    }

    public void setDT(String DT) {
        this.DT = DT;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
