package controllers;

import models.User;
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
public class AdminUser extends Application {

    public static void showAdminUser(int page) {
        if (0 == page) {
            page = 1;
        }
        int userCount = 0;
        List<User> userList = null;
        PageModel pageModel = null;

        String phone = params.get("phone");
        String userName = params.get("nickName");
        if (StringUtils.isNotBlank(phone)) {
            userCount = (int) User.count("phone like ?", "%" +phone+ "%");
            pageModel = new PageModel(userCount, page);
            userList = User.find("phone like ? order by id desc", "%" +phone+ "%")
                    .fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        } else if (StringUtils.isNotBlank(userName)) {
            userCount = (int) User.count("nickName like ?", "%" +userName+ "%");
            pageModel = new PageModel(userCount, page);
            userList = User.find("nickName like ? order by id desc", "%" +userName+ "%")
                    .fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        } else {
            userCount = (int) User.count();
            pageModel = new PageModel(userCount, page);
            userList = User.find("order by id desc")
                    .fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        }

        render(userList, pageModel);
    }

    public static void addUserInfo(long userId) {
        User user = null;
        if (0 != userId) {
            user = User.findById(userId);
            render(user);
        } else {
            render(user);
        }
    }

    public static void saveUserInfo(User user, File userLogo) {
        if (null != userLogo && StringUtils.isNotBlank(user.logo)) {
            ToolUtils.deleteFileByPath(user.logo);
        }
        if (null != userLogo) {
            try {
                String logoPath = ToolUtils.saveFile(userLogo);
                user.logo = logoPath;
            } catch (IOException e) {
                e.printStackTrace();
                renderJSON(new Response(1, "保存用户头像失败"));
            }
        }

        user.save();
        renderJSON(new Response(0, "成功"));
    }

    public static void deleteUserInfo(long userId) {
        User user = User.findById(userId);
        if (StringUtils.isNotBlank(user.logo)) {
            ToolUtils.deleteFileByPath(user.logo);
        }
        user.delete();

        renderJSON(new Response(0, "成功"));
    }
}
