package models;

import controllers.Application;
import play.db.jpa.Model;
import utils.ToolUtils;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuqing on 2015/3/3.
 */
@Entity
@Table(name = "t_user")
public class User  extends Model {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public String userName;

    public String passWord;

    public int sex;

    public int age;

    public int isAdmin;

    public String phone;

    public String nickName;

    public String logo;

    @Temporal(TemporalType.TIMESTAMP)
    public Date createAt;

    public String getUserString() {
        return userName + Application.SESSION_KEY_SPLITER + passWord + Application.SESSION_KEY_SPLITER + isAdmin;
    }

    public String getDateString() {
        if (null == createAt) {
            return "";
        }
        return format.format(createAt);
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", logo='" + logo + '\'' +
                ", createAt=" + ToolUtils.getDate(createAt, "yyyy-MM-dd HH:mm:ss") +
                '}';
    }
}
