package gift.controller.oauth;

import gift.dto.api.member.MemberResponseDto;
import gift.service.auth.KakaoOAuthService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoOAuthController {
    
    private final KakaoOAuthService kakaoOAuthService;
    
    public KakaoOAuthController(KakaoOAuthService kakaoOAuthService) {
        this.kakaoOAuthService = kakaoOAuthService;
    }
    
    @GetMapping("/login/kakao")
    public void loginToGetToken(HttpServletResponse response) throws IOException {
        String kakaoLink = kakaoOAuthService.getKakaoLoginLink();
        response.sendRedirect(kakaoLink);
    }
    
    @GetMapping
    public ResponseEntity<MemberResponseDto> kakaoLogin(
        @RequestParam(name = "code") String authorizationCode
    ) {
        MemberResponseDto responseDto = kakaoOAuthService.kakaoLogin(authorizationCode);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
