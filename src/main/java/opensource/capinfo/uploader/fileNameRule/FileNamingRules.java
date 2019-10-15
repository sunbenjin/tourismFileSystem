package opensource.capinfo.uploader.fileNameRule;

import opensource.capinfo.entity.SysResourcesFilesEntity;

/**
 * @ClassName FileNamingRules
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/20 0020 23:33
 */
public interface FileNamingRules {


    /**
     *
     * @param builder 对象
     * @param FileName 文件名
     */
    void rules(SysResourcesFilesEntity.SysResourcesFilesEntityBuilder builder, String FileName);
}
