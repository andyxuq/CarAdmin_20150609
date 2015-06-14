package models.store;

import play.db.jpa.Model;
import utils.ToolUtils;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuqing on 2015/6/14.
 */
@Entity
@Table(name = "t_store")
public class ResourceStore extends Model {

    @Temporal(TemporalType.TIMESTAMP)
    public Date createdAt;

    /**
     * 数量
     */
    public int count;

    /** 进价 */
    public String costPrice;
    /** 总价 */
    public String totalPrice;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    public Resource resource;

    public String getCreatedAtString() {
        if (null == createdAt) {
            return "";
        } else {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(createdAt);
        }
    }

}
