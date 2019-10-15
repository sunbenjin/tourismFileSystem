package opensource.capinfo.utils;

import opensource.capinfo.entity.InitialPreview;
import opensource.capinfo.entity.InitialPreviewConfig;
import opensource.capinfo.entity.SysResourcesFilesEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FileOutUtils
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/12 0012 10:12
 */

public class FileOutBuilder {


    private List<InitialPreview> ipList = new ArrayList<InitialPreview>();

    private List<InitialPreviewConfig> configList = new ArrayList<InitialPreviewConfig>();

    /**
     *
     * @param entity
     */
    public void builder(SysResourcesFilesEntity entity){
        if(StringUtils.isNotBlank(entity.getFileSuffix())){
            String type = entity.getFileSuffix().toLowerCase();
            if(type.matches("(ppt|pptx|doc|docx|xls|xlsx)$")){
                builderOffice(entity);
            }else if(type.matches("(zip|rar|tar|gzip|gz|7z)$")){
               builderZip(entity);
               // builderOffice(entity);
            }else if(type.matches("(htm|html)$")){
                builderText(entity);
            }else if(type.matches("(txt|ini|csv|java|php|js|css)$")){
                builderText(entity);
            }else if(type.matches("(avi|mpg|mkv|mov|mp4|3gp|webm|wmv)$")){
                builderMp4(entity);
            }else if(type.matches("(mp3|wav)$")){
                builderMp4(entity);
            }else if(type.matches("pdf")){
                builderPdf(entity);
            }else {
                builderImg(entity);
            }
        }
    }
    public void builderImg(SysResourcesFilesEntity entity){
        //    "http://kartik-v.github.io/bootstrap-fileinput-samples/samples/small.jpg"
        ipList.add(InitialPreview.builder().data(entity.getFileUrl()).build());

        //{type: "video", size: 375000, filetype: "video/mp4", caption: "Krajee Sample.mp4", url: "$urlD", key: 15}
        InitialPreviewConfig ipc = InitialPreviewConfig.builder().type(InitialPreviewConfig.InitialPreviewFileType.IMAGE.getType()).
                size(NumberUtils.toFloat(entity.getFileSize()))
                .caption(entity.getFileAllName()).previewAsData(true)
                //.downloadUrl(true)
                .key(entity.getId()).build();//文件名
        configList.add(ipc);
    };


    /**
     * test类型的组装器
     * @param entity
     * @return
     */

    private void builderText(SysResourcesFilesEntity entity){
        if(entity.getFiles()!=null){
            InitialPreview text = InitialPreview.builder()
                    .title(entity.getFileAllName())
                    .summaryContent(entity.getFiles()).buildToText();
            ipList.add(text);
        }else{
            InitialPreview text = InitialPreview.builder()
                    .title(entity.getFileAllName())
                    .summaryContent("").buildToText();
            ipList.add(text);
        }
        InitialPreviewConfig ipc = InitialPreviewConfig.builder().previewAsData(true)
                //.downloadUrl(true)
                .caption(entity.getFileName()).
                        size(NumberUtils.toFloat(entity.getFileSize())).build();
        configList.add(ipc);
    };

    /**
     * 对应解析Mp4
     * @param entity
     * @return
     */
    private void builderMp4(SysResourcesFilesEntity entity){
        //    "http://kartik-v.github.io/bootstrap-fileinput-samples/samples/small.mp4"
        ipList.add(InitialPreview.builder().data(entity.getFileUrl()).build());

        //{type: "video", size: 375000, filetype: "video/mp4", caption: "Krajee Sample.mp4", url: "$urlD", key: 15}
        InitialPreviewConfig ipc = InitialPreviewConfig.builder().type(InitialPreviewConfig.InitialPreviewFileType.VIDEO.getType()).
                size(NumberUtils.toFloat(entity.getFileSize()))
                .filetype(entity.getMimeType())
                .caption(entity.getFileAllName()).previewAsData(true)
                //.downloadUrl(true)
                .key(entity.getId()).build();//文件名
        configList.add(ipc);
    };

    private void builderPdf(SysResourcesFilesEntity entity){
        //{type: "pdf", size: 8000, caption: "PDF Sample.pdf", url: "$urlD", key: 14}
        ipList.add(InitialPreview.builder().data(entity.getFileUrl()).build());
        InitialPreviewConfig ipc = InitialPreviewConfig.builder().caption(entity.getFileAllName())
                .key(entity.getId()).size(NumberUtils.toFloat(entity.getFileSize())).previewAsData(true)
                //.downloadUrl(true)
                .type(InitialPreviewConfig.InitialPreviewFileType.PDF.getType()).build();
        configList.add(ipc);
    };

    /**
     * office 支持 ppt xls doc 等文档
     * 'http://kartik-v.github.io/bootstrap-fileinput-samples/samples/SampleDOCFile_100kb.doc'
     * {type: "office", size: 102400, caption: "SampleDOCFile_100kb.doc", url: "/file-upload-batch/2", key: 4},
     * @return
     */
    private void builderOffice(SysResourcesFilesEntity entity){
        ipList.add(InitialPreview.builder().data(entity.getFileUrl()).build());
        InitialPreviewConfig ipc = InitialPreviewConfig.builder()
                .type(InitialPreviewConfig.InitialPreviewFileType.OFFICE.getType()).previewAsData(true)
                //.downloadUrl(true)
                .size(NumberUtils.toFloat(entity.getFileSize()))
                .caption(entity.getFileAllName())
                .key(entity.getId()).build();
        configList.add(ipc);
    }

    /**
     * 这个格式目前可能不支持
     * @param entity
     * @return
     */
    private void builderGdocs(SysResourcesFilesEntity entity){
        ipList.add(InitialPreview.builder().data(entity.getFileUrl()).build());
        InitialPreviewConfig ipc = InitialPreviewConfig.builder()
                .type(InitialPreviewConfig.InitialPreviewFileType.GDOCS.getType()).previewAsData(true)
                //.downloadUrl(true)
                .size(NumberUtils.toFloat(entity.getFileSize()))
                .caption(entity.getFileAllName())
                .key(entity.getId()).build();
        configList.add(ipc);
    }

    private void builderZip(SysResourcesFilesEntity entity){
        ipList.add(InitialPreview.builder().data(entity.getFileUrl()).build());
        InitialPreviewConfig ipc = InitialPreviewConfig.builder()
                .type(InitialPreviewConfig.InitialPreviewFileType.ZIP.getType()).previewAsData(true)
                //.downloadUrl(true)
                .size(NumberUtils.toFloat(entity.getFileSize()))
                .caption(entity.getFileAllName())
                .key(entity.getId()).build();
        configList.add(ipc);
    }

    public List<InitialPreviewConfig> getConfigList() {
        return configList;
    }

    public List<InitialPreview> getIpList() {
        return ipList;
    }
}
