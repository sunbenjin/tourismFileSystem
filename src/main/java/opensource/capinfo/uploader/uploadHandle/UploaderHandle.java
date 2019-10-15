package opensource.capinfo.uploader.uploadHandle;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 *
 *  上传的时候使用
 *
 */
public interface UploaderHandle {
    /**
     *
     */
    public boolean handle(MultipartFile mpFile, String fileUrl);

    public boolean handleMultipleFiles(MultipartFile[] files, List<String> fileUrlList);

    public boolean handle(File file, String fileUrl);
}
