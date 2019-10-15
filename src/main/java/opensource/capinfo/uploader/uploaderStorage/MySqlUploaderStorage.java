package opensource.capinfo.uploader.uploaderStorage;

import opensource.capinfo.entity.FileInputParams;
import opensource.capinfo.entity.SysResourcesFilesEntity;
import opensource.capinfo.service.SysResourcesFilesService;
import opensource.capinfo.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @ClassName MySqlUploaderStorage
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/27 0027 16:18
 */
public class MySqlUploaderStorage implements UploaderStorage{

    private SysResourcesFilesService sysResourcesFilesService = (SysResourcesFilesService)SpringUtil.getBean("sysResourcesFilesService");


    /**
     *
     * @param entity
     * @param fileInputParams
     */
    @Override
    public void save(SysResourcesFilesEntity entity, FileInputParams fileInputParams) {
        entity.setId(fileInputParams.getKey());
                entity.setBusiId(fileInputParams.getBusiId());
        entity.setFilesDynCode(fileInputParams.getFilesDynCode());
        entity.setFileUniqueCode(fileInputParams.getFileUniqueCode());
        entity.setSysId(fileInputParams.getTableName());
        ;//就是主键ID
        String fileUrl = entity.getFileUrl();
        entity.setFileUrl(StringUtils.isNotBlank(fileUrl)?(fileUrl.replaceAll("\\\\", File.separator)):"");
        sysResourcesFilesService.save(entity);

    }

    @Override
    public void updateFileData(SysResourcesFilesEntity entity) {
        sysResourcesFilesService.save(entity);
    }
}
