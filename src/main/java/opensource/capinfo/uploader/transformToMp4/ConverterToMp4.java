package opensource.capinfo.uploader.transformToMp4;

import opensource.capinfo.config.ApplicationProperties;
import opensource.capinfo.entity.SysResourcesFilesEntity;
import opensource.capinfo.uploader.uploadHandle.UploaderHandle;
import opensource.capinfo.utils.ChangeVideo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName ConverterToMp4  转化多媒体为MP4格式
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/21 0021 8:43
 */
public class ConverterToMp4 implements ConverterMultiMedia {

    private static ApplicationProperties props;

    private UploaderHandle uploaderHandle;

    public static void setProps(ApplicationProperties props) {
        ConverterToMp4.props = props;
    }

    @Override
    public boolean isConverter() {
        return props.isConverMp4();
    }


    @Override
    public String converterTypes() {
        return props.getConverType();
    }

    /**
     * 如果需要存储则需要调用这个
     * @param uploaderHandle
     */
    @Override
    public void localFileSave(UploaderHandle uploaderHandle) {
        this.uploaderHandle = uploaderHandle;
    }

//    @Override
//    public String tempFileGenerateStrategy() {
//        Date date = new Date();
//        String path=new SimpleDateFormat("yyyy/MM/dd").format(date);
//        String[] paths=path.split("/");
//        StringBuffer mosaic = new StringBuffer("");
//        for (String childPath : paths) {
//            mosaic.append(childPath+ File.separator);
//        }
//        mosaic.deleteCharAt(mosaic.length()-1);
//        return mosaic.toString()+File.separator+ RandomFileUtils.getRandomFileName()+"."+"mp4";
//    }

    @Override
    public void converter(SysResourcesFilesEntity entity, MultipartFile file) {
        if(StringUtils.isNotBlank(converterTypes())){
            if(StringUtils.contains(converterTypes(), entity.getFileSuffix())) {
                //保存到临时路径中
                //String tempPath = tempFileGenerateStrategy();
                String tempPath = entity.getFileUrl().replaceAll(entity.getFileSuffix(),"mp4");
                String inputPath = props.getTempPath()+"/"+tempPath;
                String destPath = props.getTempPath()+"/"+entity.getFileUrl();
                try {
                    if (destPath.indexOf("/") > -1) {
                        FileUtils.forceMkdir(new File(destPath.substring(0, destPath.lastIndexOf("/"))));
                    }
                    File localFile = FileUtils.getFile(destPath);
                    File inputFile = FileUtils.getFile(inputPath);
                    file.transferTo(localFile);
                    ChangeVideo.convert(destPath,inputPath);
                    if(uploaderHandle!=null){//不上传
                        uploaderHandle.handle(inputFile,tempPath);
                    }
//                    if(localFile.exists()) {
//                        localFile.delete();
//                    }
//                    if(inputFile.exists()) {
//                        inputFile.delete();
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
