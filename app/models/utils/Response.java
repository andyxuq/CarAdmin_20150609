package models.utils;

/**
 * User: xuqing
 * Date: 15-3-10
 * Time: 下午2:44
 */
public class Response {
    /**
     * 返回代码，默认只有0是成功
     */
    private int code;

    private String msg;

    public Response() {
        this.msg = "success";
    }

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
