package opensource.capinfo.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * 组件接口调用类
 * @author Administrator
 *
 */
public class FileinputUtils {

	/**
	 * 远程下载文件到本地
	 * @param remoteFilePath
	 * @param localFilePath
	 */
	public static void downloadFile(String remoteFilePath,String localFilePath) {
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File f = new File(localFilePath);
        if(!f.exists()) {
        	try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        try
        {
            urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection)urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1)
            {
                bos.write(b, 0, len);
            }
//            System.out.println("上传成功");
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
        	if(bis!=null) {
        		
        		   try
                   {
        			   bis.close();
                       
                   }
                   catch (IOException e)
                   {
                       e.printStackTrace();
                   }
        	}
        	if(bos !=null) {
        		   try
                   {
        			   bos.close();
                       
                   }
                   catch (IOException e)
                   {
                       e.printStackTrace();
                   }
        		
        	}
         
        }
 
	}

}
