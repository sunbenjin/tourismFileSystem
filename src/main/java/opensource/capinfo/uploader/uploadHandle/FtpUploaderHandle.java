package opensource.capinfo.uploader.uploadHandle;

import opensource.capinfo.utils.FTPUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName FtpUploaderHandle
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/27 0027 15:39
 */
public class FtpUploaderHandle implements UploaderHandle {

    private Lock lock = new ReentrantLock();

    /**
     *
     * @param mpFile
     * @return 文件处理
     */
    @Override
    public boolean handle(MultipartFile mpFile, String fileUrl) {
        boolean upload = false;
        try {
            lock.lock();
            upload = FTPUtils.upload(mpFile.getInputStream(),fileUrl);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return upload;
    }

    @Override
    public boolean handleMultipleFiles(MultipartFile[] files, List<String> fileUrlList) {
        boolean upload = false;
        try {
            lock.lock();
            upload = FTPUtils.uploadFiles(files, fileUrlList);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return upload;
    }


    @Override
    public synchronized boolean handle(File file, String fileUrl) {
        boolean upload = false;
        try {
            upload = FTPUtils.upload(file,fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upload;
    }
}
