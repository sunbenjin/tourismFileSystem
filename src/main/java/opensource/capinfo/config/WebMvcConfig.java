package opensource.capinfo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import opensource.capinfo.interceptor.CorsInterceptor;
import opensource.capinfo.interceptor.TokenInterceptor;
import opensource.capinfo.uploader.jsonTransform.FileInputConverter;
import opensource.capinfo.uploader.transformToMp4.ConverterToMp4;
import opensource.capinfo.utils.ChangeVideo;
import opensource.capinfo.utils.FTPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 注释说明： 继承 WebMvcConfigurationSupport 这个类就会导致基本路径配置实效，需要重新配置
 *         registry.addResourceHandler("/**")
*                 .addResourceLocations("classpath:/resources/")
*                 .addResourceLocations("classpath:/static/")
*                 .addResourceLocations("classpath:/public/");
 *
 *         才可以访问
 *
 *
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    private static final String UTF8 = "utf-8";
    @Autowired
    private ApplicationProperties props;


    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(supportedMediaTypes);

        //supportedMediaTypes  application/json;charset=UTF-8


        return converter;
    }


    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //设置日期格式
        mappingJackson2HttpMessageConverter.setPrettyPrint(false);
        ObjectMapper objectMapper = new JsonMapper();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        //设置中文编码格式
        List<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
        return mappingJackson2HttpMessageConverter;
    }

    /**
     * 推荐写法
     * @param converters
     */
    @Override
    public void extendMessageConverters(List < HttpMessageConverter <?>> converters) {
        HttpMessageConverter converter = Iterables.find(converters, new Predicate<HttpMessageConverter<?>>() {
            public boolean apply( @Nullable HttpMessageConverter <?> input) {
                return input != null && input instanceof StringHttpMessageConverter;
            }
        }, null);
        if (converter == null) {
            StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
            stringConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "html", Charset.forName(UTF8))));
            converters.add(1, stringConverter); //默认的StringHttpMessageConverter在第二个位置
            return;
        }
        StringHttpMessageConverter stringHttpMessageConverter = (StringHttpMessageConverter)converter;
        stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "html", Charset.forName(UTF8))));

        Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
        while(iterator.hasNext()){
            HttpMessageConverter<?> conv = iterator.next();
            if(conv instanceof MappingJackson2HttpMessageConverter){
                iterator.remove();
            }
        }
        converters.add(getMappingJackson2HttpMessageConverter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //添加外部访问地址
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:/"+props.getFtpServer().getHomeDirectory()+"\\");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        //http://localhost/image/2018/07/28/2018072836803.png

        super.addResourceHandlers(registry);

    }

    /**
     * 初始化工具类
     */
    @PostConstruct
    public void init(){
        FTPUtils.configInfo(props.getFtpServer());
        ConverterToMp4.setProps(props);
        FileInputConverter.setProps(props);
        ChangeVideo.setAvifilepath(props.getAvifilepath());
        ChangeVideo.setFfmpegpath(props.getFfmpegpath());
        ChangeVideo.setMencoderpath(props.getMencoderpath());
    }


    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getMyInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**",  "/swagger-ui.html#/**");
        //registry.addInterceptor(getTokenInterceptor()).addPathPatterns("/enclosure/fileInput/uploader");
        //registry.addInterceptor(getTokenInterceptor()).addPathPatterns("/enclosure/fileInput/api/uploader");
    }

    @Bean
    public CorsInterceptor getMyInterceptor(){
        return new CorsInterceptor();
    }

    @Bean
    public TokenInterceptor getTokenInterceptor() {
        return new TokenInterceptor();
    }



}
