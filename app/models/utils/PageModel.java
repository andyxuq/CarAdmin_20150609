package models.utils;

import play.db.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: xuqing
 * Date: 15-3-10
 * Time: 上午10:23
 */
public class PageModel {

    //默认每页显示条数
    public static final int DEFAULT_PAGES = 10;

    /** 当前页 */
    private int currentPage;
    /** 总页数 */
    private int totalPage;
    /** 总条数 */
    private int count;

    private Map<String, String> search;

    public PageModel(int count, int currentPage) {
        this.count = count;
        this.currentPage = currentPage;

        if (count > DEFAULT_PAGES) {
            this.totalPage = count % DEFAULT_PAGES == 0 ? count / DEFAULT_PAGES : (count / DEFAULT_PAGES + 1);
        } else {
            this.totalPage = 1;
        }
        if (this.currentPage > this.totalPage) {
            this.currentPage = totalPage;
        }
    }

    public void putSearch(String key, String value) {
        if (null == search) {
            search = new HashMap<String, String>();
        }
        search.put(key, value);
    }

    public String getSearchValue(String key) {
        if (null == search) {
            return "";
        } else {
            return search.get(key) == null ? "" : search.get(key);
        }
    }

    public String getSearch() {
        if (null == search) {
            return "";
        } else {
            StringBuffer searchBuilder = new StringBuffer();
            for (Map.Entry<String, String> entry : search.entrySet()) {
                searchBuilder.append("&" + entry.getKey() + "=" + entry.getValue());
            }
            return searchBuilder.toString();
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private List<? extends Model> dataList;

    public List<? extends Model> getDataList() {
        return dataList;
    }

    public void setDataList(List<? extends Model> dataList) {
        this.dataList = dataList;
    }


    private int finished;
    private int process;

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }
}
