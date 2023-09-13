package business;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import data.DataType;
import data.FileData;
import data.Packet;
import data.User;
import data.domain.OfflineFileInformation;
import data.domain.OfflineInformationTable;
import data.domain.Table;
import data.domain.UserJoiningGroup;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import threadCollection.ThreadManagementCollection;
import thread＿.Thread_;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * @Author wl😹
 * @Version 1.0
 * @ClassName BusinessMeth
 * @Date 2023/8/30
 * 次类用来处理 登录业务和sql信息业务
 */

// Suppress prompts
@SuppressWarnings("all")

public class BusinessMethod {
    private static final Object obj = new Object();
    private static final Object obj1 = new Object();
    private static DataSource ds; // 德鲁伊
    private static Connection connection = null;
    private static Properties properties = new Properties();
    private static QueryRunner qr = new QueryRunner();
    private static InformationFlow iff = new InformationFlow();

    static {
        // 获取配置文件信息
        try {
            InputStream is = ClassLoader.getSystemResourceAsStream("druid.properties");
            properties.load(is);
            ds = DruidDataSourceFactory.createDataSource(properties);

            connection = ds.getConnection(); // 获取sql

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void logOnUser(User user, Socket socket) {
        // 返回是否登录成功
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            if (ComparisonUser(user)) {
                oos.writeObject(true);
                    /*
                        登录成功后创建一个线程
                        并把socket和这个线程添加到线程集合中
                        把线程启动
                     */
                System.out.println(user.getID() + "　用户登陆成功");
                Thread_ thread = new Thread_(user, socket);
                new Thread(thread).start();
                ThreadManagementCollection.add(user.getID(), thread);
                selectPITDate(user.getID()); // 推送离线信息
                selectPIFile(user.getID()); // 推送离线文件
            } else {
                System.out.println(user.getID() + "　用户登陆失败");
                oos.writeObject(false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 次方法注册用户
     *
     * @param u
     * @return
     */
    public static void registerUser(User u, Socket socket) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            if (doesExistUser(u.getID())) {
                oos.writeObject(false);
                System.out.println(u.getID() + "用户注册失败");
            } else {
                addSQLUser(u);
                Thread_ thread = new Thread_(u, socket);
                new Thread(thread).start();
                ThreadManagementCollection.add(u.getID(), thread);
                oos.writeObject(true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法在数据库中添加一个用户
     *
     * @param u
     */
    public static void addSQLUser(User u) {
        String sql = "insert into user values (?,md5(?),?)";
        synchronized (obj1) {
            try {
                qr.update(connection, sql, u.getID(), u.getPassWord(), 0);
                System.out.println(u.getID() + "用户注册成功");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 此方法推送离线文件
     *
     * @param accept
     */
    public static void selectPIFile(String accept) {
        // sql语句
        String sql = "select sender,acceptor,file,fileName,DT,Date from `offlinefileinformation` where acceptor = ?";

        try {
            List<OfflineFileInformation> list = qr.query(connection, sql, new BeanListHandler<OfflineFileInformation>(OfflineFileInformation.class), accept);

            if (list.size() > 0) {
                for (OfflineFileInformation ofi : list)
                    iff.sendsFileData(iff.setFileData(ofi.getSender(), ofi.getAcceptor(), ofi.getFile(), ofi.getFileName(), ofi.getDT(), ofi.getDate())); // 发送数据包
                deleteSqlFileChat(accept);// 删除离线信息
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法删除指定用户的离线文件信息
     *
     * @param accept
     */
    public static void deleteSqlFileChat(String accept) {
        String sql = "delete from offlinefileinformation where acceptor = ?";
        try {
            qr.update(connection, sql, accept);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法给sql数据库中离线文件表添加数据
     * 添加文件数据
     *
     * @param p 数据包
     */
    public static void addSqlData(FileData fd) {

        // 创建sql语句
        String sql = "insert into `OfflineFileInformation` values(null,?,?,?,?,?,?)";

        try {
            qr.update(connection, sql, fd.getSender(), fd.getAcceptor(), fd.getFile(), fd.getFileName(), fd.getDT(), fd.getDate());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法转发群聊信息
     * 获取此群聊中的出自己以外的所有用户
     * 判断数量是否大于0
     * 则发送数据给指定群聊用户
     *
     * @param p
     */
    public static void senderGroupChat(Packet p, String groupIndex) {
        String sql = "select * from `" + groupIndex + "` where memberName != ?";
        String sql1 = "select * from `" + groupIndex + "` where memberName = ?";
        try {
            List<Table> list = qr.query(connection, sql, new BeanListHandler<Table>(Table.class), p.getSender());
            if (list.size() > 0) {
                Table table1 = qr.query(connection, sql1, new BeanHandler<Table>(Table.class), p.getSender());
                for (Table table : list)
                    iff.forwardingSendsMessages(iff.setPacket(p.getAcceptor(), table.getMemberName(), p.getContent(), "-----" + p.getAcceptor() + "-----\n" + p.getSender() + "(" + table1.getRank() + ")：？" + p.getDate(), DataType.GROUP_CHAT));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法查询该群聊中是否有该用户
     * <p>
     * 用户存在该群聊中 返回true否则返回false
     *
     * @param groupName
     * @return
     */
    public static boolean selectGroup(String userName, String Index) {
        String sql = "select * from `" + Index + "` where memberName = ?";
        try {
            return qr.query(connection, sql, new ScalarHandler(), userName) != null ? true : false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 此方法查找指定用户加入的群聊
     *
     * @param name
     */
    public static String selectGroupUser(String name) {
        String sql = "select * from `userjoininggroup` where UserName = ?";
        String cooent = "加入的群聊：\n";
        try {

            List<UserJoiningGroup> list = qr.query(connection, sql, new BeanListHandler<UserJoiningGroup>(UserJoiningGroup.class), name);
            if (list.size() > 0) {
                for (UserJoiningGroup userJoiningGroup : list) {
                    cooent += userJoiningGroup.getGroup_Name() + "\t" + "\n";
                }
            } else {
                cooent = "您未加入群聊!";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cooent;

    }

    /**
     * 提取sql中的用户信息
     * 判断是否有此用户
     * 判断用户是否已经登陆
     */
    public static boolean ComparisonUser(User u) {
        String sqlUser = "select * from User where ID = ? and PassWord = md5(?)"; // sql 语句

        try {
            return qr.query(connection, sqlUser, new ScalarHandler(), u.getID(), u.getPassWord()) != null && ThreadManagementCollection.getUser(u.getID()) ? true : false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 此方法给sql数据库中离线信息表添加数据
     * 添加群聊和私信数据
     *
     * @param p 数据包
     */
    public static void addSqlDate(Packet p) {


        // 创建sql语句
        String sql = "insert into OfflineInformationTable values(null,?,?,?,?,?)";

        try {
            qr.update(connection, sql, p.getSender(), p.getAcceptor(), p.getContent(), p.getDate(), p.getDT());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断用户是否有离线信息
     * 如果有就推送给用户
     * 推送后删除sql端数据
     *
     * @param acceptor
     */
    public static void selectPITDate(String acceptor) {
        // sql语句
        String sql = "select sender, acceptor, content, date, DT from offlineinformationtable where acceptor = ?";

        try {

            List<OfflineInformationTable> list = qr.query(connection, sql, new BeanListHandler<OfflineInformationTable>(OfflineInformationTable.class), acceptor);

            if (list.size() > 0) {
                for (OfflineInformationTable oit : list)
                    iff.forwardingSendsMessages(iff.setPacket(oit.getSender(), oit.getAcceptor(), oit.getContent(), oit.getDate(), oit.getDT())); // 发送数据包
                deleteSqlDate(acceptor);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 此方法指定删除用户的离线信息
     *
     * @param acceptor
     * @return
     */
    public static int deleteSqlDate(String acceptor) {

        // sql语句
        String sql = "delete from offlineinformationtable where acceptor = ?";

        try {
            return qr.update(connection, sql, acceptor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 此方法判断数据库中是否有此用户
     * 有返回true
     * 否则返回false
     *
     * @param acceptor 用户id
     * @return
     */
    public static boolean doesExistUser(String userId) {
        String sql = "select * from User where ID = ?";
        try {
            return qr.query(connection, sql, new ScalarHandler(), userId) != null ? true : false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法判断此群聊是否存在
     * 存在返回false
     * 不存在返回true
     *
     * @param userId
     * @return
     */
    public static boolean selectSqlGroup(String groupName) {
        String sql = "select * from `group` where Group_Name = ?";

        try {
            return qr.query(connection, sql, new ScalarHandler(), groupName) == null ? true : false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法创建群聊信息
     * 现在group群聊管理表中添加一条数据
     * 创建一张表 然后给表添加内容
     * （谁创建谁是群主）
     */
    public static void createGroup(Packet p) {
        synchronized (obj) {
            long l = setGroup_count();
            String sql = "create table `" + l + "` (" + "`Group_Index` varchar(10)  not null," + "`memberName` varchar(6) primary key not null," + "`rank` varchar(10) not null," + "foreign key (Group_Index) references `Group`(Group_Index)" + ")";
            String sql1 = "insert into `" + l + "` values ('" + l + "',?,'牢大')";
            String sql2 = "insert into `group` values(?,?,?,?)";
            String sql3 = "insert into `UserJoiningGroup` values(?,?,?,?)";
            try {
                qr.update(connection, sql2, l, p.getContent(), p.getSender(), p.getDate());
                qr.update(connection, sql);
                qr.update(connection, sql1, p.getSender());
                qr.update(connection, sql3, l, p.getContent(), p.getSender(), p.getDate());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 此方法获取群聊id编号
     * 并修改编号
     *
     * @return
     */
    public static long setGroup_count() {
        String sql = "select `count` from `group_count`";
        try {

            Object l1 = qr.query(connection, sql, new ScalarHandler());
            long l2 = Integer.parseInt(String.valueOf(l1));

            String sql1 = "update `group_count` set count = ?";
            qr.update(connection, sql1, (l2 + 1));
            return l2;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法判断用户是否
     * 在指定的群聊中
     * 如果存在返回true
     * 不存在返回false
     */
    public static boolean selectUserExist(String id, String Group_name) {
        String sql = "select * from `UserJoiningGroup` where UserName = ? and Group_Name = ?";
        try {

            return qr.query(connection, sql, new ScalarHandler(), id, Group_name) == null ? true : false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法给数据库添加一条
     * 群聊信息
     * 并在该群表中添加一条数据
     *
     * @param p 数据包
     */
    public static void insertIntoUesr(Packet p) {
        String index = selectGroupIndex(p.getContent());
        String sql = "insert into `UserJoiningGroup` values(?,?,?,?)";
        String sql1 = "insert into `" + index + "` values(" + index + ",?,?)";
        try {
            qr.update(connection, sql, index, p.getContent(), p.getSender(), p.getDate());
            qr.update(connection, sql1, p.getSender(), "牢弟");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法获取指定群名的编号
     */
    public static String selectGroupIndex(String Goup_Name) {
        String sql = "select Group_Index from `group` where Group_Name = ?";
        String index = null;
        try {
            index = String.valueOf(qr.query(connection, sql, new ScalarHandler(), Goup_Name));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return index;
    }

    /**
     * 获取指定用户权限
     *
     * @param UserName
     * @return
     */
    public static String selectUserJD(String UserName) {
        String sql = "select JurisDiction from `user` where ID = ?";
        try {
            String JD = String.valueOf(qr.query(connection, sql, new ScalarHandler(), UserName));
            return JD;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
