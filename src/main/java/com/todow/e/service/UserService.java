package com.todow.e.service;

import com.todow.e.models.UserModel;
import com.todow.e.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public HashMap<String, Object> registration(UserModel user){
        HashMap<String, Object> answer = new HashMap<>();
        try {
            ArrayList<String> errors = new ArrayList<>();
            errors = validator(errors, user.getName(), "Name", 3, 32);
            errors = validator(errors, user.getPassword(), "Password", 8, 32);
            if (!errors.isEmpty()) {
                answer.put("code", 400);
                answer.put("errors", errors);
                return answer;
            }
            if (userRepo.findByName(user.getName()) != null) {
                answer.put("code", 400);
                errors.add("This name is taken");
                answer.put("errors", errors);
                return answer;
            }
            user.setRole(0);
            String token = generateToken(64);
            user.setToken(token);
            userRepo.save(user);
            answer.put("token", token);
            answer.put("code", 201);
            return answer;
        } catch (Exception e){
            answer.put("code", 500);
            return answer;
        }
    }

    public HashMap<String, Object> auth(UserModel user){
        HashMap<String, Object> answer = new HashMap<>();
        try {
            ArrayList<String> errors = new ArrayList<>();
            errors = validator(errors, user.getName(), "Name", 3, 32);
            errors = validator(errors, user.getPassword(), "Password", 8, 32);
            if (!errors.isEmpty()) {
                answer.put("errors", errors);
                return answer;
            }
            if (userRepo.findByName(user.getName()) == null || !Objects.equals(userRepo.findByName(user.getName()).getPassword(), user.getPassword())) {
                errors.add("Password or Name entered incorrectly");
                answer.put("errors", errors);
                return answer;
            }
            answer.put("token", userRepo.findByName(user.getName()).getToken());
            answer.put("code", 200);
            return answer;
        } catch (Exception e){
            answer.put("code", 500);
            return answer;
        }
    }


    private String generateToken(int length){
        String characters = "qwertyuiopafghlzxcvnmQWERTYUIOPASDFGHJKLZXM123457890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i=0;i < length;i++) {
            sb.append(characters.charAt( random.nextInt( characters.length() )));
        }
        return sb.toString();
    }

    private ArrayList<String> validator(ArrayList<String> errors,String field, String fieldName,Integer min, Integer max){
        if(field == null){
            errors.add(fieldName + " must not be empty");
            return errors;
        } else {
            if(field.length() < min){
                errors.add(fieldName + " must contain at least "+ min +" characters");
            } if(field.length() > max){
                errors.add(fieldName + " must not be longer than "+ max +" characters");
            }
        }
        for(char i : field.toCharArray()){
            if (!"QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890".contains(i+"")){
                errors.add(fieldName + " field should only contain these characters A-Z, a-z, 0-9");
                break;
            }
        }
        return errors;
    }

}
