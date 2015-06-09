package controllers;

import models.User;
import models.client.Car;
import models.client.CarRecord;
import models.client.Client;
import models.utils.PageModel;
import models.utils.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import play.db.jpa.JPABase;
import utils.ToolUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuqing on 2015/3/14.
 */
public class RecordManage extends Application {

    public static void showCarRecord(int page) {
        if (0 == page) {
            page = 1;
        }
        List<CarRecord> recordList = new ArrayList<CarRecord>();
        PageModel pageModel = null;

        String carNo = params.get("carNo");
        String clientName = params.get("clientName");
        String carId = params.get("carId");
        String finished = params.get("finished");
        String processing = params.get("processing");
        if (StringUtils.isNotBlank(carNo)) {
            pageModel = getRecordByCarNo(recordList,  page, carNo);
        } else if (StringUtils.isNotBlank(clientName)) {
            pageModel = getRecordByClient(recordList,  page, clientName);
        } else if (StringUtils.isNotBlank(carId)) {
            pageModel = getRecordByCarId(recordList, page, NumberUtils.toLong(carId));
        } else if (StringUtils.isNotBlank(finished)) {
            pageModel = getRecordWhichFinished(recordList, page);
            pageModel.setFinished(1);
        } else if (StringUtils.isNotBlank(processing)) {
            pageModel = getRecordWhichProcessing(recordList, page);
            pageModel.setProcess(1);
        } else {
            pageModel = getRecordByDefault(recordList,  page);
        }
        recordList = (List<CarRecord>) pageModel.getDataList();
        render(recordList, pageModel);
    }

    static PageModel getRecordByDefault(List<CarRecord> recordList, int page) {
        int count = (int) CarRecord.count();
        PageModel pageModel = new PageModel(count, page);
        recordList = CarRecord.find("order by id desc").fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        pageModel.setDataList(recordList);
        return pageModel;
    }
    static PageModel getRecordByCarNo(List<CarRecord> recordList, int page, String carNo) {
        List<Car> carList = Car.find("carNo = ?", carNo).fetch();
        int count = 0;
        for (Car car : carList) {
            count += car.recordList.size();
            recordList.addAll(car.recordList);
        }
        PageModel pageModel = new PageModel(count, page);
        pageModel.setTotalPage(1);
        pageModel.setDataList(recordList);
        return pageModel;
    }
    static PageModel getRecordByClient(List<CarRecord> recordList, int page, String clientName) {
        List<Client> clientList = Client.find("userName = ?", clientName).fetch();
        int count = 0;
        for (Client client : clientList) {
            for (Car car :client.carList) {
                count += car.recordList.size();
                recordList.addAll(car.recordList);
            }
        }
        PageModel pageModel = new PageModel(count, page);
        pageModel.setTotalPage(1);
        pageModel.setDataList(recordList);
        return pageModel;
    }
    static PageModel getRecordByCarId(List<CarRecord> recordList, int page, long carId) {
        List<Car> carList = Car.find("id = ?", carId).fetch();
        int count = 0;
        for (Car car : carList) {
            count += car.recordList.size();
            recordList.addAll(car.recordList);
        }
        PageModel pageModel = new PageModel(count, page);
        pageModel.setTotalPage(1);
        pageModel.setDataList(recordList);
        return pageModel;
    }
    static PageModel getRecordWhichFinished(List<CarRecord> recordList, int page) {
        int count = (int) CarRecord.count("finishDate is not null");
        PageModel pageModel = new PageModel(count, page);
        recordList = CarRecord.find("finishDate is not null order by id desc").fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        pageModel.setDataList(recordList);
        return pageModel;
    }
    static PageModel getRecordWhichProcessing(List<CarRecord> recordList, int page) {
        int count = (int) CarRecord.count("finishDate is null");
        PageModel pageModel = new PageModel(count, page);
        recordList = CarRecord.find("finishDate is null order by id desc").fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        pageModel.setDataList(recordList);
        return pageModel;
    }

    public static void addCarRecord(long recordId) {
        String carId = params.get("carId");
        List<User> userList = User.findAll();
        if (0 != recordId) {
            CarRecord carRecord = CarRecord.findById(recordId);
            render(carRecord, userList);
        } else if (StringUtils.isNotBlank(carId)) {
            Car car = Car.findById(NumberUtils.toLong(carId));
            render(car, userList);
        } else {
            render(userList);
        }
    }

    public static void saveCarRecord(CarRecord carRecord) {
        if (null != carRecord.id && 0 != carRecord.id) {
            String finishDate = params.get("finishDate");
            if (StringUtils.isNotBlank(finishDate)) {
                carRecord.finishDate = new Date();
            } else {
                carRecord.finishDate = null;
            }
            String backDate = params.get("backDate");
            if (StringUtils.isNotBlank(backDate)) {
                carRecord.backDate = new Date();
            } else {
                carRecord.backDate = null;
            }
        } else {
            if (carRecord.recordDate == null) {
                carRecord.recordDate = new Date();
            }
        }
        String user = session.get(SESSION_USER_KEY);
        User userInfo = getUserFromSession(user);

        logger.info("用户：" + userInfo.userName + "保存了维护记录:" + carRecord.toString());
        carRecord.save();
        renderJSON(new Response(0, "成功"));
    }

    public static void deleteCarRecord(long recordId) {
        CarRecord record = CarRecord.findById(recordId);
        record.delete();
        renderJSON(new Response(0, "成功"));
    }
}
