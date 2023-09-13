package threadCollection;

import thread＿.Thread_;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/**
 * @Author wl😹
 * @Version 1.0
 * @ClassName ThreadManagementCollection
 * @Date 2023/8/31
 * 此类管理线程
 */

// Suppress prompts
//@SuppressWarnings("all")

public class ThreadManagementCollection {
    private final static HashMap<String, Thread_> hm = new HashMap();

    public static void add(String id, Thread_ t) {
        hm.put(id, t);
    }

    /**
     * 判断用户是否在线
     *
     * @param id
     * @return
     */
    public static boolean getUser(String id) {
        return hm.get(id) == null ? true : false;
    }

    /**
     * 获取指定用户的socket线程
     *
     * @param id 指定用户id
     * @return
     */
    public static Socket getSocket(String id) {
        return hm.get(id).getSocket();
    }

    public static HashMap getHashMap() {
        return hm;
    }

    public static void deleteUser(String userId) {
        try {
            hm.get(userId).setFlag(false);
            hm.get(userId).getSocket().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        hm.remove(userId);
    }
}
