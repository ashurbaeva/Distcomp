package com.bsuir.dc.repository;

import com.bsuir.dc.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName(String name);
    List<Tag> findByNameIn(Set<String> names);
}
