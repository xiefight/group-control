package cn.yunshi.groupcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
public class GroupControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroupControlApplication.class, args);
    }


}
