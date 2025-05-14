package bsuir.khanenko.modulepublisher.repository;

import bsuir.khanenko.modulepublisher.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface UserRepository extends CrudRepository<User, Long> {
}
