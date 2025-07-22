package gift.controller.member;

import gift.dto.api.member.LoginRequestDto;
import gift.dto.api.member.MemberRequestDto;
import gift.dto.api.member.MemberResponseDto;
import gift.service.auth.AuthService;
import gift.service.member.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    
    private final MemberService memberService;
    private final AuthService authService;
    
    public MemberController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> registerMember(
        @RequestBody @Valid MemberRequestDto requestDto
    ) {
        LoginRequestDto loginRequestDto = memberService.registerMember(requestDto);
        MemberResponseDto responseDto = authService.login(loginRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> loginMember(
        @RequestBody @Valid MemberRequestDto requestDto
    ) {
        LoginRequestDto loginRequestDto = memberService.findMemberToLogin(requestDto);
        MemberResponseDto responseDto = authService.login(loginRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
