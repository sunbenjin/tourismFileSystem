package opensource.capinfo.utils;
/**
 * ftp基本配置参数
 * @author taolin
 *
 */
public class FtpConfInfo {
	
	
	private String user;
	private String password;
	private String server;
	private String location;
	private String fileName;
	private long maxWorkTime;
	private int port;
	private String encoding;
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getMaxWorkTime() {
		return maxWorkTime;
	}
	public void setMaxWorkTime(long maxWorkTime) {
		this.maxWorkTime = maxWorkTime;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}


	

}
