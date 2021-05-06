package com.leverx.controller;

import com.leverx.error.exception.UserNotFoundException;
import com.leverx.model.User;
import com.leverx.model.dto.PasswordDto;
import com.leverx.service.MailService;
import com.leverx.service.RedisService;
import com.leverx.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

/**
 * Controller for authentication's URL.
 * Check src/main/resources/postman/Dealer_stat.postman_collection.json for more detail URL mapping.
 *
 * @author Andrew Panas
 */

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final RedisService redisService;
    private final MailService mailService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(RedisService redisService, MailService mailService, UserService userService, PasswordEncoder passwordEncoder) {
        this.redisService = redisService;
        this.mailService = mailService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/confirm/{email}/{hash_code}")
    public ResponseEntity<?> activateUserByCode(@PathVariable("hash_code") String hashCode, @PathVariable("email") String email) {
        if (redisService.getHashcodeForEmailActivation(email) != null) {
            if (redisService.getHashcodeForEmailActivation(email).equals(hashCode)) {
                User user = userService.findUserByEmail(email);
                user.setActive(true);
                userService.update(user);
                log.debug("In method activateUserByCode: user activated email " + email);
                redisService.deleteHashcodeForEmailActivation(email);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<?> sendCodeToResetPassword(@RequestParam("email") String email) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            log.debug("In method sendCodeToResetPassword: User not found Exception");
            throw new UserNotFoundException("User not found");
        }
        redisService.setHashcodeForPasswordReset(email);
        final Object hashcode = redisService.getHashcodeForPasswordReset(email);
        if (hashcode != null) {
            try {
                mailService.createMessageForPasswordResetAndSend(email, hashcode.toString());
            } catch (MessagingException ex) {
                log.error("Error in method sendCodeToResetPassword: " + ex.getMessage());
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email, @RequestBody PasswordDto password) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            log.info("In method resetPassword: User not found Exception");
            throw new UserNotFoundException("User not found");
        }
        if (password.getCode().equals(redisService.getHashcodeForPasswordReset(email))) {
            redisService.deleteHashcodeForPasswordReset(email);
            user.setPassword(passwordEncoder.encode(password.getNewPassword()));
            userService.update(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/check_code")
    public ResponseEntity<?> checkIfCodeExists(@RequestParam("email") String email) throws MessagingException {
        boolean codeIsExists = redisService.isCodeForEmailActivationExists(email);
        if (!codeIsExists) {
            User user = userService.findUserByEmail(email);
            if (user == null) {
                log.info("In method checkIfCodeExists: User not found Exception");
                throw new UserNotFoundException("User not found");
            }
            if (!user.isActive()) {
                redisService.setHashcodeForEmailActivation(email);
                Object hashcode = redisService.getHashcodeForEmailActivation(email);
                if (hashcode != null) {
                    mailService.createMessageForEmailActivationAndSend(email, hashcode.toString());
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }
                else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}