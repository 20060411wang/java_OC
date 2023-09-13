package data.domain;

/**
 * @Author wl😹
 * @ClassName Table
 * @Date 2023/9/10
 * 此类对应数据库中的群聊表
 */

// Suppress prompts
//@SuppressWarnings("all")

public class Table {
    private String Group_Index;
    private String memberName;
    private String rank;

    public Table() {
    }

    public String getGroup_Index() {
        return Group_Index;
    }

    public void setGroup_Index(String group_Index) {
        Group_Index = group_Index;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
