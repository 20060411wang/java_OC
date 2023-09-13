package threadCollection;


import Thread_.Thread_;

import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
     * 获取指定用户的socket线程
     * @param id 指定用户id
     * @return
     */
    public static Socket getSocket(String id) {
        return hm.get(id).getSocket();
    }

    public static Thread_ get(String id) {
        return hm.get(id);
    }
}
