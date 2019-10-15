package opensource.capinfo.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import opensource.capinfo.utils.FTPUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统自动启动类
 */
@Component
@Order
@Slf4j
@Data
public class WebStartUpRunner implements CommandLineRunner {

    @Autowired
    private ApplicationProperties props;

    private FtpServerFactory serverFactory = null;

    private org.apache.ftpserver.FtpServer ftpServer = null;

    @Override
    public void run(String... args) throws Exception {


        serverFactory = new FtpServerFactory();
        ListenerFactory factory = new ListenerFactory();
        //设置监听端口
        FtpServerProperties propsFtpServer = props.getFtpServer();

        factory.setPort(NumberUtils.toInt(propsFtpServer.getPort()));
        File filePath = new File(propsFtpServer.getHomeDirectory());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        //替换默认监听
        serverFactory.addListener("default", factory.createListener());
        //用户名
        BaseUser user = new BaseUser();
        user.setName(propsFtpServer.getName());
        //密码 如果不设置密码就是匿名用户
        user.setPassword(propsFtpServer.getPassword());
        //用户主目录
        user.setHomeDirectory(propsFtpServer.getHomeDirectory());
        List<Authority> authorities = new ArrayList<Authority>();
        //增加写权限
        authorities.add(new WritePermission());
        user.setAuthorities(authorities);
        //增加该用户
        serverFactory.getUserManager().save(user);
        ftpServer = serverFactory.createServer();
        start();
    }

    public void start(){
        try {
            //System.out.println("=========================="+ftpServer.isStopped());
            ftpServer.start();
            //System.out.println("========================="+ftpServer.isStopped());
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }
}