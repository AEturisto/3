package com.todow.e.service;

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

    public HashMap<String, Object> createTask(String token, TaskModel task){
        HashMap<String, Object> answer = new HashMap<>();
        try {
            UserModel user = getUserByToken(token);
            if (user == null) {
                answer.put("code", 401);
                return answer;
            }
            if (task.getName() == null) {
                answer.put("code", 400);
                answer.put("errors", "Name field must not be empty");
                return answer;
            }
            task.setOwner(user);
            task.setCompleted(false);
            taskRepo.save(task);
            answer.put("taskId", task.getId());
            answer.put("name", task.getName());
            answer.put("description", task.getDescription());
            answer.put("mark", task.getCompleted());
            answer.put("code", 201);
            return answer;
        } catch (Exception e){
            answer.put("code", 500);
            return answer;
        }
    }

    public HashMap<String, Object> markTask(String token,String id, TaskModel task){
        HashMap<String, Object> answer = new HashMap<>();
        try {
            if( task == null){
                answer.put("code", 400);
                return answer;
            }
            UserModel user = getUserByToken(token);
            TaskModel changeableTask = taskRepo.findById(id);
            if (authChecks(answer, user, changeableTask).get("code") != null) return answer;
            changeableTask.setCompleted(task.getCompleted());
            taskRepo.save(changeableTask);
            answer.put("completed", changeableTask.getCompleted());
            answer.put("code", 200);
            return answer;
        } catch(Exception e){
            answer.put("code", 500);
            return answer;
        }
    }

    public HashMap<String, Object> editTask(String token,String id, TaskModel task){
        HashMap<String, Object> answer = new HashMap<>();
        try {
            UserModel user = getUserByToken(token);
            TaskModel changeableTask = taskRepo.findById(id);
            if (authChecks(answer, user, changeableTask).get("code") != null) return answer;
            if(task.getName() != null){
                changeableTask.setName(task.getName());
            }
            if(task.getDescription() != null) {
                changeableTask.setDescription(task.getDescription());
            }
            taskRepo.save(changeableTask);
            answer.put("name", changeableTask.getName());
            answer.put("description", changeableTask.getDescription());
            answer.put("code", 200);
            return answer;
        } catch (Exception e){
            answer.put("code", 500);
            return answer;
        }
    }

    public HashMap<String, Object> deleteTask(String token,String id){
        HashMap<String, Object> answer = new HashMap<>();
        try {
            UserModel user = getUserByToken(token);
            TaskModel changeableTask = taskRepo.findById(id);
            if (authChecks(answer, user, changeableTask).get("code") != null) return answer;
            taskRepo.delete(changeableTask);
            answer.put("code", 200);
            return answer;
        } catch(Exception e){
            answer.put("code", 500);
            return answer;
        }
    }

    public HashMap<String, Object> viewTask(String token, String filter){
        HashMap<String, Object> answer = new HashMap<>();
        try {
            if (filter == null){
                filter = "empty";
            } else if (filter.equalsIgnoreCase("false") || filter.equalsIgnoreCase("true")){
            } else {
                filter = "empty";
            }
            UserModel user = getUserByToken(token);
            if (user == null) {
                answer.put("code", 401);
                return answer;
            }
            int i = 0;
            ArrayList tasks = new ArrayList();
            for (TaskModel t : user.getTasks()) {
                if (!(Objects.equals(filter.toLowerCase(), Boolean.toString(t.getCompleted())) || filter.equals("empty"))){
                    continue;
                }
                HashMap<String, Object> task = new HashMap<>();
                task.put("name", t.getName());
                task.put("description", t.getDescription());
                task.put("id", t.getId());
                task.put("completed", t.getCompleted());
                tasks.add(task);
                i++;
            }
            answer.put("items", tasks);
            answer.put("code", 200);
            answer.put("length", i);
            return answer;
        } catch (Exception e){
            answer.put("code", 500);
            return answer;
        }
    }

    private UserModel getUserByToken(String token){
        //Removing "Bearer: " in start of token string. Count of symbols equals 7
        token = token.substring(7);
        return userRepo.findByToken(token);
    }

    private HashMap<String, Object> authChecks(HashMap<String, Object> answer, UserModel user, TaskModel changeableTask) {
        if (changeableTask == null){
            answer.put("code", 401);
            return answer;
        }
        if (user == null || !Objects.equals(changeableTask.getOwner().getId(), user.getId())) {
            answer.put("code", 401);
            return answer;
        }
        return answer;
    }
}
