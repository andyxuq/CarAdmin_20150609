package controllers;

import models.store.Brand;
import models.store.Category;
import models.utils.PageModel;
import models.utils.Response;

import java.util.List;

/**
 * Created by xuqing on 2015/6/8.
 */
public class CategoryController extends Application{

    public static void getCategory(int page) {
        page = page == 0 ? 1 : page;

        int categorySize = (int) Category.count();
        PageModel pageModel = new PageModel(categorySize, page);

        List<Category> categoryList = Category.find("order by id desc").fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        render(categoryList, pageModel);
    }

    public static void deleteCategory(long categoryId) {
        Category category = Category.findById(categoryId);
        category.delete();
        renderJSON(new Response());
    }

    public static void showCategory(long categoryId) {
        Category category = Category.findById(categoryId);
        render(category);
    }

    public static void saveCategory(Category category) {
        category.save();
        renderJSON(new Response());
    }
}
