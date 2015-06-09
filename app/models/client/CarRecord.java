package models.client;

import models.User;
import play.db.jpa.Model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuqing on 2015/3/7.
 */
@Entity(name = "t_car_record")
public class CarRecord extends Model{

    public static final int TYPE_HOLD = 1;
    public static final int TYPE_FIX = 2;

    @ManyToOne
    @JoinColumn(name = "carId")
    public Car car;

    /**
     * 1:保养
     * 2:维修
     */
    public int type;

    @Temporal(TemporalType.TIMESTAMP)
    public Date recordDate;

    public String requireNote;

    @Temporal(TemporalType.TIMESTAMP)
    public Date finishDate;

    @Temporal(TemporalType.TIMESTAMP)
    public Date backDate;

    public String recordNote;

    //里程数
    public String mileRecord;

    //花费
    public String cost;

    @ManyToOne
    @JoinColumn(name="userId")
    public User user;

    public String getDateString(Date date) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    @Override
    public String toString() {
        return "CarRecord{" +
                "cost='" + cost + '\'' +
                ", mileRecord='" + mileRecord + '\'' +
                ", recordNote='" + recordNote + '\'' +
                ", backDate=" + backDate +
                ", finishDate=" + finishDate +
                ", requireNote='" + requireNote + '\'' +
                ", recordDate=" + recordDate +
                ", type=" + type +
                '}';
    }
}
