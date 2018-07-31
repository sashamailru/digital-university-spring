package ru.digitaluniversity.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.digitaluniversity.entity.User;
import ru.digitaluniversity.exception.NotLogInException;
import ru.digitaluniversity.exception.TokenNotFoundException;
import ru.digitaluniversity.exception.UnsupportedRoleException;
import ru.digitaluniversity.repository.UserRepository;
import ru.digitaluniversity.security.dto.TokenDto;
import ru.digitaluniversity.security.dto.TokenRequestData;
import ru.digitaluniversity.security.component.AuthenticationToken;
import ru.digitaluniversity.security.entity.Token;
import ru.digitaluniversity.security.entity.UserRole;
import ru.digitaluniversity.security.repository.RoleRepository;
import ru.digitaluniversity.security.repository.TokenRepository;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service
public class AuthorizationServiceImpl implements AuthorizationService{

    private static final Long TIME_5_MIN = 300000L;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthProvider authProvider;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public TokenDto generateToken(TokenRequestData requestData, HttpSession session) throws UnsupportedRoleException {
        User user = userRepository.findByUsername(requestData.getUsername());
        if (user != null){
            if (user.getPassword().equals(requestData.getPassword())){
                long time = new Date().getTime();
                Long expirationDate = time + TIME_5_MIN;
                String tokenString = "TOKEN" + user.getId() + expirationDate;
                Token token = new Token();
                token.setTokenString(tokenString);
                token.setExperationDate(new Date(expirationDate));
                token.setUser(user);
                tokenRepository.save(token);
                AuthenticationToken authenticationToken = new AuthenticationToken(tokenString, user);
                authProvider.authenticate(authenticationToken);
                TokenDto tokenDto = new TokenDto();
                tokenDto.setTokenString(tokenString);
                tokenDto.setExpirationDate(expirationDate);
                tokenDto.setUserId(user.getUsername());
                tokenDto.setUserRole(getUserRole(user.getId()));
                return tokenDto;
            }
        }
        throw new NotLogInException();
    }

    @Override
    public boolean checkToken(String tokenString) throws TokenNotFoundException {
        Token token = tokenRepository.findByTokenString(tokenString);
        if (token != null) {
            long currentTime = new Date().getTime();
            return currentTime < token.getExperationDate().getTime();
        } else {
            throw new TokenNotFoundException("Token not found!");
        }
    }

    @Override
    public void createLogin(String tokenString) {
        Token token = tokenRepository.findByTokenString(tokenString);
        if (token != null){
            long experationTime = token.getExperationDate().getTime();
            long currentTime = new Date().getTime();
            if (currentTime < experationTime){
                User tokenUser = token.getUser();
                AuthenticationToken authenticationToken = new AuthenticationToken(tokenString, tokenUser);
                authProvider.authenticate(authenticationToken);
            }
        }
    }

    @Override
    public boolean hasRole(String role) throws Exception {
        AuthenticationToken authenticationToken = (AuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        List<UserRole> roles = authenticationToken.getUser().getRoles();
        UserRole byRole = roleRepository.findByRole(role);
        if (roles != null && byRole != null) {
            return roles.contains(byRole);
        }
        throw new UnsupportedRoleException();
    }

    @Override
    public String getUserRole(Integer userId) throws UnsupportedRoleException {
        User user = userRepository.findById(userId).get();
        if (user != null) {
            List<UserRole> roles = user.getRoles();
            UserRole studentRole = roleRepository.findByRole(AuthorizationService.STUDENT_ROLE);
            UserRole teacherRole = roleRepository.findByRole(AuthorizationService.TEACHER_ROLE);
            if (roles.contains(teacherRole)) {
                return AuthorizationService.TEACHER_ROLE;
            } else if (roles.contains(studentRole)){
                return AuthorizationService.STUDENT_ROLE;
            }
        }
        throw new UnsupportedRoleException();
    }

    @Override
    public Authentication getAuthentication(String tokenString) throws TokenNotFoundException {
        Token token = tokenRepository.findByTokenString(tokenString);
        if (checkToken(token.getTokenString())){
            User user = userRepository.findByUsername(token.getUser().getUsername());
            return new AuthenticationToken(tokenString, user);
        }
        throw  new TokenNotFoundException();
    }
}
