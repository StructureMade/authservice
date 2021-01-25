package de.structuremade.ms.authservice.api.routes;

import com.google.gson.Gson;
import de.structuremade.ms.authservice.api.json.LoginUserJson;
import de.structuremade.ms.authservice.api.service.JWTUtil;
import de.structuremade.ms.authservice.database.entity.School;
import de.structuremade.ms.authservice.database.entity.User;
import de.structuremade.ms.authservice.response.ResponseJWT;
import de.structuremade.ms.authservice.database.repo.SchoolRepository;
import de.structuremade.ms.authservice.database.repo.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/auth")
public class AuthRoute {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthRoute.class);

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JWTUtil jwtUtil;


    @Transactional
    @CrossOrigin
    @PostMapping(path = "/login", produces = "application/json", consumes = "application/json")
    public Object loginUser(@RequestBody @Valid LoginUserJson userJson, HttpServletResponse response, HttpServletRequest request) {
        /*Method Variables*/
        Gson gson = new Gson();
        Cookie cookie;
        User user;
        School school;
        List<School> schools;
        String token;
        /*End of Variables*/
        try {
            /*Get User and check if an User was found*/
            user = userRepository.findByEmail(userJson.getEmail());
            if (user != null) {
                /*Check if User typed the right Password*/
                if (BCrypt.checkpw(userJson.getPassword(), user.getPassword()) && user.isVerified()) {
                    LOGGER.info("User and Password are valid & create now an JWT for Auth");
                    if (user.getLastSchool() == null) {
                        /*Set Lastschool of User*/
                        LOGGER.info("Set lastschool of User");
                        schools = user.getSchools();
                        school = schools.get(0);
                        user.setLastSchool(school.getId());
                        userRepository.save(user);
                    }
                    /*Generate JWT Auth Token*/
                    LOGGER.info("Generate JWT for Cookie and Response");
                    token = jwtUtil.generateToken(user);
                    response.setStatus(HttpStatus.ACCEPTED.value());
                    /*Set Cookie & generate Cookie JWT*/
                    LOGGER.info("Init Cookie and response it");
                    cookie = new Cookie("jwt", jwtUtil.createCookieToken(user));
                    cookie.setMaxAge(60 * 60 * 24 * 30);
                    cookie.setSecure(true);
                    cookie.setDomain("structuremade.de");
                    cookie.setPath("/auth/refreshtoken");
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                    return gson.toJson(new ResponseJWT(token));
                } else {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    LOGGER.info("Password was false");
                    return gson.toJson(new ResponseJWT());
                }
            }
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            LOGGER.info("Email was false");
            return gson.toJson(new ResponseJWT());
        } catch (Exception e) {
            LOGGER.error("Login failed", e.fillInStackTrace());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return gson.toJson(new ResponseJWT());
        }
    }

    @CrossOrigin
    @GetMapping(path = "/refreshtoken", produces = "application/json")
    public Object refreshToken(HttpServletRequest request, HttpServletResponse response) {
        /*Method Variables*/
        Gson gson = new Gson();
        User user;
        /*End of Variables*/
        try {
            /*Get Cookies from User and get from there the JWT Token*/
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equalsIgnoreCase("jwt")) {
                    if (jwtUtil.isTokenExpired(cookie.getValue()) || jwtUtil.isTokenInBlacklist(cookie.getValue())) {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        return gson.toJson(new ResponseJWT());
                    }
                    user = userRepository.findAllById(jwtUtil.extractIdOrEmail(cookie.getValue()));
                    String token = jwtUtil.generateToken(user);
                    response.setStatus(HttpStatus.ACCEPTED.value());
                    return gson.toJson(new ResponseJWT(token));
                }
            }
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return gson.toJson(new ResponseJWT());
        } catch (Exception e) {
            LOGGER.error("Couldn't generate Token", e.fillInStackTrace());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return gson.toJson(new ResponseJWT());
        }
    }

    @CrossOrigin
    @GetMapping(path = "/refreshtoken/{schoolid}", produces = "application/json")
    public Object refreshToken(@PathVariable String schoolid, HttpServletRequest request, HttpServletResponse response) {
        /*Method Variables*/
        Gson gson = new Gson();
        User user;
        /*End of Variables*/
        try {
            /*Get Cookies from User and get from there the JWT Token*/
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equalsIgnoreCase("jwt")) {
                    if (jwtUtil.isTokenExpired(cookie.getValue()) || jwtUtil.isTokenInBlacklist(cookie.getValue())) {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        return gson.toJson(new ResponseJWT());
                    }
                    /*Watch if User exists and set lastschool*/
                    user = userRepository.findAllById(jwtUtil.extractIdOrEmail(cookie.getValue()));
                    user.setLastSchool(schoolid);
                    String token = jwtUtil.generateToken(user);
                    response.setStatus(HttpStatus.ACCEPTED.value());
                    return gson.toJson(new ResponseJWT(token));
                }
            }
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return gson.toJson(new ResponseJWT());
        } catch (Exception e) {
            LOGGER.error("Couldn't generate Token", e.fillInStackTrace());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return gson.toJson(new ResponseJWT());
        }
    }


}
