import business.InformationFlow;
import thread＿.ReceiveThread;

/**
 * @Author wl😹
 * @Version 1.0
 * @ClassName ViewServer
 * @Date 2023/8/30
 */

// Suppress prompts
//@SuppressWarnings("all")

public class ViewServer {
    public static void main(String[] args) {
        new Thread(new ReceiveThread()).start();
    }
}
