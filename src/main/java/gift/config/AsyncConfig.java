package gift.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
    // 필요 시 ThreadPoolTaskExecutor 설정도 가능
}
