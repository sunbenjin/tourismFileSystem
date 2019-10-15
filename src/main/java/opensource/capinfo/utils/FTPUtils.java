package opensource.capinfo.utils;

import java.io.File;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opensource.capinfo.config.ApplicationProperties;
import opensource.capinfo.config.FtpServerProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用于向FTP上传和下载的工具类
 * 
 * @author taolin
 * 
 */
public class FTPUtils {


	protected static Logger logger = LoggerFactory.getLogger(FTPUtils.class);

	private static FtpServerProperties ftpServer = new FtpServerProperties();

	public static String ftpUrl = ftpServer.getPath();

	public static final String TOUSU="tousu/";//投诉上传文件根目录
	public static final String SENQING="senqing/";//申请
	
	public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;
	public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;
	public static final String ISO_8859_1 = "ISO-8859-1";
	
	public static final long maxSize = 1024*5;

	public static final char FTP_PATH_CHAR = '/';

	/**
	 * 
	 * 方法download的作用:FTP下载 修改记录: [20120504_904032_增加存储路径]
	 *
	 *            请求信息 格式：ftp://user:password@ip:port/path/fileName
	 * @param storePath
	 *            文件存储路径
	 * @param maxSize
	 *            文件大小，允许ftp下载远程文件的最大值，单位为byte,超过此大小将不允许下载
	 * @return
	 * @throws Exception
	 * @Exception 异常对象
	 * @version 1.0[修改时小数点后+1]
	 */
	public static File download(String fileNamePath, String storePath, long maxSize)
			throws Exception {

		FtpConfInfo infoConf = getInfo(ftpUrl+fileNamePath);

		if (infoConf == null) {
			logger.error("构建FTP配置信息失败，请检查:" + ftpUrl+fileNamePath);
			return null;
		}
		FTPClient client = null;
		File file = null;
		try {
			client = connectServer(infoConf);
			if (client == null) {
				logger.error("构建FTP客户端失败");
				return null;
			}
			logger.debug("FTP服务器连接成功");
			if (infoConf.getLocation() != null) {
				String[] ss = infoConf.getLocation().split("/");
				for (String s : ss) {
					client.changeWorkingDirectory(s);
				}
			}
			logger.debug("FTP切换目录成功download()");
			String fileName = downFile(infoConf, storePath == null ? ""
					: storePath, client, infoConf.getFileName(), maxSize);

			if (fileName == null) {
				return null;
			}
			file = new File(fileName);
			if (!file.isFile()) {
				logger.error("下载的文件失败");
				return null;
			}
			logger.debug("文件下载成功准备重命名,file = " + fileName);
			String suffix = infoConf.getFileName().substring(
					infoConf.getFileName().lastIndexOf('.'));
			File tempFile = new File(storePath + File.separator
					+ UUID.randomUUID() + suffix);
			file.renameTo(tempFile);// 重命名保持唯一性
			file = tempFile;
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		} finally {
			// 关闭ftp
			closeServer(client);
		}
		return file;
	}
	
	
	/**
	 * 
	 * 方法download的作用:FTP下载 修改记录: [20120504_904032_增加存储路径]
	 *
	 *            请求信息 格式：ftp://user:password@ip:port/path/fileName
	 *            文件存储路径
	 * @param maxSize
	 *            文件大小，允许ftp下载远程文件的最大值，单位为byte,超过此大小将不允许下载
	 * @return
	 * @throws Exception
	 * @Exception 异常对象
	 * @version 1.0[修改时小数点后+1]
	 */
	public static OutputStream getOutPutStream(String fileNamePath, long maxSize)
			throws Exception {
		FtpConfInfo infoConf = getInfo(ftpUrl+fileNamePath);
		if (infoConf == null) {
			logger.error("构建FTP配置信息失败，请检查:" + ftpUrl+fileNamePath);
			return null;
		}
		FTPClient client = null;
		try {
			client = connectServer(infoConf);
			if (client == null) {
				logger.error("构建FTP客户端失败");
				return null;
			}

			logger.debug("FTP服务器连接成功");

			if (infoConf.getLocation() != null) {
				String[] ss = infoConf.getLocation().split("/");
				for (String s : ss) {
					client.changeWorkingDirectory(s);
				}
			}
			logger.debug("FTP切换目录成功download()");
			return downFile(infoConf, client, infoConf.getFileName(), maxSize);
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		} finally {
			// 关闭ftp
			closeServer(client);
		}
	}
	
	
	
	public static InputStream getInputStream(String fileNamePath, long maxSize)
			throws Exception {
		ByteArrayOutputStream op = (ByteArrayOutputStream)FTPUtils.getOutPutStream(fileNamePath, 10000);
		ByteArrayInputStream bis = new ByteArrayInputStream(op.toByteArray());
		return bis;
		
	}
	
	
	

	/**
	 * FTP批量下载文件到本地目录
	 * 
	 * @param localPath
	 *            本地目录
	 * @param ftpUrlList
	 *            FTP文件列表
	 * @return localFilePathList 本地文件路径列表
	 */
	public static List<String> downloadFiles(String localPath,
			List<String> ftpUrlList, long maxSize) throws Exception {
		List<String> localFilePathList = null;
		if (ftpUrlList != null && ftpUrlList.size() > 0) {
			localFilePathList = new ArrayList<String>();
			FtpConfInfo infoConf = getInfo(ftpUrlList.get(0));
			if (infoConf == null) {
				logger.error("构建FTP配置信息失败，请检查:" + ftpUrlList.get(0));
				return null;
			}
			FTPClient client = null;
			try {
				client = connectServer(infoConf);
				if (client == null) {
					logger.error("构建FTP客户端失败");
					return null;
				}
				for (String ftpUrl : ftpUrlList) {
					infoConf = getInfo(ftpUrl);
					if (infoConf.getLocation() != null) {
						String[] ss = infoConf.getLocation().split("/");
						for (String s : ss) {
							client.changeWorkingDirectory(s);
						}
					}
					String fileName = downFile(infoConf, localPath
							+ File.separator + infoConf.getLocation(), client,
							infoConf.getFileName(), maxSize);
					if (fileName != null) {
						localFilePathList.add(fileName);
					}
				}
			} catch (Exception e) {
				logger.error("", e);
				throw e;
			} finally {
				// 关闭ftp
				closeServer(client);
			}
		}
		return localFilePathList;
	}

	/**
	 * 判断是否有重名
	 * 
	 * @param ftpUrl
	 * @param newName
	 * @return
	 * @throws Exception
	 */
	public static boolean checkName(String ftpUrl, String newName)
			throws Exception {
		FtpConfInfo infoConf = getInfo(ftpUrl);
		FTPClient ftpclient = null;
		if (infoConf.getLocation() != null) {
			ftpclient = connectServer(infoConf);
			String[] ss = infoConf.getLocation().split("/");
			for (String s : ss) {
				if (!existDirectory(ftpclient, s)) {
					closeServer(ftpclient);
					return true;
				}
				ftpclient.changeWorkingDirectory(s);
			}
		}
		if (ftpclient == null)
			throw new Exception("FTP链接不成功，请检查FTP链接参数！");
		if (newName == null)
			throw new Exception("检查重名方法的参数newName不允许为空！");
		else
			newName = new String(newName.getBytes(infoConf.getEncoding()),
					FTPUtils.ISO_8859_1);

		FTPFile[] files = listFiles(ftpclient, newName);
		closeServer(ftpclient);

		if (files.length > 0) {
			return false;
		} else {
			return true;
		}
	}

	public static void clearPath(String ftpUrl, String newName)
			throws Exception {
		FtpConfInfo infoConf = getInfo(ftpUrl);
		FTPClient ftpclient = null;
		if (infoConf.getLocation() != null) {
			ftpclient = connectServer(infoConf);
			String[] ss = infoConf.getLocation().split("/");
			for (String s : ss) {
				if (!existDirectory(ftpclient, s)) {
					closeServer(ftpclient);
					return;
				}
				ftpclient.changeWorkingDirectory(s);
			}
		}
		if (ftpclient == null)
			throw new Exception("FTP链接不成功，请检查FTP链接参数！");
		if (newName == null)
			throw new Exception("删除文件方法的参数newName不允许为空！");
		else
			newName = new String(newName.getBytes(infoConf.getEncoding()),
					FTPUtils.ISO_8859_1);

		FTPFile[] files = listFiles(ftpclient, null);

		for (FTPFile file : files) {
			if (file.isFile()) {
				if (!file.getName().equals(newName))
					ftpclient.deleteFile(file.getName());
			}
		}
		closeServer(ftpclient);
	}

	/**
	 * 上载文件到远程FTP路径中
	 *
	 *  ftpUrl
	 *            eg: ftp://admin:admin@172.30.20.121:2121/poster/
	 * @param file
	 * @return [boolean] false=失败;true=成功.
	 */
	public static boolean upload(File file, String newName)
			throws Exception {
		InputStream is = null;
		if (!file.exists()) {
			logger.error("要上传的文件不存在");
			return false;
		}
		is = new FileInputStream(file);
		return upload(is, newName);
	}
	
	
	/**
	 * 上载文件到远程FTP路径中
	 * @return [boolean] false=失败;true=成功.
	 */
	public static boolean upload(InputStream is, String newName)
			throws Exception {
		FtpConfInfo infoConf = getInfo(ftpUrl+newName);
		if (infoConf == null) {
			logger.error("构建FTP配置信息失败，请检查:" + ftpUrl);
			return false;
		}
		FTPClient ftpclient = connectServer(infoConf);
		if (ftpclient == null) {
			logger.error("构建FTP客户端失败");
			return false;
		}
		if (newName == null)
			throw new Exception("上传文件方法的参数newName不允许为空！");
		else
			newName = new String(newName.getBytes(infoConf.getEncoding()),
					FTPUtils.ISO_8859_1);
		if (infoConf.getLocation() != null) {
		    //Matcher.quoteReplacement(File.separator)
			String[] ss = infoConf.getLocation().split("/");
			for (String s : ss) {
				if (!existDirectory(ftpclient, s))
					ftpclient.mkd(s);
				ftpclient.changeWorkingDirectory(s);
			}
		}
		boolean storeResult = false;
		try {
			storeResult = ftpclient.storeFile(infoConf.getFileName(), is);
			//System.out.println("========================="+storeResult);
			if (!storeResult) {
				ftpclient.enterLocalPassiveMode();
				storeResult = ftpclient.storeFile(infoConf.getFileName(), is);
			}
			logger.debug("uploadFTP的当前目录" + ftpclient.printWorkingDirectory());
			ftpclient.logout();
			closeServer(ftpclient);
			if (storeResult) {
				logger.info("文件传输到FTP成功");
			}else {
				logger.info("文件传输到FTP失败");
			}
			return storeResult;
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		} finally {
			if (is != null)
				is.close();
		}
	}
	
	public static boolean uploadFiles(MultipartFile[] files, List<String> fileUrlList) throws Exception {
		if (fileUrlList == null || fileUrlList.isEmpty()) {
			throw new Exception("上传文件方法的参数newName不允许为空！");
		}
		FtpConfInfo infoConf = getInfo(ftpUrl+fileUrlList.get(0));
		if (infoConf == null) {
			logger.error("构建FTP配置信息失败，请检查:" + ftpUrl);
			return false;
		}
		FTPClient ftpclient = connectServer(infoConf);
		if (ftpclient == null) {
			logger.error("构建FTP客户端失败");
			return false;
		}
		String newName = "";
		boolean storeResult = false;
		List<Boolean> resultFlagList = new ArrayList<>();
		InputStream is = null;

		boolean flag = false;
		try {
			for (int i = 0; i < files.length; i++) {
				ftpclient.changeWorkingDirectory("/");
				is = files[i].getInputStream();
				newName = fileUrlList.get(i);
				if (newName == null) {
					throw new Exception("上传文件方法的参数newName不允许为空！");
				} else {
					newName = new String(newName.getBytes(infoConf.getEncoding()),
							FTPUtils.ISO_8859_1);
				}
				String fileName = newName;
				String path = null;
				if (fileName.indexOf("/") > -1) {
					path = fileName.substring(0, fileName.lastIndexOf("/"));
					fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				}
				if (path != null) {
					String[] ss = path.split("/");
					for (String s : ss) {
						if (!existDirectory(ftpclient, s)){
							ftpclient.mkd(s);
						}
						ftpclient.changeWorkingDirectory(s);
					}
				}
				storeResult = ftpclient.storeFile(fileName, is);
				resultFlagList.add(storeResult);
				logger.debug("uploadFTP的当前目录" + ftpclient.printWorkingDirectory());
			}
			ftpclient.logout();
			closeServer(ftpclient);
			flag = true;
			for (Boolean bo : resultFlagList) {
				if (!bo) {
					flag = false;
					break;
				}
			}
			if (flag) {
				logger.info("文件传输到FTP成功");
			}else {
				logger.info("文件传输到FTP失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return flag;
	}



	/**
	 * 删除FTP上的文件
	 * 
	 * @param ftpUrl
	 *            eg：ftp://admin:admin@172.30.20.121:2121/IsmpPicture/201006/
	 *            201006154100845_1083136_jpg
	 * @return 删除海报失败
	 * @throws Exception
	 */
	public static boolean delete(String ftpUrl) throws Exception {
		boolean deleteResult = false;
		FtpConfInfo infoConf = getInfo(ftpUrl);
		if (infoConf == null) {
			logger.error("构建FTP配置信息失败，请检查:" + ftpUrl);
			return false;
		}
		FTPClient ftpclient = connectServer(infoConf);
		if (ftpclient == null) {
			logger.error("构建FTP客户端失败");
			return false;
		}
		if (infoConf.getFileName() != null) {
			if (infoConf.getLocation() != null) {
				deleteResult = ftpclient.deleteFile(infoConf.getLocation()
						+ "/" + infoConf.getFileName());
			} else {
				deleteResult = ftpclient.deleteFile(infoConf.getFileName());
			}
		}
		ftpclient.logout();
		closeServer(ftpclient);
		return deleteResult;
	}

	/**
	 * 获得FTP对象信息
	 * 
	 * @param ftpInfo
	 * @return FtpConfInfo
	 */
	private static FtpConfInfo getInfo(String ftpInfo) {
		if (ftpInfo == null) {
			return null;
		}

		String regEx = "^ftp://([\\w]+:[\\S]*@)?[\\S]+/[^\\/:*?\"<>|]*$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(ftpInfo);
		if (!m.find()) {
			return null;
		}

		String str = ftpInfo.substring("ftp://".length());
		String serverInfo = str.substring(0, str.indexOf("/"));
		String fileName = str.substring(str.indexOf("/") + 1);
		// if path exist
		String path = null;
		if (fileName.indexOf("/") > -1) {
			path = fileName.substring(0, fileName.lastIndexOf("/"));
			fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		}

		// 解析ftp用户名、密码、IP、端口号 特别是密码含有特殊字符的如：
		// "icdspics:ic:ds!@#@125.210.227.11:98"（lidahu）
		int k = serverInfo.indexOf(":");
		String serverName = serverInfo.substring(0, k);
		String server = serverInfo.substring(k + 1);
		int j = server.lastIndexOf("@");
		String serverPwd = server.substring(0, j);

		String serverIpPort = server.substring(j + 1);
		int i = serverIpPort.indexOf(":");
		String serverIp = null;
		String serverPort = null;
		if (i == -1) {
			serverIp = serverIpPort;
		} else {
			serverIp = serverIpPort.substring(0, i);
			serverPort = serverIpPort.substring(i + 1);
		}
		FtpConfInfo conf = new FtpConfInfo();

		conf.setUser(serverName);
		conf.setPassword(serverPwd);
		conf.setServer(serverIp);
		conf.setLocation(path);
		conf.setFileName(fileName);
		conf.setMaxWorkTime(60 * 1000l);// 默认60秒完成
		if (StringUtils.isNotEmpty(serverPort)) {
			try {
				conf.setPort(Integer.parseInt(serverPort));
			} catch (ClassCastException e) {
				// 设置默认端口 21
				conf.setPort(21);
			}

		} else {
			// 设置默认端口 21
			conf.setPort(21);
		}

		return conf;

	}

	/**
	 * Check the path is exist; exist return true, else false.
	 * 
	 * @param ftpClient
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private static boolean existDirectory(FTPClient ftpClient, String path)
			throws IOException {
		boolean flag = false;
		FTPFile[] listFiles;
		try {
			String Localpath = ftpClient.printWorkingDirectory();
			logger.debug(Localpath);
			listFiles = listFiles(ftpClient, null);
		} catch (Exception e) {
			logger.error("检查FTP路径发送错误", e);
			return false;
		}
		if (listFiles == null)
			return false;
		for (FTPFile ffile : listFiles) {
			boolean isFile = ffile.isFile();
			if (!isFile) {
				if (ffile.getName().equalsIgnoreCase(path)) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * (私有方法)连接FTP，返回FTPClient连接[返回根目录]，使用完连接后，调用closeServer关闭连接
	 * 
	 * @param conf
	 * @return FTPClient
	 * @throws SocketException
	 * @throws IOException
	 */
	private static FTPClient connectServer(FtpConfInfo conf)
			throws SocketException, IOException {
		FTPClient ftpClient = new FTPClient();
		//ftpClient.setConnectTimeout(5000);// 超时设置,建议采用配置
		ftpClient.setConnectTimeout(10 * 1000);
		ftpClient.setDataTimeout(10 * 1000);// 设置数据响应超时时间默认10秒

		try {
			ftpClient.connect(conf.getServer(), conf.getPort());
		} catch (SocketException e) {
			logger.error(
					"FTP服务器连接超时,请检查FTP服务器地址及端口配置是否正确:FTPServer["
							+ conf.getServer() + "]--Port[" + conf.getPort()
							+ "]", e);
			throw e;
		} catch (IOException e) {
			logger.error(
					"FTP服务器连接超时,请检查FTP服务器地址及端口配置是否正确:FTPServer["
							+ conf.getServer() + "]--Port[" + conf.getPort()
							+ "]", e);
			throw e;
		}

		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {

			if (ftpClient.login(conf.getUser(), conf.getPassword())) {
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				/** 解决中文问题的解码 903889 2010-12-30 Start **/
				ftpClient.sendCommand("FEAT");
				String featRS = ftpClient.getReplyString();
				String encoding = "GBK";
				if (featRS.indexOf(" UTF8") != -1)
					encoding = "UTF8";
				if (conf.getLocation() != null)
					conf.setLocation(new String(conf.getLocation().getBytes(
							encoding), FTPUtils.ISO_8859_1));
				if (conf.getFileName() != null)
					conf.setFileName(new String(conf.getFileName().getBytes(
							encoding), FTPUtils.ISO_8859_1));
				conf.setEncoding(encoding);// 将获取的编码信息带出来

				ftpClient.setActivePortRange(9000, 9100);// 主动模式下设置客户方端口范围9000-9100

				// ftpClient.enterLocalPassiveMode();//将默认的PORT主动模式改成PASV被动模式
				/** 解决中文问题的解码 End **/
				return ftpClient;
			}
			closeServer(ftpClient);
			logger.error("FTP的用户名和密码不对");
			logger.error(conf.toString());
			return null;
		}
		closeServer(ftpClient);
		logger.error("FTP的连接失败");
		logger.error(conf.toString());
		return null;
	}

	/**
	 * (私有方法)关闭FTP连接
	 * 
	 * @param ftpClient
	 * @throws IOException
	 */
	private static void closeServer(FTPClient ftpClient) throws IOException {
		if (ftpClient != null && ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

	/**
	 * 取得ftp文件
	 * 
	 * @param ftp
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private static FTPFile[] listFiles(FTPClient ftp, String fileName)
			throws IOException {
		FTPFile[] files = null;
		if (StringUtils.isNotEmpty(fileName)) {
			files = ftp.listFiles(fileName);
			if (files.length != 1) {
				// 如果没有不能下载文件，再用被动模式试一次（lidahu）
				ftp.enterLocalPassiveMode();
				files = ftp.listFiles(fileName);
			}
		} else {
			files = ftp.listFiles();
			// 以被动模式再试一次
			if (files.length == 0 || files == null) {
				ftp.enterLocalPassiveMode();
				files = ftp.listFiles();
			}
		}
		return files;
	}
	
	
	
	/**
	 * (私有方法) 下载文件
	 * 
	 * @param ftp
	 * @param fileName
	 * @param maxSize
	 *            为-1不检查文件大小
	 * @return String
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private static OutputStream downFile(FtpConfInfo ftpConfInfo,
			FTPClient ftp, String fileName, long maxSize)
			throws UnsupportedEncodingException, IOException, Exception {
		if (ftp == null || fileName == null || fileName.length() < 1) {
			logger.error("文件名为空或不存在");
			return null;
		}

		FTPFile[] files = listFiles(ftp, fileName);
		if (files.length != 1) {
			logger.error("|FileName:" + fileName
					+ "当前文件不存在或有多个,实际文件个数为:" + files.length);
			return null;
		}

		FTPFile file = files[0];
		if (!file.isFile()) {
			logger.error(file.getName() + "不是文件！");
			return null;
		}
		long lRemoteSize = file.getSize();
		// if (lRemoteSize > maxSize && maxSize != -1) {
		// log.error("要下载的文件太大，超过maxSize=" + maxSize + "chars");
		// return null;
		// }
		long maxTime = lRemoteSize / 1024 / 50;// 50K一秒,
		ftpConfInfo.setMaxWorkTime(maxTime * 1000);

		try {
			OutputStream output = new ByteArrayOutputStream();
			ftp.retrieveFile(fileName, output);
			return output;
			//output.close();
		} catch (Exception e) {
			throw new Exception("FTP传输失败，中断连接");
		} finally {
			FTPUtils.closeServer(ftp);
		}
	}
	
	
	
	

	/**
	 * (私有方法) 下载文件
	 * 
	 * @param ftp
	 * @param fileName
	 * @param maxSize
	 *            为-1不检查文件大小
	 * @return String
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private static String downFile(FtpConfInfo ftpConfInfo, String path,
			FTPClient ftp, String fileName, long maxSize)
			throws UnsupportedEncodingException, IOException, Exception {
		if (ftp == null || fileName == null || fileName.length() < 1) {
			logger.error("文件名为空或不存在");
			return null;
		}
		FTPFile[] files = listFiles(ftp, fileName);
		if (files.length != 1) {
			logger.error("PATH:" + path + "|FileName:" + fileName
					+ "当前文件不存在或有多个,实际文件个数为:" + files.length);
			return null;
		}
		FTPFile file = files[0];
		if (!file.isFile()) {
			logger.error(file.getName() + "不是文件！");
			return null;
		}
		long lRemoteSize = file.getSize();
		// if (lRemoteSize > maxSize && maxSize != -1) {
		// log.error("要下载的文件太大，超过maxSize=" + maxSize + "chars");
		// return null;
		// }
		long maxTime = lRemoteSize / 1024 / 50;// 50K一秒,
		ftpConfInfo.setMaxWorkTime(maxTime * 1000);

		if (path == null || path.equals("")) {
			String temp = System.getProperty("user.dir");
			path = temp;
		}
		File f = new File(path);
		if (!f.exists()) {
			boolean mkResult = f.mkdirs();
			if (!mkResult)
				logger.warn("make dir failed");
		}
		path = path + File.separator + fileName;

		try {
			File localFile = new File(path);
			OutputStream output = new FileOutputStream(localFile);
			ftp.retrieveFile(fileName, output);
			output.close();
		} catch (Exception e) {
			logger.error("下载文件" + path + "失败", e);
			throw new Exception("FTP传输失败，中断连接");
		} finally {
			FTPUtils.closeServer(ftp);
		}
		return path;
	}
	public static String repairFtpString(String ftpString) {
		if (StringUtils.isNotEmpty(ftpString)
				&& FTP_PATH_CHAR != ftpString.charAt(ftpString.length() - 1)) {
			return ftpString + FTP_PATH_CHAR;
		} else {
			return ftpString;
		}
	}

	public static void configInfo(FtpServerProperties ftpServer) {
		FTPUtils.ftpServer = ftpServer;
		FTPUtils.ftpUrl = ftpServer.getPath();
	}


	private FtpConfInfo getConfigByFileUrlStr() {


		return null;
	}

}
