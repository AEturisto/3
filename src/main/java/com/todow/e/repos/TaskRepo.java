package com.todow.e.repos;

import com.todow.e.models.TaskModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends CrudRepository<TaskModel, Long> {
    TaskModel findById(String Id);
}