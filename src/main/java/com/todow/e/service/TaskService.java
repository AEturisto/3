package com.todow.e.service;

import com.todow.e.exceptions.MalformedInputException;
import com.todow.e.exceptions.UnauthorizedException;
import com.todow.e.models.TaskModel;
import com.todow.e.models.UserModel;
import com.todow.e.repos.TaskRepo;
import com.todow.e.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TaskRepo taskRepo;

    public TaskModel createTask(String token, TaskModel task) throws MalformedInputException, UnauthorizedException {
        token = token.substring(7);
        UserModel owner = userRepo.findByToken(token);
        if(owner == null) {
            throw new UnauthorizedException();
        }
        if(task.getName() == null){
            throw new MalformedInputException("Name must not be empty");
        }
        task.setOwner(owner);
        return taskRepo.save(task);
    }

    public void markTask(String token, TaskModel task) throws UnauthorizedException {
        token = token.substring(7);
        UserModel owner = userRepo.findByToken(token);
        TaskModel changeableTask = taskRepo.findById(task.getId());
        if (owner == null || !Objects.equals(changeableTask.getOwner().getId(), owner.getId())) {
            throw new UnauthorizedException();
        }
        changeableTask.setDone(!changeableTask.getDone());
        taskRepo.save(changeableTask);
    }

    public TaskModel editTask(String token, TaskModel task) throws MalformedInputException, UnauthorizedException {
        token = token.substring(7);
        UserModel owner = userRepo.findByToken(token);
        TaskModel changeableTask = taskRepo.findById(task.getId());
        if(owner == null || !Objects.equals(changeableTask.getOwner().getId(), owner.getId())) {
            throw new UnauthorizedException();
        }
        if(changeableTask == null){
            throw new UnauthorizedException();
        }
        if(task.getName() != null){
            changeableTask.setName(task.getName());
        }
        if(task.getCommentary() != null) {
            changeableTask.setCommentary(task.getCommentary());
        }
        return taskRepo.save(changeableTask);
    }

    public void deleteTask(String token, TaskModel task) throws UnauthorizedException {
        token = token.substring(7);
        UserModel owner = userRepo.findByToken(token);
        TaskModel changeableTask = taskRepo.findById(task.getId());
        if (owner == null || !Objects.equals(changeableTask.getOwner().getId(), owner.getId())) {
            throw new UnauthorizedException();
        }
        taskRepo.delete(changeableTask);
    }

    public HashMap<String,Object> viewTask(String token) throws UnauthorizedException {
        token = token.substring(7);
        HashMap<String, Object> answer = new HashMap<>();
        UserModel owner = userRepo.findByToken(token);
        if(owner == null) {
            throw new UnauthorizedException();
        }
        int i = 0;
        for (TaskModel t : owner.getTasks()) {
            HashMap<String, Object> task = new HashMap<>();
            task.put("name", t.getName());
            task.put("description", t.getCommentary());
            task.put("id", t.getId());
            task.put("Is Done", t.getDone());
            answer.put(i+"",task);
            i++;
        }
        return answer;
    }

}
