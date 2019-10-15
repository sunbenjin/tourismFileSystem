package opensource.capinfo.uploader.fileNameRule;

import lombok.extern.slf4j.Slf4j;
import opensource.capinfo.entity.InitialPreviewConfig;
import opensource.capinfo.entity.SysResourcesFilesEntity;
import opensource.capinfo.utils.RandomFileUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName SimpleDateCodingRules
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/21 0021 0:48
 */
@Slf4j
public class SimpleDateCodingRules extends AbstractDateCodingRules{
    /**
     *
     * @param builder
     * @param fileName
     */
    @Override
    public void splitFileAllName(SysResourcesFilesEntity.SysResourcesFilesEntityBuilder builder, String fileName) {
        if (StringUtils.isNotBlank(fileName)) {
            int num = fileName.lastIndexOf(".");
            String fileSuffix = fileName.substring(num+1, fileName.length());
            builder.fileSuffix(fileSuffix);
            log.debug("文件后缀名为:"+fileSuffix);
            String fileType = showFileType(fileSuffix);
            builder.fileType(fileType);
            log.debug("文件类型为:"+fileType);
            builder.fileName(fileName.substring(0,num));
            log.debug("文件名为:"+fileName);
        }
    }

    /**
     * 判断文件的类型（FileInput默认的类型）
     * @param suffix
     * @return
     */
    private String showFileType(String suffix) {
        List<InitialPreviewConfig.InitialPreviewFileType> enumList =
                EnumUtils.getEnumList(InitialPreviewConfig.InitialPreviewFileType.class);
        Optional<InitialPreviewConfig.InitialPreviewFileType> doc = enumList.stream().filter(v -> {
            if (v.getSuffix().contains(suffix)) {
                return true;
            }
            return false;
        }).findFirst();
        if (doc.isPresent()){
            return doc.get().getType();
        }
        return "";
    }


    /**
     * 数据库路径（文件路径日期方式生成）
     * @return 返回路径信息
     */
    @Override
    public String createNewDateBaseFilePath(String suffix) {
        Date date = new Date();
        String path=new SimpleDateFormat("yyyy/MM/dd").format(date);
        String[] paths=path.split("/");
        StringBuffer mosaic = new StringBuffer("");
        for (String childPath : paths) {
            mosaic.append(childPath+ "/");
        }
        mosaic.deleteCharAt(mosaic.length()-1);
        return mosaic.toString()+"/"+ RandomFileUtils.getRandomFileName()+"."+suffix;
    }




}
