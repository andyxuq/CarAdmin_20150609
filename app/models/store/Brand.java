package models.store;

import play.db.jpa.Model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuqing on 2015/6/8.
 */
@Entity
@Table(name = "t_store_brand")
public class Brand extends Model{

    public String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    public Date createdAt;

    public String getCreatedAt() {
        if (null == createdAt) {
            return "";
        } else {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(createdAt);
        }
    }
}
