package opensource.capinfo.uploader.jsonTransform;

import opensource.capinfo.entity.FileOutUploader;
import opensource.capinfo.entity.SysResourcesFilesEntity;
import opensource.capinfo.uploader.transformToMp4.ConverterToMp4;

import java.util.List;

/**
 * @ClassName JsonConverter
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/20 0020 22:07
 */
public interface JsonConverter {

   public FileOutUploader outData(List<SysResourcesFilesEntity> entity, ConverterToMp4 isConverterMp4);
}
