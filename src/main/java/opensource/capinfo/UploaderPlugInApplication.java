package opensource.capinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableJpaRepositories(basePackages = "opensource.capinfo.dao")
@SpringBootApplication
public class UploaderPlugInApplication
{
    public static void main(String[] args) {
        SpringApplication.run(UploaderPlugInApplication.class, args);
    }

}
