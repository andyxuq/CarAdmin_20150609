package models.store;

import play.db.jpa.Model;

import javax.persistence.*;

/**
 * Created by xuqing on 2015/6/10.
 */
@Entity
@Table(name = "t_store_resource")
public class Resource extends Model{

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    public Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    public Category category;

    /**型号 */
    public String type;

    /** 规格 1L/2L/3L/1瓶/2瓶..*/
    public String spec;

    /** 单位 */
    public String unit;

    /** 数量 */
    public int totalCount;

    /** 成本价 */
    public String costPrice;

    /** 销售价 */
    public String salesPrice;
}
