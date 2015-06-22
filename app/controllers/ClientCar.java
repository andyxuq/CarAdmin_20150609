package controllers;

import models.client.Car;
import models.client.CarType;
import models.client.Client;
import models.utils.PageModel;
import models.utils.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import play.data.binding.As;
import utils.ToolUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by xuqing on 2015/3/12.
 */
public class ClientCar extends Application {

    public static void showClientCar(int page) {
        if (0 == page) {
            page = 1;
        }
        int totalCar = 0;
        List<Car> carList = null;
        PageModel pageModel = null;

        String carNo = params.get("carNo");
        String clientId = params.get("clientId");
        if (StringUtils.isNotBlank(carNo)) {
            totalCar = (int) Car.count("carNo like ?", "%" +carNo+ "%");
            pageModel = new PageModel(totalCar, page);
            carList = Car.find("carNo like ? order by id desc", "%" +carNo+ "%")
                    .fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        } else if (StringUtils.isNotBlank(clientId)) {
            totalCar = (int) Car.count("clientId = ?", NumberUtils.toLong(clientId));
            pageModel = new PageModel(totalCar, page);
            carList = Car.find("clientId = ? order by id desc", NumberUtils.toLong(clientId))
                    .fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        } else {
            totalCar = (int) Car.count();
            pageModel = new PageModel(totalCar, page);
            carList = Car.find("order by id desc")
                    .fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        }

        pageModel.putSearch("carNo", carNo);
        render(carList, pageModel);
    }

    public static void addClientCar(long carId) {
        List<CarType> ctList = CarType.find("type = ? order by id desc", CarType.MODEL_TYPE).fetch();
        String clientId = params.get("clientId");
        if (0 != carId) {
            Car car = Car.findById(carId);
            render(car, ctList);
        } else if (StringUtils.isNotBlank(clientId)) {
            Client carUser = Client.findById(NumberUtils.toLong(clientId));
            render(carUser, ctList);
        } else {
            render(ctList);
        }
    }

    public static void saveClientCar(Car car, File carLogo, @As("yyyy-MM-dd") Date buyDate) {
        if (null != carLogo && StringUtils.isNotBlank(car.logo)) {
            ToolUtils.deleteFileByPath(car.logo);
        }
        if (null != carLogo) {
            try {
                String logoPath = ToolUtils.saveFile(carLogo);
                car.logo = logoPath;
            } catch (IOException e) {
                e.printStackTrace();
                renderJSON(new Response(1, "保存汽车图片失败"));
            }
        }
        if (null != buyDate) {
            car.buyDate = buyDate;
        }
        car.save();
        renderJSON(new Response(0, "成功"));
    }

    public static void deleteClientCar(long carId) {
        Car car = Car.findById(carId);
        if (StringUtils.isNotBlank(car.logo)) {
            ToolUtils.deleteFileByPath(car.logo);
        }
        car.delete();

        renderJSON(new Response(0, "成功"));
    }
}
