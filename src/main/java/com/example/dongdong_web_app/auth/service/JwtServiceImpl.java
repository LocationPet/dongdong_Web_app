package com.example.dongdong_web_app.auth.service;

import com.example.dongdong_web_app.auth.dto.SignInDto;
import com.example.dongdong_web_app.auth.dto.TokenDto;
import com.example.dongdong_web_app.auth.repository.AuthRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService{

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private AuthRepository authRepository;

    public TokenDto reissueToken(TokenDto tokenDto) throws Exception {
        if(!jwtProvider.validationToken(tokenDto.getRefreshToken())) {
            throw new Exception("TEST");
        }

        SignInDto.Response response = jwtProvider.getUserData(tokenDto.getAccessToken());


        return tokenDto;
    }
}
