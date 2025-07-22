package gift.config;

import gift.auth.JwtProvider;
import gift.config.filter.JwtHeaderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    private final JwtProvider jwtProvider;
    
    public FilterConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }
    
    @Bean
    public FilterRegistrationBean<JwtHeaderFilter> jwtHeaderFilter() {
        FilterRegistrationBean<JwtHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        
        registrationBean.setFilter(new JwtHeaderFilter(jwtProvider));
        registrationBean.addUrlPatterns("/api/wishlist", "/api/wishlist/*", "/api/products", "/api/products/*");
        registrationBean.setOrder(1);
        
        return registrationBean;
    }
}
