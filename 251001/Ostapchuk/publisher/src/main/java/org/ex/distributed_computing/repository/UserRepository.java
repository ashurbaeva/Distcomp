package org.ex.distributed_computing.repository;

import java.util.List;
import org.ex.distributed_computing.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  List<User> findAll();

  boolean existsByLogin(String login);
}
