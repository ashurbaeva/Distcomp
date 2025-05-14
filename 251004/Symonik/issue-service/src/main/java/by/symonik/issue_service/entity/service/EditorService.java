package by.symonik.issue_service.entity.service;

import by.symonik.issue_service.dto.editor.EditorRequestTo;
import by.symonik.issue_service.dto.editor.EditorResponseTo;
import by.symonik.issue_service.dto.editor.EditorUpdateRequestTo;

import java.util.List;

public interface EditorService {
    EditorResponseTo create(EditorRequestTo editorRequestTo);

    List<EditorResponseTo> readAll();

    EditorResponseTo readById(Long id);

    EditorResponseTo update(EditorUpdateRequestTo editorUpdateRequestTo);

    void deleteById(Long id);
}
