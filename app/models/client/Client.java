package models.client;

import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Random;

/**
 * Created by xuqing on 2015/3/7.
 */
@Entity(name = "t_client")
public class Client extends Model{

    public String userName;

    public String logo;

    public String phone;

    public int sex;

    public int age;

    public String address;

    public String note;

    @OneToMany(mappedBy = "client", cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    public List<Car> carList;

    public int getClientTimes() {
        int serviceCount = 0;
        if (null != carList) {
            for (Car car : carList) {
                List<CarRecord> recordList = car.recordList;
                serviceCount += recordList == null ? 0 : recordList.size();
            }
        }
        return serviceCount;
    }
}
