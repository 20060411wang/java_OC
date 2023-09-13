package businessLayer;

import data.FileData;
import utils.InputUtils;

import java.io.*;
import java.util.Properties;

/**
 * @Author wl😹
 * @ClassName AcceptFileData
 * @Date 2023/9/11
 * 此类接受服务端返回的文件数据
 */

// Suppress prompts
//@SuppressWarnings("all")

public class AcceptFileData {
    private static final Properties properties = new Properties(); // 配置文件

    static {
        try {
            InputStream is = ClassLoader.getSystemResourceAsStream("Path.properties");
            properties.load(is); //获取配置文件路径
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法处理某用户发送的文件数据包
     *
     * @param fd
     */
    public static void OutFileData(FileData fd) {
        if (whetherAccept().equals("y")) {
            BufferedOutputStream bos = null;
            String path = FilePath();
            File file = new File(path);
            if (!file.exists())
                file.mkdirs();

            String out = file + "\\" + fd.getFileName();
            System.out.println("\t日期：" + fd.getDate() + "\n" + fd.getSender() + "用户给你发送文件到：" + out);
            try {

                bos = new BufferedOutputStream(new FileOutputStream(out));
                bos.write(fd.getFile(), 0, fd.getFile().length);

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("\t日期：" + fd.getDate() + "\n" + fd.getSender() + "用户给你发送文件已拒绝接受");
        }
    }

    public static String whetherAccept() {
        return String.valueOf(properties.get("WhetherAccept"));
    }

    /**
     * 获取存放文件的指定路径
     *
     * @return
     */
    public static String FilePath() {
        return String.valueOf(properties.get("filePath"));
    }

    /**
     * 此方法修改客户端的文件保存路径
     */
    public static void setFilePath() {
        System.out.print("请输入要修改的路径(盘符:\\目录\\目录..)：");
        String out = InputUtils.readString(60);
        properties.setProperty("filePath", out);
    }

    /**
     * 此方法修改用户是否接受文件
     */
    public static void setWhetherAccept() {
        String s = null;
        do {
            System.out.println("请输入是否接受文件(y/n)");
            s = InputUtils.readString(1);
        } while (!s.equals("y") || !s.equals("n"));

        properties.setProperty("WhetherAccept", s);
    }
}
