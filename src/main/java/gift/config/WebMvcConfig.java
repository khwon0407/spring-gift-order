package gift.config;

import gift.config.interceptor.ValidHeaderInterceptor;
import gift.config.resolver.LoginUserArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final ValidHeaderInterceptor validHeaderInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;
    
    public WebMvcConfig(ValidHeaderInterceptor validHeaderInterceptor,
        LoginUserArgumentResolver loginUserArgumentResolver) {
        this.validHeaderInterceptor = validHeaderInterceptor;
        this.loginUserArgumentResolver = loginUserArgumentResolver;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(validHeaderInterceptor);
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 또는 "/**"로 전체 허용
            .allowedOrigins("http://localhost:3000") // 허용할 Origin
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("Authorization") // 클라이언트에서 읽을 수 있도록
            .allowCredentials(true); // 쿠키 전송 허용 시 true
    }
}
