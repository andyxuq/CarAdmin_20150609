package utils;

import controllers.Application;
import models.utils.PageModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import play.Play;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: xuqing
 * Date: 15-3-5
 * Time: 下午2:15
 */
public class ToolUtils {

    private static final String UPLOAD_PATH = Play.configuration.getProperty("upload.path");

    public static String getDateToday(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date());
    }

    public static Date getDate(String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(getDateToday(format));
    }

    public static String getDate(Date date, String format) {
        if (null != date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        } else {
            return "";
        }
    }

    public static String mul(String price, int num) {
        double doublePrice = NumberUtils.toDouble(price);
        return mul(doublePrice, num);
    }

    public static String mul(double doublePrice, int num) {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(doublePrice));
        BigDecimal totalNum = new BigDecimal(Double.toString(num));

        return String.valueOf(bigDecimal.multiply(totalNum));
    }

    /**
     * 保存文件到指定目录
     * @param uploadFile 保存指定文件
     * @return
     * @throws IOException
     */
    public static String saveFile(File uploadFile) throws IOException {
        String uploadPath = UPLOAD_PATH;
        String logoPath = Play.configuration.getProperty("upload.img.path");

        String subfix = uploadFile.getName().substring(uploadFile.getName().lastIndexOf(".") + 1);
        StringBuffer filePath = new StringBuffer();
        filePath.append(logoPath);
        filePath.append("/" + getDateToday("yyyy/MM/dd"));
        filePath.append("/" + System.currentTimeMillis());
        filePath.append("." + subfix);

        FileUtils.moveFile(uploadFile, new File(uploadPath + filePath.toString()));
        return filePath.toString();
    }

    /**
     * 删除指定位置的文件
     * @param filePath
     */
    public static void deleteFileByPath(String filePath) {
        if (StringUtils.isNotBlank(filePath)) {
            String deletePath = UPLOAD_PATH + filePath;
            FileUtils.deleteQuietly(new File(deletePath));
        }
    }
}
