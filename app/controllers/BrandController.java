package controllers;

import models.store.Brand;
import models.utils.PageModel;
import models.utils.Response;
import play.db.jpa.JPABase;

import java.util.List;

/**
 * Created by xuqing on 2015/6/8.
 */
public class BrandController extends Application{

    public static void getBrand(int page) {
        page = page == 0 ? 1 : page;

        int brandSize = (int) Brand.count();
        PageModel pageModel = new PageModel(brandSize, page);

        List<Brand> brandList = Brand.find("order by id desc").fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        render(brandList, pageModel);
    }

    public static void deleteBrand(long brandId) {
        Brand brand = Brand.findById(brandId);
        brand.delete();
        renderJSON(new Response());
    }

    public static void showBrand(long brandId) {
        Brand brand = Brand.findById(brandId);
        render(brand);
    }

    public static void saveBrand(Brand brand) {
        brand.save();
        renderJSON(new Response());
    }
}
