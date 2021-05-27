package cn.xuben99.service.impl;

import cn.xuben99.properties.AutoPunchProperties;
import cn.xuben99.service.SmallOneService;
import cn.xuben99.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@EnableConfigurationProperties(AutoPunchProperties.class)
public class SmallOneServiceImpl implements SmallOneService {

    @Autowired
    private MailService mailService;
    @Autowired
    private AutoPunchProperties autoPunchProperties;

    public Map<String, String> initHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", autoPunchProperties.getCookie());
        header.put("Content-Type", "application/json");
        header.put("User-Agent", "PostmanRuntime/7.26.8");
        header.put("ncov-access-token", autoPunchProperties.getToken());
        return header;
    }

    public String pullLastReportData() throws IOException {
        String lastReport = "https://www.ioteams.com/ncov/api/users/last-report";
        Map<String, String> header = initHeader();
        String res = HttpUtil.get(lastReport, header);
        JSONObject resJsonObj = JSON.parseObject(res);
        int code = (int) resJsonObj.get("code");
        if (code == 0) {
            JSONObject dataJsonObj = resJsonObj.getJSONObject("data").getJSONObject("data");
            dataJsonObj.fluentRemove("_id")
                    .fluentRemove("user")
                    .fluentRemove("company")
                    .fluentRemove("created_at")
                    .fluentRemove("updated_at")
                    .fluentRemove("__v");
            dataJsonObj.getJSONObject("address").remove("_id");
            return dataJsonObj.toString();
        }
        throw new RuntimeException("pull last report data failed");
    }

    @Override
    public void autopunch() throws IOException {
        String url = "https://www.ioteams.com/ncov/api/users/dailyReport";
        Map<String, String> header = initHeader();
        String body = pullLastReportData();
        String res = HttpUtil.post(url, header, body);
        JSONObject resJsonObject = JSON.parseObject(res);
        int code = (int) resJsonObject.get("code");
        if (!autoPunchProperties.getNotifyEnable()){return;}
        if (code == 0) {
            mailService.sendSimpleMail(autoPunchProperties.getNotifyEmail(), "易统计打卡结果反馈:成功", "吼你谢特，打卡成功! => "+res);
        } else {
            mailService.sendSimpleMail(autoPunchProperties.getNotifyEmail(), "易统计打卡结果反馈:失败", "打卡失败 => " + res);
        }
    }
}
