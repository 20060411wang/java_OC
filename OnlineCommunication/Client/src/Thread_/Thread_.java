package Thread_;

import businessLayer.AcceptFileData;
import data.*;

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
    private Socket socket;
    private User u;

    /**
     * 循环接受
     * 服务端返回的信息
     */
    @Override
    public void run() {
        while (true) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(socket.getInputStream());

                Data data = (Data) ois.readObject(); // 接收客户端返回信息

                // 判断客户端返回的信息
                if (data.getType().equals(Type.INFORMATION_TYPE)) {
                    Packet packet = data.getPacket();
                    if (packet.getDT().equals(DataType.REQUEST_ONLINE_LIST))  // 在线用户列表信息
                        // 打印在线用户列表信息
                        System.out.println("\n日期：" + packet.getDate() + "\n" + packet.getContent());
                    else if (packet.getDT().equals(DataType.PRIVATE_CHAT))  // 服务端返回私信数据包
                        System.out.println("\n日期：" + packet.getDate() + "\n" + packet.getSender() + packet.getContent());
                    else if (packet.getDT().equals(DataType.GROUP_CHAT)) { // 接受服务端返回的群聊数据包
                        String[] date = packet.getDate().split("？");
                        System.out.println(date[1]);
                        System.out.println(date[0] + packet.getContent());
                    } else if (packet.getDT().equals(DataType.SENDS_EXIT)) { // 客户端退出系统
                        System.out.println("退出......");
                        System.exit(0);
                    }
                } else if (data.getType().equals(Type.FILE_TYPE)) {
                    FileData fileData = data.getFd();
                    if (fileData.getDT().equals(DataType.FILE_DATE)) // 接受服务段返回的数据
                        AcceptFileData.OutFileData(fileData);

                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Thread_(Socket socket, User u) {
        this.socket = socket;
        this.u = u;
    }

    public Socket getSocket() {
        return socket;
    }

    public User getU() {
        return u;
    }
}
