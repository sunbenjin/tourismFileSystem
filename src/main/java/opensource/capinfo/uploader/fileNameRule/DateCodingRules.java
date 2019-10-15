package opensource.capinfo.uploader.fileNameRule;

import opensource.capinfo.entity.SysResourcesFilesEntity;

/**
 * @ClassName DateCodingRules 根据时间规则存储文件
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/20 0020 23:39
 */
public abstract class DateCodingRules implements FileNamingRules {


    @Override
    public void rules(SysResourcesFilesEntity.SysResourcesFilesEntityBuilder builder, String fileName) {
        this.splitFileAllName(builder,fileName);
        builder.fileUrl(this.createNewDateBaseFilePath());
    }

    /**
     * @param builder
     * @param fileName
     * @return
     */

    //获取文件前缀和后缀
    public abstract void splitFileAllName(SysResourcesFilesEntity.SysResourcesFilesEntityBuilder builder,String fileName);
    //新建文件路径规则
    public abstract String createNewDateBaseFilePath();

}
