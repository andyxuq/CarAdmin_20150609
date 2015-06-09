package models;

import controllers.Application;
import play.db.jpa.Model;
import utils.ToolUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * User: xuqing
 * Date: 15-3-3
 * Time: 上午10:28
 */
public class UserInfo extends Model{

    public String userName;

    public String passWord;

    public int sex;

    public int age;

    public int isAdmin;

    public String phone;

    @Transient
    public String logo;

    @Temporal(TemporalType.TIMESTAMP)
    public Date createAt;

    public String getUserString() {
        return userName + Application.SESSION_KEY_SPLITER + passWord;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", logo='" + logo + '\'' +
                ", createAt=" + ToolUtils.getDate(createAt, "yyyy-MM-dd HH:mm:ss") +
                '}';
    }
}
