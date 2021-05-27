package cn.xuben99.service;

import cn.xuben99.properties.AutoPunchProperties;
import cn.xuben99.service.impl.MailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MailServiceTest {

    @Autowired
    MailService service;
    @Autowired
    AutoPunchProperties autoPunchProperties;

    @Test
    public void send(){
        service.sendSimpleMail(autoPunchProperties.getNotifyEmail(),"易统计打卡结果反馈:成功","打卡成功");
    }

}
