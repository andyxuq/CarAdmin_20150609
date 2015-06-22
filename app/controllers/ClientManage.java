package controllers;


import models.client.CarRecord;
import models.client.CarType;
import models.client.Client;
import models.utils.PageModel;
import models.utils.Response;
import org.apache.commons.lang.StringUtils;
import utils.ToolUtils;

import java.io.File;
import java.util.List;

/**
 * Created by xuqing on 2015/3/7.
 */
public class ClientManage extends Application{

    public static void showClientInfo(int page) {
        //此处需要从数据库中获取数据,现在模拟所以直接给几个数据
        int totalClient = 0, finishRecord = 0, processRecord = 0, complainRecord = 0;
        if (page == 0) page = 1;
        List<Client> clientList = null;
        PageModel pageModel = null;

        String userName = params.get("clientName");
        String phone = params.get("clientPhone");
        if (StringUtils.isNotBlank(userName)) {
            totalClient = (int)Client.count("userName like ?", "%" +userName+ "%");
            pageModel = new PageModel(totalClient, page);
            clientList = Client.find("userName like ? order by id desc", "%" +userName+ "%")
                    .fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        } else if (StringUtils.isNotBlank(phone)) {
            totalClient = (int)Client.count("phone like ?", "%" +phone+ "%");
            pageModel = new PageModel(totalClient, page);
            clientList = Client.find("phone like ? order by id desc", "%" +phone+ "%")
                    .fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        } else {
            totalClient = (int) Client.count();
            pageModel = new PageModel(totalClient, page);
            clientList = Client.find("order by id desc")
                    .fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        }

        finishRecord = (int) CarRecord.count("finishDate is not null");
        processRecord = (int) CarRecord.count("finishDate is null");
        renderArgs.put("totalClient", totalClient);
        renderArgs.put("finishRecord", finishRecord);
        renderArgs.put("processRecord", processRecord);
        renderArgs.put("complainRecord", 0);

        renderArgs.put("pageModel", pageModel);
        renderArgs.put("clientList", clientList);
        render();
    }

    public static void addClientInfo(long clientId) {
        if (0 != clientId) {
            Client clientInfo = Client.findById(clientId);
            render(clientInfo);
        } else {
            render();
        }
    }

    public static void saveClientInfo(Client clientInfo, File userLogo) {
        if (StringUtils.isNotBlank(clientInfo.logo) && null != userLogo) {
            ToolUtils.deleteFileByPath(clientInfo.logo);
        }
        if (null != userLogo) {
            try {
                String filePath = ToolUtils.saveFile(userLogo);
                clientInfo.logo = filePath;
            } catch (Exception e) {
                e.printStackTrace();
                renderJSON(new Response(1, "保存用户头像失败"));
            }
        }
        clientInfo.save();
        renderJSON(new Response(0, "成功"));
    }

    public static void deleteClientInfo(long clientId) {
        Client clientInfo = Client.findById(clientId);
        ToolUtils.deleteFileByPath(clientInfo.logo);
        clientInfo.delete();
        renderJSON(new Response(0, "成功"));
    }

    public static void showCarType(int page) {
        if (page == 0) {
            page = 1;
        }

        String brandName = params.get("brandName");
        if (StringUtils.isNotBlank(brandName)) {
            int count = (int) CarType.count("from t_car_type t where t.type = ? and exists (select 1 from t_car_type p where p.id = t.brandId and p.name like ?)", CarType.MODEL_TYPE, "%" + brandName + "%");
            PageModel pageModel = new PageModel(count, page);
            pageModel.putSearch("brandName", brandName);

            List<CarType> carTypeList = CarType.find("from t_car_type t where t.type = ? and exists (select 1 from t_car_type p where p.id = t.brandId and p.name like ?)", CarType.MODEL_TYPE, "%" + brandName + "%")
                    .fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
            render(carTypeList, pageModel);
        } else {
            int count = (int) CarType.count("type = ?", CarType.MODEL_TYPE);
            PageModel pageModel = new PageModel(count, page);

            List<CarType> carTypeList = CarType.find("type = ? order by id desc", CarType.MODEL_TYPE)
                    .fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
            render(carTypeList, pageModel);
        }
    }

    public static void addCarType(String brandName, String modelName) {
        List<CarType> typeList = CarType.find("name = ? and type = ?", brandName, CarType.BRAND_TYPE).fetch();
        CarType brandType = null;
        if (null == typeList || typeList.size() == 0) {
            brandType = new CarType();
            brandType.name = brandName;
            brandType.type = CarType.BRAND_TYPE;
            brandType.brandId = 0;
            brandType.save();
        } else {
            brandType = typeList.get(0);
        }

        long modelCount = CarType.count("name = ? and type = ? and brandId = ?", modelName, CarType.MODEL_TYPE, brandType.id);
        if (modelCount == 0) {
            CarType modelType = new CarType();
            modelType.name = modelName;
            modelType.type = CarType.MODEL_TYPE;
            modelType.brandId = brandType.id;
            modelType.save();
        }
        renderJSON(new Response(0, "成功"));
    }

    public static void modifyCarType(long modelId) {
        if (modelId != 0) {
            CarType carType = CarType.findById(modelId);
            render(carType);
        } else {
            render();
        }
    }

    public static void deleteCarType(long modelId) {
        CarType carType = CarType.findById(modelId);
        carType.delete();
        renderJSON(new Response(0, "成功"));
    }
}
