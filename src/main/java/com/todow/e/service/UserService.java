package com.todow.e.service;

import com.todow.e.exceptions.MalformedInputException;
import com.todow.e.models.UserModel;
import com.todow.e.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserModel registration(UserModel user) throws MalformedInputException{
        HashMap<String, Object> answer = new HashMap<>();
        if(user.getName() == null){
            throw new MalformedInputException("Name must not be empty");
        }
        if(user.getPassword() == null){
            throw new MalformedInputException("Password must not be empty");
        }
        if(user.getPassword().length() < 8){
            throw new MalformedInputException("Password must contain at least 8 characters");
        }
        if(user.getPassword().length() > 32){
            throw new MalformedInputException("Password must not be longer than 32 characters");
        }
        if(user.getName().length() < 3){
            throw new MalformedInputException("Name must contain at least 3 characters");
        }
        if(user.getName().length() > 32){
            throw new MalformedInputException("Name must not be longer than 32 characters");
        }
        for(char i : user.getName().toCharArray()){
            if (!"QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890".contains(i+"")){
                throw new MalformedInputException("Name field should only contain these characters A-Z, a-z, 0-9");
            }
        }
        for(char i : user.getPassword().toCharArray()){
            if (!"QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890".contains(i+"")){
                throw new MalformedInputException("Password field should only contain these characters A-Z, a-z, 0-9");
            }
        }
        if(userRepo.findByName(user.getName()) != null) {
            throw new MalformedInputException("This name is taken");
        }
        user.setRole(0);
        user.setToken(generateToken(64));
        return userRepo.save(user);
    }

    public UserModel auth(UserModel user) throws MalformedInputException{
        if(user.getName() == null){
            throw new MalformedInputException("Name field must not be empty");
        }
        if(user.getPassword() == null){
            throw new MalformedInputException("Password field must not be empty");
        }
        if(userRepo.findByName(user.getName()) == null || !Objects.equals(userRepo.findByName(user.getName()).getPassword(), user.getPassword())) {
            throw new MalformedInputException("Password or Name entered incorrectly");
        }
        return userRepo.findByName(user.getName());
    }



    public String generateToken(int length){
        String characters = "qwertyuiopafghlzxcvnmQWERTYUIOPASDFGHJKLZXM123457890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i=0;i < length;i++) {
            sb.append(characters.charAt( random.nextInt( characters.length() )));
        }
        return sb.toString();
    }

}
