package com.poojithairosha.webscoketdemo.auth;

import com.poojithairosha.webscoketdemo.jwt.JwtTokenUtil;
import com.poojithairosha.webscoketdemo.user.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<AuthRespDto> login(@RequestBody AuthReqDto authReqDto) {
        log.info("Login request received for user: {}", authReqDto.username());
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authReqDto.username(), authReqDto.password()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserDetails userDetails = userDetailsService.loadUserByUsername(authReqDto.username());
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(AuthRespDto.builder().token(token).username(userDetails.getUsername()).build());

    }

}
