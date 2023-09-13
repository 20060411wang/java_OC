package data;

import java.io.Serializable;

/**
 * @Author wl😹
 * @Version 1.0
 * @ClassName User
 * @Date 2023/8/30
 * 此类保存用户信息
 */

// Suppress prompts
//@SuppressWarnings("all")

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ID;
    private String PassWord;
    private String DT;

    public User(String ID, String passWord,String DT) {
        this.ID = ID;
        PassWord = passWord;
        this.DT = DT;
    }

    public String getDT() {
        return DT;
    }

    public void setDT(String DT) {
        this.DT = DT;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }
}
