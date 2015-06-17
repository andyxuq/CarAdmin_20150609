package controllers;

import models.client.Car;
import models.client.CarRecord;
import models.store.*;
import models.utils.PageModel;
import models.utils.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import play.db.jpa.JPABase;
import utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuqing on 2015/6/16.
 */
public class StoreRecordController extends Application {

    public static void getResources(int page) {
        page = page == 0 ? 1 : page;

        String recordId = params.get("recordId");
        String resourceId = params.get("resourceId");
        String carNum = params.get("carNum");

        StringBuffer query = new StringBuffer();
        List<Object> args = new ArrayList<Object>();
        if (StringUtils.isNotBlank(recordId)) {
            query.append(" record_id = ? and ");
            args.add(NumberUtils.toLong(recordId));
        }
        if (StringUtils.isNotBlank(resourceId)) {
            query.append(" resource_id = ? and ");
            args.add(NumberUtils.toLong(resourceId));
        }

        if (StringUtils.isNotBlank(carNum)) {
            List<Car> carList = Car.find("carNo like ?", "%" + carNum + "%").fetch();
            StringBuffer builder = new StringBuffer();
            for (Car car : carList) {
                List<CarRecord> recordList = car.recordList;
                for (CarRecord record : recordList) {
                    builder.append("," + record.id);
                }
            }
            if (builder.toString().length() > 0) {
                String sql = builder.substring(1, builder.length());
                query.append(" record_id in (" + sql + ") and ");
            }
        }

        List<Resource> resourceList = Resource.findAll();
        if (query.length() > 0) {
            String sql = query.substring(0, query.lastIndexOf("and"));
            int count = (int) StoreRecord.count(sql, args.toArray());
            sql += " order by id desc";
            PageModel pageModel = new PageModel(count, page);
            List<Object> recordList = StoreRecord.find(sql, args.toArray()).fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);

            render(recordList, pageModel, resourceList);
        } else {
            int count = (int) StoreRecord.count();
            query.append(" order by id desc");
            PageModel pageModel = new PageModel(count, page);
            List<Object> recordList = StoreRecord.find(query.toString()).fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);

            render(recordList, pageModel, resourceList);
        }
    }

    public static void showResource(long resourceId) {
        StoreRecord resource = StoreRecord.findById(resourceId);

        CarRecord carRecord = null;
        String recordId = params.get("recordId");
        if (StringUtils.isNotBlank(recordId)) {
            carRecord = CarRecord.findById(NumberUtils.toLong(recordId));
        }

        List<Resource> resourceList = Resource.findAll();
        render(resource, carRecord, resourceList);
    }

    public static void deleteResource(long resourceId) {
        StoreRecord resource = StoreRecord.findById(resourceId);
        resource.delete();

        resource.resource.totalCount += resource.resourceNum;
        resource.resource.save();
        renderJSON(new Response());
    }

    public static void saveResource(StoreRecord resource) {
        if (StringUtils.isBlank(resource.salesPrice)) {
            resource.salesPrice = ToolUtils.mul(resource.resource.salesPrice, resource.resourceNum);
        }

        if (resource.id == null) {
            if (resource.resourceNum > resource.resource.totalCount) {
                renderJSON(new Response(1, "配件库存不足，总库存:" + resource.resource.totalCount));
            }

            resource.resource.totalCount = resource.resource.totalCount - resource.resourceNum;
            resource.resource.save();
        }
        resource.save();
        renderJSON(new Response());
    }
}
