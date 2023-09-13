package data.domain;

/**
 * @Author wl😹
 * @ClassName UserJoiningGroup
 * @Date 2023/9/8
 * 此类对应数据库中全部用户加入的群聊表
 */

// Suppress prompts
//@SuppressWarnings("all")

public class UserJoiningGroup {
    private String Group_Index;
    private String Group_Name;
    private String UserName;
    private String Date;

    public UserJoiningGroup() {
    }

    public String getGroup_Index() {
        return Group_Index;
    }

    public void setGroup_Index(String group_Index) {
        Group_Index = group_Index;
    }

    public String getGroup_Name() {
        return Group_Name;
    }

    public void setGroup_Name(String group_Name) {
        Group_Name = group_Name;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
