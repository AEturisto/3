package com.todow.e.repos;

import com.todow.e.models.TaskModel;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepo extends CrudRepository<TaskModel, Long> {
    TaskModel findById(int Id);
}