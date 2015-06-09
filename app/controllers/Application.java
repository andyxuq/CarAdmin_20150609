package controllers;

import models.client.CarRecord;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.*;
import play.*;
import play.Logger;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    protected static final String SESSION_USER_KEY = "user-serssion";
    protected static final String COOKIE_USER_KEY = "user-cookie";
    public static final String SESSION_KEY_SPLITER = ":~:";

    protected static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("Rolling");

    @Before(unless = {"login", "UserManage.doLogin", "UserManage.doLoginOut"})
    static void checkUser() {
        String userInfo = session.get(SESSION_USER_KEY);
        if (StringUtils.isBlank(userInfo)) {
            login();
        }
    }

    static User checkUserPassword(User user) {
        User dbUser = User.find("userName = ? and password = ?", user.userName, user.passWord).first();
        if (null == dbUser) {
            return null;
        } else {
            return dbUser;
        }
    }

    public static void login() {
        String userSession = session.get(Application.SESSION_USER_KEY);
        if (StringUtils.isNotBlank(userSession)) {
            User userInfo = getUserFromSession(userSession);

            User dbUser = checkUserPassword(userInfo);
            if (null != dbUser) {
                userInfo = dbUser;
                session.put(SESSION_USER_KEY, userInfo.getUserString());

                List<CarRecord> recordList = CarRecord.find("userId = ? order by id desc", userInfo.getId()).fetch();
                int finishNum = 0, workNum = 0;
                for (CarRecord record : recordList) {
                    if (record.backDate == null) {
                        workNum++;
                    } else {
                        finishNum++;
                    }
                }
                renderTemplate("UserManage/doLogin.html", userInfo, recordList, finishNum, workNum);
            }
        }

        String cookieVal = getCookieVal(request, COOKIE_USER_KEY);
        if (StringUtils.isNotBlank(cookieVal)) {
            User userInfo = getUserFromSession(cookieVal);

            User dbUser = checkUserPassword(userInfo);
            if (null != dbUser) {
                userInfo = dbUser;
                session.put(SESSION_USER_KEY, userInfo.getUserString());
                List<CarRecord> recordList = CarRecord.find("userId = ? order by id desc", userInfo.getId()).fetch();
                int finishNum = 0, workNum = 0;
                for (CarRecord record : recordList) {
                    if (record.backDate == null) {
                        workNum++;
                    } else {
                        finishNum++;
                    }
                }
                renderTemplate("UserManage/doLogin.html", userInfo, recordList, finishNum, workNum);
            }
        } else {
            render();
        }
    }

    static User getUserFromSession(String userString) {
        String[] userArray = userString.split(SESSION_KEY_SPLITER);

        Map<String, Object> params = new HashMap<String, Object>();
        User userInfo = new User();
        userInfo.userName = userArray[0];
        userInfo.passWord = userArray[1];
        userInfo.isAdmin = NumberUtils.toInt(userArray[2]);

        return userInfo;
    }

    //设置Cookie
    static void setCookie(Http.Response response, String name, String value, String duration) {
        if (StringUtils.isNotBlank(duration)) {
            response.setCookie(name, value, duration);
        } else {
            response.setCookie(name, value);
        }
    }

    //获取Cookie的值
    static String getCookieVal(Http.Request request, String name) {
        return request.cookies.get(name) == null ? "" : String.valueOf(request.cookies.get(name).value);
    }
}