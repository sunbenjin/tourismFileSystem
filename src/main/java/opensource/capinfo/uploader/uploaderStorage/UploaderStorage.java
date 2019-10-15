package opensource.capinfo.uploader.uploaderStorage;

import opensource.capinfo.entity.FileInputParams;
import opensource.capinfo.entity.SysResourcesFilesEntity;

/**
 * @ClassName UploaderStorage
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/20 0020 22:04
 */
public interface UploaderStorage {





    /**
     * 传进来的参数，保存到相应的什么位置
     * @param entity
     * @param fileInputParams
     */
    public void save(SysResourcesFilesEntity entity, FileInputParams fileInputParams);


    /**
     * 点击保存按钮时候，保存对应数据的业务表id和delflag
     * @param entity
     */
    public void updateFileData(SysResourcesFilesEntity entity);


}
