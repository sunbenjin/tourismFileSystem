package opensource.capinfo.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @date 2017/12/31.
 * @email 154040976@qq.com
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${server.port}")
    private String port;
    @Value("${localaddress}")
    private String localaddress;


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //这里采用包含注解的方式来确定要显示的接口
//                .apis(RequestHandlerSelectors.basePackage("com.capinfo.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("上传下载的接口文档")
                .description("抽象出来的上传下载组件的API接口文档")
                .termsOfServiceUrl(localaddress+":"+port+"/swagger-ui.html")
                .contact(new Contact("ltl", "http://www.capinfo.com.cn", "63941428@qq.com"))
                .version("1.1.0")
                .build();
    }

}
