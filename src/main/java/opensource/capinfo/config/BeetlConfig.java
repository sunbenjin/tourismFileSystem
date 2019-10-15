package opensource.capinfo.config;

import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName BeetlConfig
 * @Description TODO
 * @Author 消魂钉
 * @Date 7/4 0004 10:10
 */
@Configuration
public class BeetlConfig {
    @Autowired
    private BeetlProperties beetlProperties;
    @Autowired
    private HttpServletRequest request;

    /**
     * beetl的配置
     */
    @Bean(initMethod = "init")
    public BeetlConfiguration beetlConfiguration() {
        BeetlConfiguration beetlConfiguration = new BeetlConfiguration();
        beetlConfiguration.setResourceLoader(new ClasspathResourceLoader(BeetlConfig.class.getClassLoader(), beetlProperties.getPrefix()));
        beetlConfiguration.setConfigProperties(beetlProperties.getProperties());

        return beetlConfiguration;
    }


    @Bean
    public GroupTemplate getGroupTemplate(@Qualifier("beetlConfiguration") BeetlConfiguration beetlConfiguration) {
        GroupTemplate gt = beetlConfiguration.getGroupTemplate();
        Map<String,Object> shared = new HashMap<>();
        shared.put("author", "李陶琳");
        gt.setSharedVars(shared);
        return gt;
    }

    /**
     * beetl的视图解析器
     */
    @Bean
    public BeetlSpringViewResolver beetlViewResolver() {
        BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
        beetlSpringViewResolver.setConfig(beetlConfiguration());
        beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
        beetlSpringViewResolver.setSuffix(".html");

        return beetlSpringViewResolver;
    }




}
