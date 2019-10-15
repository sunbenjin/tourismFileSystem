package opensource.capinfo.uploader.transformToMp4;

import opensource.capinfo.entity.SysResourcesFilesEntity;
import opensource.capinfo.uploader.uploadHandle.UploaderHandle;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName ConverterMp4
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/20 0020 22:11
 */
public interface ConverterMultiMedia {

    /**
     * 是否转化
     * @return
     */
    public boolean isConverter();

    /**
     * 转化类型
     * @return
     */
    public String converterTypes();

    /**
     * 将转化后的mp4
     * @param uploaderHandle
     */
    public void localFileSave(UploaderHandle uploaderHandle);




    /**
     *
     * @return 是否转化成功
     */
    public void converter(SysResourcesFilesEntity entity, MultipartFile file);

}
