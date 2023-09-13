package thread＿;

import business.InformationFlow;
import data.*;
import threadCollection.ThreadManagementCollection;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @Author wl😹
 * @Version 1.0
 * @ClassName Thread_
 * @Date 2023/8/31
 */

// Suppress prompts
//@SuppressWarnings("all")

public class Thread_ implements Runnable {
    private User u;
    private Socket socket;
    private boolean flag = true;
    private InformationFlow if_ = null;

    /**
     * 循环接收客户端信息
     * 并做处理
     */
    @Override
    public void run() {
        socket = ThreadManagementCollection.getSocket(u.getID());
        ObjectInputStream ois = null;
        if_ = new InformationFlow(u);
        while (flag) {
            try {

                ois = new ObjectInputStream(socket.getInputStream());
                // 接收客户端数据包
                Data data = (Data) ois.readObject();
                // 判断数据包类型
                if (data.getType().equals(Type.INFORMATION_TYPE)) { // 信息类型
                    Packet packet = data.getPacket();
                    if (packet.getDT().equals(DataType.REQUEST_ONLINE_LIST)) { // 客户端请求在线用户列表
                        if_.onlineUserInformation(packet.getSender());
                    } else if (packet.getDT().equals(DataType.PRIVATE_CHAT)) { // 接收客户端的私信数据包
                        if_.forwardingSendsMessages(data);
                    } else if (packet.getDT().equals(DataType.CREATE_GROUP)) { // 创建群聊
                        if_.createGroup(packet);
                    } else if (packet.getDT().equals(DataType.JOIN_GROUP)) { // 用户加入群聊
                        if_.joinGroup(packet);
                    } else if (packet.getDT().equals(DataType.TALK_ABOUT_INFORMATION)) { // 用户拉取已加入的群聊
                        if_.talkAboutInformation(packet);
                    } else if (packet.getDT().equals(DataType.GROUP_CHAT)) { // 发送群聊信息
                        if_.sendsGroupChatJudging(packet);
                    } else if (packet.getDT().equals(DataType.SENDS_EXIT)) { // 客户端请求推出系统
                        if_.clientExit(packet.getSender());
                    }
                } else if (data.getType().equals(Type.FILE_TYPE)) {
                    FileData fileData = data.getFd();
                    if (fileData.getDT().equals(DataType.FILE_DATE)) { // 处理用户的文件信息
                        if_.sendsFileData(data);
                    }
                }

            } catch (Exception e) {
                if_.clientExit1(u.getID());
//                throw new RuntimeException(e);
            }
        }
    }

    public Thread_(User u, Socket socket) {
        this.u = u;
        this.socket = socket;
    }

    public User getU() {
        return u;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
