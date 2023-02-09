package com.todow.e.controller;

import com.todow.e.exceptions.MalformedInputException;
import com.todow.e.exceptions.UnauthorizedException;
import com.todow.e.models.TaskModel;
import com.todow.e.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity createTask(@RequestHeader("Authorization") String token, @RequestBody TaskModel task) {
        try{
            HashMap<String, Object> answer = new HashMap<>();
            taskService.createTask(token,task);
            answer.put("Message", "Task created");
            return ResponseEntity.ok(answer);

        } catch (MalformedInputException e) {
            HashMap<String, Object> answer = new HashMap<>();
            answer.put("Error", e.getMessage());
            return ResponseEntity.badRequest().body(answer);
        } catch (UnauthorizedException e) {
            HashMap<String, Object> answer = new HashMap<>();
            answer.put("Error", "Unauthorized");
            return ResponseEntity.status(401).body(answer);
        }
    }

    @PostMapping("/edit")
    public ResponseEntity editTask(@RequestHeader("Authorization") String token, @RequestBody TaskModel task) {
        try{
            HashMap<String, Object> answer = new HashMap<>();
            taskService.editTask(token,task);
            answer.put("Message", "Task edited");
            return ResponseEntity.ok(answer);

        } catch (MalformedInputException e) {
            HashMap<String, Object> answer = new HashMap<>();
            answer.put("Error", e.getMessage());
            return ResponseEntity.badRequest().body(answer);
        } catch (UnauthorizedException e) {
            HashMap<String, Object> answer = new HashMap<>();
            answer.put("Error", "Unauthorized");
            return ResponseEntity.status(401).body(answer);
        }
    }

    @PostMapping("/mark")
    public ResponseEntity markTask(@RequestHeader("Authorization") String token, @RequestBody TaskModel task) {
        try{
            HashMap<String, Object> answer = new HashMap<>();
            taskService.markTask(token,task);
            answer.put("Message", "Task edited");
            return ResponseEntity.ok(answer);

        } catch (UnauthorizedException e) {
            HashMap<String, Object> answer = new HashMap<>();
            answer.put("Error", "Unauthorized");
            return ResponseEntity.status(401).body(answer);
        }
    }

    @PostMapping("/view")
    public ResponseEntity viewTask(@RequestHeader("Authorization") String token) {
        try{
            return ResponseEntity.ok(taskService.viewTask(token));
        }   catch (UnauthorizedException e) {
            HashMap<String, Object> answer = new HashMap<>();
            answer.put("Error", "Unauthorized");
            return ResponseEntity.status(401).body(answer);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity deleteTask(@RequestHeader("Authorization") String token, @RequestBody TaskModel task) {
        try{
            HashMap<String, Object> answer = new HashMap<>();
            taskService.deleteTask(token, task);
            answer.put("Message", "Task deleted");
            return ResponseEntity.ok(answer);
        }   catch (UnauthorizedException e) {
            HashMap<String, Object> answer = new HashMap<>();
            answer.put("Error", "Unauthorized");
            return ResponseEntity.status(401).body(answer);
        }
    }

}
