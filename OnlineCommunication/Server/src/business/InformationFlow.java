package business;

import data.*;
import threadCollection.ThreadManagementCollection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author wl😹
 * @Version 1.0
 * @ClassName informationFlow
 * @Date 2023/8/31
 * 此类用来处理业务信息
 */

// Suppress prompts
//@SuppressWarnings("all")

public class InformationFlow {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
    private User u;
    private BusinessMethod bm = new BusinessMethod();

    public void clientExit(String userId) {
        forwardingSendsMessages(setPacket(null, userId, null, null, DataType.SENDS_EXIT));
        ThreadManagementCollection.deleteUser(userId);
        System.out.println(userId + "用户退出系统...");
    }
    public void clientExit1(String userId) {
        ThreadManagementCollection.deleteUser(userId);
        System.out.println(userId + "用户退出系统...");
    }

    /**
     * 此方法处理用户的文件转发数据
     */
    public void sendsFileData(Data d) {
        FileData fd = d.getFd();
        if (onlineStatus(fd)) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(ThreadManagementCollection.getSocket(fd.getAcceptor()).getOutputStream());
                oos.writeObject(d);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 此方法判断用户是否存在
     * 如果不存在就把文件数据存在数据库中
     * 不存在返回提示信息
     *
     * @param fd
     * @return
     */
    public boolean onlineStatus(FileData fd) {
        HashMap hm = ThreadManagementCollection.getHashMap();
        if (hm.get(fd.getAcceptor()) != null) {
            return true;
        }
        if (bm.doesExistUser(fd.getAcceptor())) {
            bm.addSqlData(fd);
            return false;
        } else {
            forwardingSendsMessages(setPacket("", fd.getSender(), "此用户不存在", fd.getDate(), DataType.PRIVATE_CHAT));
            return false;
        }
    }

    /**
     * 此方法转发客户端发送的群聊信息
     *
     * @param p
     */
    public void sendsGroupChatJudging(Packet p) {
        if (!bm.selectSqlGroup(p.getAcceptor())) {
            String index = bm.selectGroupIndex(p.getAcceptor());
            if (bm.selectGroup(p.getSender(), index))
                bm.senderGroupChat(p, index);
            else
                forwardingSendsMessages(setPacket("", p.getSender(), "发送失败，你为在此群聊中", p.getDate(), DataType.PRIVATE_CHAT));
        } else
            forwardingSendsMessages(setPacket("", p.getSender(), "发送失败，没有找到此群聊", p.getDate(), DataType.PRIVATE_CHAT));

    }

    /**
     * 此方法返回此用户加入的群聊信息
     *
     * @param p
     */
    public void talkAboutInformation(Packet p) {
        forwardingSendsMessages(setPacket("", p.getSender(), bm.selectGroupUser(p.getSender()), p.getDate(), DataType.PRIVATE_CHAT));
    }

    /**
     * 此方法把客户端用户添加到指定 群聊中
     * 先判断群聊是否存在
     * 如果存在就添加到表中
     * 不存在提示信息
     * 在判断此用户是否存在群聊中
     * 存在不添加 并提示信息
     * 不存在添加 返回提示信息
     */
    public void joinGroup(Packet p) {
        String s = null;
        if (!bm.selectSqlGroup(p.getContent())) if (bm.selectUserExist(p.getSender(), p.getContent())) {
            bm.insertIntoUesr(p);
            s = "群聊加入成功";
        } else s = "你已存在群聊中";
        else s = "群聊不存在";
        forwardingSendsMessages(setPacket("", p.getSender(), s, sdf.format(new Date()), DataType.PRIVATE_CHAT));
    }

    /**
     * 此方法创建群聊
     * 先判断用户权限
     * 先判断此群聊是否存在
     * 不存在就创建
     */
    public void createGroup(Packet packet) {
        String s = null;
        if (bm.selectUserJD(packet.getSender()).equals("1")) {
            if (bm.selectSqlGroup(packet.getContent())) {
                bm.createGroup(packet);
                System.out.println(packet.getSender() + "用户创建了" + packet.getContent() + "群聊");
                s = "群聊创建成功";
            } else s = "群聊创建失败";
        } else s = "你的权限不足";
        forwardingSendsMessages(setPacket("", packet.getSender(), s, sdf.format(new Date()), DataType.PRIVATE_CHAT));
    }

    /**
     * 此方法统计在线用户数量
     * 并包装成Packet数据包
     *
     * @param sender 发送者
     */
    public void onlineUserInformation(String sender) {
        Map hm = ThreadManagementCollection.getHashMap();

        String str = "用户名\n";
        Iterator iterator = hm.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            str += key + "\n";
        }

        ObjectOutputStream oos = null;
        try {

            oos = new ObjectOutputStream(ThreadManagementCollection.getSocket(sender).getOutputStream());
            oos.writeObject(setPacket(null, sender, str, sdf.format(new Date()), DataType.REQUEST_ONLINE_LIST));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法转发客户端的私信
     */
    public void forwardingSendsMessages(Data d) {
        ObjectOutputStream oos = null;
        try {
            Packet p = d.getPacket();
            if (onlineStatus(d)) {
                oos = new ObjectOutputStream(ThreadManagementCollection.getSocket(p.getAcceptor()).getOutputStream());
                oos.writeObject(d);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法判断用户是否在线
     * 不在线 判断数据库中是否存在此用户
     * 不存在给发送者回执提示信息
     * 存在把数据添加到数据库中
     * 返回false
     * 在线true
     */
    public boolean onlineStatus(Data d) {

        Map map = ThreadManagementCollection.getHashMap();
        Packet p = d.getPacket();
        if (map.get(p.getAcceptor()) != null) {
            return true;
        }

        if (bm.doesExistUser(p.getAcceptor())) {
            bm.addSqlDate(p);
            return false;
        } else {
            p.setAcceptor(p.getSender());
            p.setSender("");
            p.setContent("此用户不存在!!!");
            forwardingSendsMessages(d);
            return false;
        }
    }

    /**
     * 此方法封装信息数据包
     *
     * @param sender
     * @param acceptor
     * @param content
     * @param DT
     * @return
     */
    public Data setPacket(String sender, String acceptor, String content, String date, String DT) {
        Data data = new Data();
        data.setType(Type.INFORMATION_TYPE);
        Packet packet = new Packet();
        packet.setSender(sender);
        packet.setAcceptor(acceptor);
        packet.setContent(content);
        packet.setDate(date);
        packet.setDT(DT);
        data.setPacket(packet);
        return data;
    }

    /**
     * 此方法封装文件数据包
     *
     * @param sender
     * @param acceptor
     * @param file
     * @param DT
     * @return
     */
    public Data setFileData(String sender, String acceptor, byte[] file, String fileName, String DT, String date) {
        FileData fd = new FileData();
        fd.setSender(sender);
        fd.setAcceptor(acceptor);
        fd.setFile(file);
        fd.setDT(DT);
        fd.setFileName(fileName);
        fd.setDate(date);
        Data data = new Data();
        data.setFd(fd);
        data.setType(Type.FILE_TYPE);
        return data;
    }

    public InformationFlow(User u) {
        this.u = u;
    }

    public InformationFlow() {
    }

}
