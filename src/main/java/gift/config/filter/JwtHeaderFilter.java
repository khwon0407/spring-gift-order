package gift.config.filter;

import gift.auth.JwtProvider;
import gift.entity.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtHeaderFilter implements Filter {
    private final JwtProvider jwtProvider;
    
    public JwtHeaderFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }
    
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        
        if (path.startsWith("/api/products") && "GET".equalsIgnoreCase(method)) {
            chain.doFilter(request, response);
            return;
        }
        
        String authHeader = httpRequest.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            setErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 Authorization 헤더입니다.");
            return;
        }
        
        String token = authHeader.substring(7);
        
        try {
            Claims claims = jwtProvider.parseToken(token);
            
            String email = claims.get("email", String.class);
            Role role = Role.valueOf(claims.get("role", String.class));
            
            httpRequest.setAttribute("email", email);
            httpRequest.setAttribute("role", role);
            
        } catch (Exception e) {
            setErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 Authorization 헤더입니다.");
            return;
        }
        
        chain.doFilter(request, response);
    }
    
    private void setErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        
        response.getWriter().write(message);
    }
}
