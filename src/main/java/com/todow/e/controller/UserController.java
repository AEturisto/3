package com.todow.e.controller;

import com.todow.e.models.UserModel;
import com.todow.e.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @PostMapping()
    public ResponseEntity registration(@RequestBody UserModel user) {
        try {
            HashMap<String, Object> answer = new HashMap<>();
            if(user.getName() == null){
                answer.put("Error", "Name field must not be empty");
                return ResponseEntity.badRequest().body(answer);
            }
            if(user.getPassword() == null){
                answer.put("Error", "Password field must not be empty");
                return ResponseEntity.badRequest().body(answer);
            }
            if(user.getPassword().length() < 8){
                answer.put("Error", "Password must contain at least 8 characters");
                return ResponseEntity.badRequest().body(answer);
            }
            for(char i : user.getName().toCharArray()){
                if (!"QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890".contains(i+"")){
                    answer.put("Error", "Name field should only contain these characters A-Z, a-z, 0-9");
                    return ResponseEntity.badRequest().body(answer);
                }
            }
            for(char i : user.getPassword().toCharArray()){
                if (!"QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890".contains(i+"")){
                    answer.put("Error", "Password field should only contain these characters A-Z, a-z, 0-9");
                    return ResponseEntity.badRequest().body(answer);
                }
            }

            user.setRole(0);
            user.setToken(generateToken(64));
            userRepo.save(user);
            answer.put("Message", "Your account has been created successfully");
            return ResponseEntity.ok(answer);
        } catch (Exception e) {

            //Перед заливкой заменить этой строкой
            //return ResponseEntity.internalServerError();
            return ResponseEntity.badRequest().body(e);
        }
    }

    public static String generateToken(int length){
        String characters = "qwertyuiopafghlzxcvnmQWERTYUIOPASDFGHJKLZXM123457890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i=0;i < length;i++) {
            sb.append(characters.charAt( random.nextInt( characters.length() )));
        }
        return sb.toString();
    }

}
