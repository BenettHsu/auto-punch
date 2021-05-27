package cn.xuben99.schedule;

import cn.xuben99.properties.AutoPunchProperties;
import cn.xuben99.properties.ScheduleProperties;
import cn.xuben99.service.SmallOneService;
import cn.xuben99.service.impl.MailService;
import cn.xuben99.util.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Configuration
@EnableScheduling
@EnableConfigurationProperties(ScheduleProperties.class)
public class SmallOneSchedule {
    private final long SECOND = 1_000L;
    private final String AUTO_PUNCH_SCHEDULE = "易统计自动打卡";
    @Autowired
    private SmallOneService smallOneService;
    @Autowired
    private MailService mailService;
    @Autowired
    private ScheduleProperties scheduleProperties;
    @Resource(name = "threadPool")
    private ExecutorService threadPool;
    @Autowired
    private AutoPunchProperties autoPunchProperties;

    @Scheduled(cron = "${auto-punch.schedule.autoPunchScheduleCron}")
    public void autoPunchSchedule() {
        threadPool.execute(() -> {
            Timer timer = new Timer(AUTO_PUNCH_SCHEDULE);
            timer.start();
            handleWithException(AUTO_PUNCH_SCHEDULE);
            timer.end();
        });
    }

    public void handle() throws IOException {
         smallOneService.autopunch();
    }

    public void handleWithException(String taskName) {
        AtomicInteger currentRetryCount = new AtomicInteger(0);
        int totalRetryCount = scheduleProperties.getExceptionRetryCount();
        while (currentRetryCount.get() < totalRetryCount) {
            try {
                handle();
                return;
            } catch (Exception e) {
                log.error(e.getMessage());
                retryHandleWhenException(currentRetryCount, taskName);
            }
        }
    }

    public void retryHandleWhenException(AtomicInteger currentRetryCount, String taskName) {
        //下次重试时间依次累加 10秒 20秒 30秒
        long nextRetryTime = currentRetryCount.incrementAndGet() * scheduleProperties.getExceptionRetryTimeGap();
        log.error("{}任务调度【发生异常】,将在{}秒后第{}次重试，共{}次", taskName
                , nextRetryTime, currentRetryCount.get(), scheduleProperties.getExceptionRetryCount());
        try {
            Thread.sleep(nextRetryTime * SECOND);
        } catch (InterruptedException e) {
            log.warn("thread sleep throw exception");
        }
    }

    @Data
    public class Timer {
        private String taskName;
        private Long startTime;
        private Long endTime;

        Timer(String taskName) {
            this.taskName = taskName;
        }

        public void start() {
            startTime = System.currentTimeMillis();
            log.info(">>>>>>>>>> {}任务调度开始:{} ", taskName, DateUtil.getCurrentStr());
        }

        public void end() {
            endTime = System.currentTimeMillis();
            log.info("<<<<<<<<<< {}任务调度结束 用时:{}毫秒", taskName, endTime - startTime);
        }
    }
}
