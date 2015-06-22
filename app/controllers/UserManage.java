package controllers;

import models.User;
import models.client.CarRecord;
import models.utils.PageModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import play.data.validation.Validation;
import play.mvc.Controller;
import play.mvc.Http;

import java.util.List;

/**
 * User: xuqing
 * Date: 15-3-4
 * Time: 上午9:59
 */
public class UserManage extends Application {

    private static final String COOKIE_AGE = "7d";

    public static void doLogin(User userInfo) {
        String remember = params.get("remember_me");
        System.out.println(remember + "=>" + userInfo.toString());

        int page = NumberUtils.toInt(params.get("page"));
        page = page == 0 ? 1 : page;
        User dbUser = checkUserPassword(userInfo);
        if (null != dbUser) {
            userInfo = dbUser;
            session.put(SESSION_USER_KEY, userInfo.getUserString());
            if ("on".equals(remember)) {
                setCookie(response, COOKIE_USER_KEY, userInfo.getUserString(), COOKIE_AGE);
            }

//            int count = (int) CarRecord.count("userId = ?", userInfo.getId());
//            PageModel pageModel = new PageModel(count, page);
//            List<CarRecord> recordList = CarRecord.find("userId = ? order by id desc", userInfo.getId()).fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
//            int finishNum = 0, workNum = 0;
//            for (CarRecord record : recordList) {
//                if (record.backDate == null) {
//                    workNum++;
//                } else {
//                    finishNum++;
//                }
//            }
//            render(userInfo, recordList, finishNum, workNum, pageModel);
            showMainPage(0);
        } else {
            Validation.addError("userName", "用户名或密码不正确");
            renderTemplate("Application/login.html");
        }
    }

    public static void showMainPage(int page) {
        page = page == 0 ? 1 : page;
        String userSession = session.get(Application.SESSION_USER_KEY);
        User userInfo = getUserFromSession(userSession);
        userInfo = checkUserPassword(userInfo);

        int count = (int) CarRecord.count("userId = ?", userInfo.getId());
        PageModel pageModel = new PageModel(count, page);
        List<CarRecord> recordList = CarRecord.find("userId = ? order by id desc", userInfo.getId()).fetch(pageModel.getCurrentPage(), PageModel.DEFAULT_PAGES);
        int finishNum = 0, workNum = 0;
        for (CarRecord record : recordList) {
            if (record.backDate == null) {
                workNum++;
            } else {
                finishNum++;
            }
        }
        renderTemplate("UserManage/doLogin.html", userInfo, recordList, finishNum, workNum, pageModel);
    }

    public static void doLoginOut() {
        session.remove(SESSION_USER_KEY);
//        renderTemplate();
        redirect("/?out=1");
    }
}
