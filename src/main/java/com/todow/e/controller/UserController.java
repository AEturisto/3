package com.todow.e.controller;

import com.todow.e.exceptions.MalformedInputException;
import com.todow.e.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.todow.e.service.UserService;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody UserModel user) {
        try {
            HashMap<String, Object> answer = new HashMap<>();
            userService.registration(user);
            answer.put("Message", "Your account has been created successfully");
            return ResponseEntity.ok(answer);
        } catch (MalformedInputException e) {
            HashMap<String, Object> answer = new HashMap<>();
            answer.put("Error", e.getMessage());
            return ResponseEntity.badRequest().body(answer);
        }
    }
    @PostMapping("/auth")
    public ResponseEntity auth(@RequestBody UserModel user) {
        try{
            HashMap<String, Object> answer = new HashMap<>();
            answer.put("token", userService.auth(user).getToken());
            return ResponseEntity.ok(answer);
        } catch (MalformedInputException e) {
            HashMap<String, Object> answer = new HashMap<>();
            answer.put("Error", e.getMessage());
            return ResponseEntity.badRequest().body(answer);
        }
    }
}
