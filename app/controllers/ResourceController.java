package controllers;

import models.store.Brand;
import models.store.Category;
import models.store.Resource;
import models.utils.PageModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import play.db.jpa.JPABase;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuqing on 2015/6/10.
 */
public class ResourceController extends Controller{

    public static void getResources(int page) {
        page = page == 0 ? 1 : page;

        long categoryId = NumberUtils.toLong(params.get("categoryId"));
        long brandId = NumberUtils.toLong(params.get("brandId"));
        String type = params.get("type");

        StringBuffer query = new StringBuffer();
        List<Object> args = new ArrayList<Object>();
        if (0 != categoryId) {
            query.append("category_id = ? and ");
            args.add(categoryId);
        }
        if (0 != brandId) {
            query.append("brand_id = ? and ");
            args.add(brandId);
        }
        if (StringUtils.isNotBlank(type)) {
            query.append(" type like ? and ");
            args.add("%" + type + "%");
        }

        List<Brand> brandList = Brand.findAll();
        List<Category> categoryList = Category.findAll();
        if (query.length() > 0) {
            query.substring(0, query.lastIndexOf("and"));
            int count = (int) Resource.count(query.toString(), args.toArray());
            query.append(" order by id desc");
            PageModel pageModel = new PageModel(count, page);
            List<Object> resourceList = Resource.find(query.toString(), args.toArray()).fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);

            render(resourceList, pageModel, brandList, categoryList);
        } else {
            int count = (int) Resource.count();
            query.append(" order by id desc");
            PageModel pageModel = new PageModel(count, page);
            List<Object> resourceList = Resource.find(query.toString()).fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);

            render(resourceList, pageModel, brandList, categoryList);
        }

    }
}
