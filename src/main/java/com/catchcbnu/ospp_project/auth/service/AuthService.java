package com.catchcbnu.ospp_project.auth.service;

import com.catchcbnu.ospp_project.auth.dto.LoginRequest;
import com.catchcbnu.ospp_project.auth.dto.LoginResponse;
import com.catchcbnu.ospp_project.auth.dto.SignupRequest;
import com.catchcbnu.ospp_project.auth.dto.SignupResponse;
import com.catchcbnu.ospp_project.auth.jwt.JwtProvider;
import com.catchcbnu.ospp_project.common.exception.DuplicateEmailException;
import com.catchcbnu.ospp_project.common.exception.DuplicateNicknameException;
import com.catchcbnu.ospp_project.common.exception.InvalidCredentialsException;
import com.catchcbnu.ospp_project.user.entity.User;
import com.catchcbnu.ospp_project.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException();
        }
        if (userRepository.existsByNickname(request.nickname())) {
            throw new DuplicateNicknameException();
        }

        String passwordHash = passwordEncoder.encode(request.password());
        User user = new User(request.email(), passwordHash, request.nickname(), request.college(), request.department());
        User saved = userRepository.save(user);

        return new SignupResponse(saved.getId());
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtProvider.generateToken(user.getId());
        return new LoginResponse(token);
    }

    public void logout(String token) {
        jwtProvider.invalidateToken(token);
    }
}
