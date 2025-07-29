package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
    String clientId,
    String redirectUri,
    
    String authorizeUrl,
    String tokenUrl,
    String userInfoUrl,
    String messageSendUrl
) {}
