package data;

import java.io.Serializable;

/**
 * @Author wl😹
 * @ClassName Data
 * @Date 2023/9/4
 */

// Suppress prompts
//@SuppressWarnings("all")

public class Data implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type; // 数据类型
    private Packet packet; // 信息数据包
    private FileData fd; // 文件数据包

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public FileData getFd() {
        return fd;
    }

    public void setFd(FileData fd) {
        this.fd = fd;
    }
}
