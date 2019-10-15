package opensource.capinfo.utils;


import java.io.Serializable;

public class ResultData implements Serializable {

    private boolean flag;

    private Integer code;

    private String msg;

    private Object data;


    public ResultData() {
    }

    public ResultData(boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    public ResultData(boolean flag, Integer code, String msg) {
        this.flag = flag;
        this.code = code;
        this.msg = msg;
    }
    public ResultData(boolean flag, Integer code, String msg,Object data) {
        this.flag = flag;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResultData error(String msg){
        return new ResultData(false, 500,msg);
    }
    public  static ResultData sucess(String msg){
        return new ResultData(true, 200,msg);
    }
    public static ResultData sucess(String msg,Object data){
        return new ResultData(true,200,msg,data);
    }
}
