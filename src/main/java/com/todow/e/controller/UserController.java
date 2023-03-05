package com.todow.e.controller;

import com.todow.e.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.todow.e.service.UserService;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody UserModel user) {
        HashMap<String, Object> answer = userService.registration(user);
        return ResponseEntity.status(HttpStatus.valueOf((Integer) answer.get("Code"))).body(answer);
    }
    @PostMapping("/auth")
    public ResponseEntity auth(@RequestBody UserModel user) {
        HashMap<String, Object> answer = userService.auth(user);
        return ResponseEntity.status(HttpStatus.valueOf((Integer) answer.get("Code"))).body(answer);
    }
}
