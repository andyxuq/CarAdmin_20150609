package models.store;

import models.client.CarRecord;
import play.db.jpa.Model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuqing on 2015/6/16.
 */
@Entity
@Table(name = "t_store_record")
public class StoreRecord extends Model {

    @ManyToOne
    @JoinColumn(name = "record_id")
    public CarRecord carRecord;

    @Temporal(TemporalType.TIMESTAMP)
    public Date createdAt;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    public Resource resource;

    /** 使用的资源数量 */
    public int resourceNum;

    /** 价钱 */
    public String salesPrice;

    public String getCreatedString() {
        if (null == createdAt) {
            return "";
        } else {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(createdAt);
        }
    }

    public String getTotalNameById(long id) {
        Resource res = Resource.findById(id);
        if (null == res) {
            return "";
        } else {
            return res.brand.name + "->" + res.category.name + "->" + res.type;
        }
    }
}

