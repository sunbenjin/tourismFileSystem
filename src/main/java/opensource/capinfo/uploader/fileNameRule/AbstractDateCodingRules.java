package opensource.capinfo.uploader.fileNameRule;

import opensource.capinfo.entity.SysResourcesFilesEntity;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName DateCodingRules 根据时间规则存储文件
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/20 0020 23:39
 */
public abstract class AbstractDateCodingRules implements FileNamingRules {


    @Override
    public void rules(SysResourcesFilesEntity.SysResourcesFilesEntityBuilder builder, String fileName) {
        this.splitFileAllName(builder,fileName);
        //获取文件后缀名
        String suffix = builder.build().getFileSuffix();
        if(StringUtils.isNotBlank(suffix)){
            builder.fileUrl(this.createNewDateBaseFilePath(suffix));
        }
    }

    /**
     * @param builder
     * @param fileName
     * @return
     */

    //获取文件前缀和后缀
    public abstract void splitFileAllName(SysResourcesFilesEntity.SysResourcesFilesEntityBuilder builder,String fileName);
    //新建文件路径规则
    public abstract String createNewDateBaseFilePath(String suffix);

}
