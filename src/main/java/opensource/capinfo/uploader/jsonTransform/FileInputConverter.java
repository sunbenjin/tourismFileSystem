package opensource.capinfo.uploader.jsonTransform;

import opensource.capinfo.config.ApplicationProperties;
import opensource.capinfo.entity.*;
import opensource.capinfo.uploader.transformToMp4.ConverterToMp4;
import opensource.capinfo.utils.FileOutBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FileInputConverter
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/27 0027 17:04
 */
public class FileInputConverter implements JsonConverter {

    private static ApplicationProperties props;

    public static void setProps(ApplicationProperties props) {
        FileInputConverter.props = props;
    }

    @Override
    public FileOutUploader outData(List<SysResourcesFilesEntity> list, ConverterToMp4 isConverterMp4) {

        FileOutBuilder fileOutBuilder = new FileOutBuilder();
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(srfe -> {
                //外部资源服务器的绝对路
                srfe.setFileUrl(props.getDisplayPath()+srfe.getFileUrl());
                if(isConverterMp4.isConverter()){
                    if(StringUtils.contains(isConverterMp4.converterTypes(), srfe.getFileSuffix())) {
                        String tempPath = srfe.getFileUrl().replaceAll(srfe.getFileSuffix(), "mp4");
                        srfe.setFileUrl(tempPath);
                        srfe.setMimeType("video/mp4");
                    }
                }
                fileOutBuilder.builder(srfe);
            });
        }
        List<InitialPreviewConfig> newList = new ArrayList<InitialPreviewConfig>();
        for (int i = 0; i < fileOutBuilder.getConfigList().size(); i++) {
            InitialPreviewConfig initialPreviewConfig = fileOutBuilder.getConfigList().get(i);
            InitialPreview urlEntity = fileOutBuilder.getIpList().get(i);
            if (urlEntity != null && StringUtils.isNotBlank(urlEntity.getData())) {
                initialPreviewConfig.setFileHttpUrl(urlEntity.getData());
            }
            newList.add(initialPreviewConfig);
        }
        FileOutInfo fileOutInfo = new FileOutInfo();
        //fileOutInfo.setInitialPreviewConfig(fileOutBuilder.getConfigList());
        fileOutInfo.setInitialPreviewConfig(newList);
        fileOutInfo.setInitialPreview(fileOutBuilder.getIpList());
        fileOutInfo.setAppend(true);
        return fileOutInfo;
    }
}
