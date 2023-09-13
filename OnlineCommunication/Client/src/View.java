import businessLayer.AcceptFileData;
import businessLayer.ClientSendsMessages;
import businessLayer.Connect;
import data.DataType;
import data.User;
import utils.InputUtils;

/**
 * @Author wl😹
 * @Version 1.0
 * @ClassName View
 * @Date 2023/8/30
 */

// Suppress prompts
//@SuppressWarnings("all")

public class View {
    private ClientSendsMessages csm = null;

    public View() {
        boolean flag = true;
        while (flag) {
//            Connect.setIP();
            System.out.println("----------欢迎登录通信系统----------");
            System.out.println("\t\t\t1 登录");
            System.out.println("\t\t\t2 注册");
            System.out.println("\t\t\t3 退出");
            System.out.print("请输入(1/2)：");
            int key = InputUtils.readInt();
            if (key == 1) { // 登录
                System.out.print("请输入ID(6)：");
                String ID = InputUtils.readString(6);
                System.out.print("请输入PW(8)：");
                String PW = InputUtils.readString(8);
                // 发送给服务端判断是否有此用户
                User user = new User(ID, PW, DataType.LOG_ON_USER);
                view_2(Connect.judgement(user), user);
            } else if (key == 2) { // 注册
                System.out.print("请输入ID(6)：");
                String ID = InputUtils.readString(6);
                System.out.print("请输入PW(8)：");
                String PW = InputUtils.readString(8);
                User u = new User(ID, PW, DataType.REGISTER_USER);
                view_2(Connect.registerUser(u), u);
            } else if (key == 3) {
                System.out.println("退出");
                flag = false;
            } else System.out.println("输入错误");
        }
    }

    public void Group() {
        System.out.println("----------群聊信息----------");
        System.out.println("\t\t1 创建群聊");
        System.out.println("\t\t2 加入群聊");
        System.out.println("\t\t3 显示群聊");
        System.out.println("\t\t4 群聊信息");
        System.out.print("请输入：");
        switch (InputUtils.readInt()) {
            case 1:
                csm.sendsCreateGroup();
                break;
            case 2:
                csm.joinGroup();
                break;
            case 3:
                csm.talkAboutInformation();
                break;
            case 4:
                csm.sendsGroupChat();
                break;
            default:
                System.out.println("输入错误");
        }
    }

    public void File() {
        System.out.println("----------文件信息----------");
        System.out.println("\t\t1 发送文件");
        System.out.println("\t\t2 修改地址");
        System.out.println("\t\t3 是否接受文件");
        System.out.print("请输入：");
        switch (InputUtils.readInt()) {
            case 1:
                csm.sendsFileDate();
                break;
            case 2:
                AcceptFileData.setFilePath();
                break;
            case 3:
                AcceptFileData.setWhetherAccept();
                break;
            default:
                System.out.println("输入错误");
        }
    }


    public void view_2(boolean b, User user) {
        if (b) {
            csm = new ClientSendsMessages(user);
            System.out.println("==========欢迎(" + user.getID() + ")登录网络通讯==========");
            while (true) {
                System.out.println("~----------通信系统----------~");
                System.out.println("\t\t1 显示在线用户");
                System.out.println("\t\t2 私      信");
                System.out.println("\t\t3 群      聊");
                System.out.println("\t\t4 发 送 文 件");
                System.out.println("\t\t0 退出");
                System.out.print("请输入：");
                switch (InputUtils.readInt()) {
                    case 1:
                        csm.requestOnLineList();
                        break;
                    case 2:
                        csm.sendsMessages();
                        break;
                    case 3:
                        Group();
                        break;
                    case 4:
                        File();
                        break;
                    case 0:
                        csm.sendsExit();
                        break;
                    default:
                        System.out.println("输入错误");
                }
            }
        }
    }

}
