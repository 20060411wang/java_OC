package businessLayer;

import data.*;
import threadCollection.ThreadManagementCollection;
import utils.InputUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author wl😹
 * @Version 1.0
 * @ClassName ClientSendsMessages
 * @Date 2023/8/31
 * 客户端发送信息
 */

// Suppress prompts
//@SuppressWarnings("all")

public class ClientSendsMessages {
    private User u;

    public ClientSendsMessages(User u) {
        this.u = u;
    }

    /**
     * 此方法发送退出信息到服务端
     */
    public void sendsExit() {
        sendsChat(setPacket(u.getID(), null, null, DataType.SENDS_EXIT));
    }

    /**
     * 次方法发送文件到服务端
     */
    public void sendsFileDate() {
        System.out.print("请输入要发送的用户：");
        String acceptID = InputUtils.readString(6);
        System.out.print("源文件地址(盘符:目录\\文件名.后缀)：");
        String src = InputUtils.readString(64);
        Data data = InputFileData(acceptID, src);
        if (data != null)
            sendsChat(data);
        else
            System.out.println("数据为空");

    }

    /**
     * 次方法获取本地文件 封装成byte数组
     * 判断是否是标准文件
     * 判断文件大小是否小于 16777215b
     *
     * @param src
     * @return
     */
    public Data InputFileData(String acceptID, String src) {
        File file = new File(src);
        if (file.isFile()) {
            int length = (int) file.length();
            if (length < 16777215) {
                byte[] buf = new byte[length];
                BufferedInputStream bis = null;
                try {
                    bis = new BufferedInputStream(new FileInputStream(file));
                    bis.read(buf);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return setFileData(u.getID(), acceptID, buf, file.getName(), DataType.FILE_DATE);
            } else {
                System.out.println("文件太大，不能超过16MB(16777215字节)");
                return null;
            }
        } else {
            System.out.println("无法发送目录文件");
            return null;
        }
    }

    /**
     * 此方法向服务端发送群聊信息
     */
    public void sendsGroupChat() {
        System.out.print("请输入你要发送的群聊名称：");
        String Group_name = InputUtils.readString(6);
        System.out.print("输入发送的内容：");
        String content = InputUtils.readString(64);

        sendsChat(setPacket(u.getID(), Group_name, content, DataType.GROUP_CHAT));
    }

    /**
     * 此方法向服务端拉取此用户已加入的群聊
     */
    public void talkAboutInformation() {
        // 发送信息
        sendsChat(setPacket(u.getID(), "", "", DataType.TALK_ABOUT_INFORMATION));
    }

    /**
     * 此方法把当前用户添加指定群聊中
     */
    public void joinGroup() {
        System.out.print("输入要加入的群聊(名称)：");
        String GroupName = InputUtils.readString(6);
        sendsChat(setPacket(u.getID(), null, GroupName, DataType.JOIN_GROUP));
    }

    /**
     * 此方法向服务端请求在线用户信息
     */
    public void requestOnLineList() {
        // 发送数据包
        sendsChat(setPacket(u.getID(), null, null, DataType.REQUEST_ONLINE_LIST));
    }

    /**
     * 此方法给指定用户发送私聊
     */
    public void sendsMessages() {
        System.out.print("输入要发送的用户(id)：");
        String acceptor = InputUtils.readString(6);
        System.out.print("输入要发送的信息(64)：");
        String content = InputUtils.readString(64);

        sendsChat(setPacket(u.getID(), acceptor, "：" + content, DataType.PRIVATE_CHAT));
    }

    /**
     * 此方法发送创建群聊信息
     */
    public void sendsCreateGroup() {
        System.out.print("输入要创建的群聊名称：");
        String name = InputUtils.readString(6);

        sendsChat(setPacket(u.getID(), null, name, DataType.CREATE_GROUP));
    }

    /**
     * 此方法转发提供的数据包给
     * 服务端
     *
     * @param d
     */
    public void sendsChat(Data d) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ThreadManagementCollection.getSocket(u.getID()).getOutputStream());
            oos.writeObject(d);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    public Data setPacket(String sender, String acceptor, String content, String DT) {
        Data data = new Data();
        data.setType(Type.INFORMATION_TYPE);
        Packet packet = new Packet();
        packet.setSender(sender);
        packet.setAcceptor(acceptor);
        packet.setContent(content);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        packet.setDate(sdf.format(new Date()));
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
    public Data setFileData(String sender, String acceptor, byte[] file, String fileName, String DT) {
        FileData fd = new FileData();
        fd.setSender(sender);
        fd.setAcceptor(acceptor);
        fd.setFile(file);
        fd.setDT(DT);
        fd.setFileName(fileName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        fd.setDate(sdf.format(new Date()));
        Data data = new Data();
        data.setFd(fd);
        data.setType(Type.FILE_TYPE);
        return data;
    }


}
