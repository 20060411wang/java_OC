package businessLayer;


import Thread_.Thread_;
import data.User;
import threadCollection.ThreadManagementCollection;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author wl😹
 * @Version 1.0
 * @ClassName connect
 * @Date 2023/8/3
 * 此类发送用户信息判断是否登录
 */

// Suppress prompts
//@SuppressWarnings("all")

public class Connect {
    private static Socket socket = null;
    private static String ip = "169.254.251.86";

    public static void setIP() {
        System.out.println();
        System.out.print("请输入你要发送的主机ip：");
        ip = new Scanner(System.in).nextLine();
    }

    public static boolean registerUser(User u) {
        try {
            socket = new Socket(ip, 9999);
            // 发送数据到服务端
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);

            // 返回结果
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            if ((boolean) ois.readObject()) {
                Thread_ thread = new Thread_(socket, u);
                new Thread(thread, u.getID()).start(); // 创建线程并启动
                ThreadManagementCollection.add(u.getID(), thread); // 把socket
                System.out.println(u.getID() + "用户注册成功");
                return true;
            } else {
                System.out.println("用户注册失败 用户已存在");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean judgement(User u) {
        try {
            socket = new Socket(ip, 9999);
            // 发送数据到服务端
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);

            // 返回结果
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            if ((boolean) ois.readObject()) {
                Thread_ thread = new Thread_(socket, u);
                new Thread(thread, u.getID()).start(); // 创建线程并启动
                ThreadManagementCollection.add(u.getID(), thread); // 把socket
                return true;
            } else {
                System.out.println("登陆失败");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
