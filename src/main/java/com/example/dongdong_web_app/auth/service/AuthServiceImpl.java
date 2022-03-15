package com.example.dongdong_web_app.auth.service;

import com.example.dongdong_web_app.auth.dto.SignInDto;
import com.example.dongdong_web_app.auth.dto.SignUpDto;
import com.example.dongdong_web_app.auth.entity.AuthEntity;
import com.example.dongdong_web_app.auth.repository.AuthRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.Signature;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String SECRET_KEY = "1io2jipowejfisjkofj0198q3ju4i1jkl3j4pr13jifsadlkjfapsc89vqu34";

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Jwt jwt;

    @Override
    public SignInDto.Response getUserData(String user_email, String user_password) {
        String getPassword = authRepository.findByUserEmail(user_email).getUserPassword();


        if(passwordEncoder.matches(user_password, getPassword)){
            AuthEntity UserData = authRepository.findByUserEmail(user_email);
            SignInDto.Info info = new SignInDto.Info(UserData.getUserUid(), UserData.getUserNickName(), UserData.getUserAge());
            SignInDto.Animal animal = new SignInDto.Animal(UserData.getAnimalName(), UserData.getAnimalKind());

            SignInDto.Token token = jwt.createToken( UserData.getUserUid(), info );

            SignInDto.Response response = SignInDto.Response.builder()
                    .userEmail(UserData.getUserEmail())
                    .info(info)
                    .token(token)
                    .animal(animal)
                    .build();

            return response;
        }else{
            return null;
        }
    }

    @Override
    public ResponseEntity saveUserData(SignUpDto.Request request) {
        String planText = request.getUserPassword();
        String encodePassword = passwordEncoder.encode(planText);
        AuthEntity auth;
        try{
            auth = AuthEntity.builder()
                    .userEmail(request.getUserEmail())
                    .userPassword(encodePassword)
                    .userNickName(request.getInfo().getUserNickName())
                    .userAge(request.getInfo().getUserAge())
                    .animalKind(request.getAnimal().getAnimalKind())
                    .animalName(request.getAnimal().getAnimalName())
                    .build();

            authRepository.save(auth);

            return new ResponseEntity(null,null,200);
        }catch (Exception e){
            return new ResponseEntity(null,null,400);
        }
    }
}