package cz.upce.fei.nnpia.controller;

import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.dto.UserRequestDto;
import cz.upce.fei.nnpia.dto.UserResponseDto;
import cz.upce.fei.nnpia.responses.LoginResponse;
import cz.upce.fei.nnpia.service.AuthenticationService;
import cz.upce.fei.nnpia.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser.toResponseDto());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody UserRequestDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        log.info("User authenticated: " + authenticatedUser.getEmail());

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }
}