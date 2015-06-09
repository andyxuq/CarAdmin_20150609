package models.client;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.List;

/**
 * Created by xuqing on 2015/3/7.
 */
@Entity(name = "t_car_type")
public class CarType extends Model{

    public static final int BRAND_TYPE = 1;
    public static final int MODEL_TYPE = 2;

    public String name;

    @Column(columnDefinition = "INT default 1")
    public long brandId;

    public int type;

    public String getBrandName() {
        List<CarType> carBrand = CarType.find("id = ?", brandId).fetch();
        if (carBrand.size() != 0) {
            return carBrand.get(0).name;
        } else {
            return "无品牌";
        }
    }
}
