package opensource.capinfo.config;

import lombok.Data;
import opensource.capinfo.utils.FTPUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName ApplicationProperties
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/17 0017 9:09
 */
@Component
@ConfigurationProperties(prefix="uploader")
@Data
public class ApplicationProperties {

    private String basePath;
    private String ffmpegpath;
    private String mencoderpath;
    private String avifilepath;
    /**
     * 是否转化为mp4
     */
    private boolean converMp4;
    /**
     * 转化的类型
     */
    private String converType;

    /**
     * 转化后的临时文件
     */
    private String tempPath;
    /**
     * ftpServer 
     */
    private FtpServerProperties ftpServer;
    /**
     * 对应资源服务器的地址
     *   registry.addResourceHandler("/image/**")
     *   .addResourceLocations("file:/"+props.getFtpServer().getHomeDirectory()+"\\");
     */
    private String displayPath;







}
