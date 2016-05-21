package models.client;


import controllers.RecordManage;
import play.db.jpa.Model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xuqing on 2015/3/7.
 */
@Entity(name = "t_client_car")
public class Car extends Model {

    public long modelId;

    public String carNo;

    public String logo;

    /** 轮胎尺寸 */
    public String tireSize;

    /** 购车日期 */
    @Temporal(TemporalType.TIMESTAMP)
    public Date buyDate;

    /** 保险承保公司 */
    public String insuranceCompany;

    public String carFrameNo;

    @ManyToOne
    @JoinColumn(name = "clientId")
    public Client client;

    public String engineNo;

    @OneToMany(mappedBy = "car", cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    public List<CarRecord> recordList;

    public String getCarModelName() {
        CarType modelType = CarType.findById(modelId);
        if (null == modelType) {
            return "未找到该车型号";
        }
        return "[" + modelType.getBrandName() + "]" + modelType.name;
    }

    public String getDateString() {
        if (null == buyDate) {
            return "";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(buyDate);
        }
    }


    public String getBrandNameById(long modelId) {
        CarType type = CarType.findById(modelId);
        if (null == type) {
            return "";
        }
        CarType brandType = CarType.findById(type.brandId);
        return "[" + brandType.name + "]" + type.name;
    }
}
