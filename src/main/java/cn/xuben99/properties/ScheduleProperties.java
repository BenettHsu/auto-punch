package cn.xuben99.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Data
@ConfigurationProperties(prefix = "auto-punch.schedule")
@Component
public class ScheduleProperties {

    /**
     * 定时任务异常重试次数
     */
    int exceptionRetryCount = 3;
    /**
     * 定时任务异常重试间隔时间，累加算法 10，20，30
     */
    int exceptionRetryTimeGap = 10;
    /**
     * 线程池容量
     */
    int threadPoolCapacity = 10;

    @Bean(name = "threadPool")
    ThreadPoolExecutor initTheadPool() {
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolCapacity);
    }

}
