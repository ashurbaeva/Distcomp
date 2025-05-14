package com.bsuir.dc.repository;

import com.bsuir.dc.model.Editor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorRepository extends JpaRepository<Editor, Long> {
    boolean existsByLogin(String login);
}