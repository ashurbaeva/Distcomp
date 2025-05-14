package by.symonik.issue_service.repository;

import by.symonik.issue_service.entity.Editor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EditorRepository extends JpaRepository<Editor, Long> {
    Optional<Editor> findByLogin(String login);
}
