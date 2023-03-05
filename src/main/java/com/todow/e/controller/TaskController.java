package com.todow.e.controller;

import com.todow.e.models.TaskModel;
import com.todow.e.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("")
    public ResponseEntity createTask(@RequestHeader("Authorization") String token, @RequestBody TaskModel task) {
            HashMap<String, Object> answer = taskService.createTask(token,task);
            return ResponseEntity.status(HttpStatus.valueOf((Integer) answer.get("code"))).body(answer);
    }

    @PatchMapping("/{id}")
    public ResponseEntity editTask(@RequestHeader("Authorization") String token, @PathVariable String id, @RequestBody TaskModel task) {
        HashMap<String, Object> answer = taskService.editTask(token,id,task);
        return ResponseEntity.status(HttpStatus.valueOf((Integer) answer.get("code"))).body(answer);
    }

    @GetMapping("")
    public ResponseEntity viewTasks(@RequestHeader("Authorization") String token, @RequestParam(required = false) String filter) {
        HashMap<String, Object> answer = taskService.viewTask(token,filter);
        return ResponseEntity.status(HttpStatus.valueOf((Integer) answer.get("code"))).body(answer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTask(@RequestHeader("Authorization") String token, @PathVariable String id) {
        HashMap<String, Object> answer = taskService.deleteTask(token, id);
        return ResponseEntity.status(HttpStatus.valueOf((Integer) answer.get("code"))).body(answer);
    }

    @PutMapping("/{id}/completed")
    public ResponseEntity markTask(@RequestHeader("Authorization") String token, @PathVariable String id, @RequestBody TaskModel task){
        HashMap<String, Object> answer = taskService.markTask(token, id, task);
        return ResponseEntity.status(HttpStatus.valueOf((Integer) answer.get("code"))).body(answer);
    }

}
