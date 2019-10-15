package opensource.capinfo.config;

import lombok.Data;

/**
 * @ClassName FtpServerProperties
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/17 0017 9:25
 */
@Data
public class FtpServerProperties {

    private String port;
    private String name;
    private String password;
    private String homeDirectory;
    private String path;

    private String maxTotal;
    private String minIdel;
    private String maxIdle;
    private String maxWaitMillis;
    private String host;

}
