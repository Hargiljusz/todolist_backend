package com.iwaniuk.todolist_auth.repositories;

import com.iwaniuk.todolist_auth.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role,String> {
    Role findByRole(String role);
}
