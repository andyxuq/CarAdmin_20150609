package controllers;

import models.store.Resource;
import models.store.ResourceStore;
import models.utils.PageModel;
import models.utils.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuqing on 2015/6/14.
 */
public class StoreController extends Application {


    public static void getResources(int page) {
        page = page == 0 ? 1 : page;

        String startDate = params.get("startDate");
        String endDate = params.get("endDate");
        long resourceId = NumberUtils.toLong(params.get("resourceId"));

        StringBuffer query = new StringBuffer();
        List<Object> args = new ArrayList<Object>();
        if (StringUtils.isNotBlank(startDate)) {
            query.append("createdAt >= str_to_date(?, '%Y-%m-%d %H:%i:%s') and ");
            args.add(startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            query.append("createdAt <= str_to_date(?, '%Y-%m-%d %H:%i:%s') and ");
            args.add(endDate);
        }
        if (0 != resourceId) {
            query.append(" resource_id = ? and ");
            args.add(resourceId);
        }

        List<Resource> resourceList = Resource.findAll();
        if (query.length() > 0) {
            String sql = query.substring(0, query.lastIndexOf("and"));
            int count = (int) ResourceStore.count(sql, args.toArray());
            sql += " order by id desc";
            PageModel pageModel = new PageModel(count, page);
            List<Object> storeList = ResourceStore.find(sql, args.toArray()).fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);

            render(storeList, pageModel, resourceList);
        } else {
            int count = (int) ResourceStore.count();
            query.append(" order by id desc");
            PageModel pageModel = new PageModel(count, page);
            List<Object> storeList = ResourceStore.find(query.toString()).fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);

            render(storeList, pageModel, resourceList);
        }
    }

    public static void showResource(long resourceId) {
        ResourceStore store = ResourceStore.findById(resourceId);

        List<Resource> resourceList = Resource.findAll();
        render(store, resourceList);
    }

    public static void deleteResource(long resourceId) {
        ResourceStore resource = ResourceStore.findById(resourceId);
        resource.delete();

        resource.resource.totalCount = resource.resource.totalCount - resource.count;
        if (resource.resource.totalCount < 0) {
            resource.resource.totalCount = 0;
        }
        resource.resource.save();
        renderJSON(new Response());
    }

    public static void saveResource(ResourceStore store) {
        if (StringUtils.isBlank(store.totalPrice)) {
            store.totalPrice = ToolUtils.mul(store.costPrice, store.count);
        }
        if (store.id == null) {
            store.resource.totalCount = store.resource.totalCount + store.count;
            store.resource.costPrice = store.costPrice;
            store.resource.save();
        } else {
            store.resource.costPrice = store.costPrice;
            store.resource.save();
        }
        store.save();
        renderJSON(new Response());
    }
}
