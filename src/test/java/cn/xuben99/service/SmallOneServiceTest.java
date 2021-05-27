package cn.xuben99.service;

import cn.xuben99.service.impl.SmallOneServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SmallOneServiceTest {

    @Autowired
    SmallOneServiceImpl service;

    @Test
    public void autoPunch() throws IOException {
        service.autopunch();
    }
    @Test
    public void pullLastReportData() throws IOException {
        service.pullLastReportData();
    }

}
