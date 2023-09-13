package thread＿;

import business.BusinessMethod;
import data.DataType;
import data.User;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author wl😹
 * @Version 1.0
 * @ClassName ReceiveＴhread
 * @Date 2023/8/30
 * 循环收客户端发送的信息
 */

// Suppress prompts
//@SuppressWarnings("all")

public class ReceiveThread implements Runnable {
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("服务端在9999端口监听中——————");
            Socket socket = null;
            while (true) {
                System.out.println("等待客户端连接");
                socket = serverSocket.accept(); // 创建连接

                // 接收用户信息
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User) ois.readObject();

                if (user.getDT().equals(DataType.LOG_ON_USER)) { // 用户登录
                    BusinessMethod.logOnUser(user, socket);
                } else if (user.getDT().equals(DataType.REGISTER_USER)) { // 用户注册
                    BusinessMethod.registerUser(user, socket);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BusinessMethod bm = new BusinessMethod();
}
