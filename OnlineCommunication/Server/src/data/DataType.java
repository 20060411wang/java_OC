package data;

/**
 * @Author wl😹
 * @Version 1.0
 * @ClassName DataType
 * @Date 2023/8/31
 * 此接口表示数据类型
 */

// Suppress prompts
//@SuppressWarnings("all")

public interface DataType {
    final static String REQUEST_ONLINE_LIST = "1"; // 拉取在线用户列表
    final static String PRIVATE_CHAT = "2"; // 私聊
    final static String GROUP_CHAT = "3"; // 群聊
    final static String FILE_DATE = "4"; // 文件
    final static String CREATE_GROUP = "5"; // 创建群
    final static String JOIN_GROUP = "6"; // 加入群聊
    final static String TALK_ABOUT_INFORMATION = "7"; // 拉取用户群聊
    final static String SENDS_EXIT = "8";// 请求推出
    final static String REGISTER_USER = "9";// 注册用户
    final static String LOG_ON_USER = "10"; // 登录
}