package cn.xuben99.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("auto-punch")
public class AutoPunchProperties {
    String cookie;
    String token;
    Boolean notifyEnable = Boolean.FALSE;
    String notifyEmail;
}
