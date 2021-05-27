package cn.xuben99;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class AutoPunchApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoPunchApplication.class, args);
        log.info("=====> 自动打卡服务启动 <=====");
    }

}
