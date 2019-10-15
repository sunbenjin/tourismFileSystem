package opensource.capinfo.entity;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * 
 * @ClassName:  MessageCode   统一Ajax对外传输接口封装的内容
 * @Description:TODO 用于ajax想歪   
 * @author: litaolin
 * @date:   2018-4-25 下午3:53:45   
 *     
 * @Copyright: 2018 www.capinfo.com.cn Inc. All rights reserved.
 */
public class MessageCode {
	
	private final static String FLAG_SUCCESS = "success";  //执行成功
	private final static String FLAG_FAIL = "fail"; //执行失败
	private final static String MSG_SUCCESS = "%s操作成功";
	private final static String MSG_FAIL = "%s操作失败!%s";	
	
	/**
	 * 用于保存成功还是失败
	 */
	@Expose
	private String flag;//
	
	/**
	 * 
	 */
	@Expose
	private String msg;//
	@Expose
	private Object data;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	//工具
	public String setSuccessMessage(Object obj,String msg){
		this.flag = FLAG_SUCCESS;	
		this.msg = String.format(MSG_SUCCESS,msg!=null?"":msg);
		this.data = obj;
		return this.builder();
	}
	
	public String setfailMessage(String msg,String exceptionMessage){
		this.flag = FLAG_FAIL;
		this.msg = String.format(MSG_FAIL,msg!=null?"":msg,exceptionMessage!=null?"":exceptionMessage);
		this.data = new Object();
		return this.builder();
	}

	
	public String builder(){
		GsonBuilder builder = new GsonBuilder();  
		builder.excludeFieldsWithoutExposeAnnotation();  
		Gson gson = builder.create();  
		return gson.toJson(this);
	}

}
