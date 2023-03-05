package com.todow.e.repos;

import org.springframework.data.repository.CrudRepository;
import com.todow.e.models.UserModel;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<UserModel, Long> {
    UserModel findByName(String name);
    UserModel findByToken(String token);
}
