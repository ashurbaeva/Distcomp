package by.symonik.issue_service.entity.service.impl;

import by.symonik.issue_service.dto.editor.EditorRequestTo;
import by.symonik.issue_service.dto.editor.EditorResponseTo;
import by.symonik.issue_service.dto.editor.EditorUpdateRequestTo;
import by.symonik.issue_service.entity.Editor;
import by.symonik.issue_service.exception.EditorLoginDuplicationException;
import by.symonik.issue_service.exception.EditorNotFoundException;
import by.symonik.issue_service.mapper.EditorMapper;
import by.symonik.issue_service.repository.EditorRepository;
import by.symonik.issue_service.entity.service.EditorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EditorServiceImpl implements EditorService {
    private final EditorRepository editorRepository;
    private final EditorMapper editorMapper;

    private void checkOnLoginDuplication(String login) {
        Optional<Editor> optionalEditor = editorRepository.findByLogin(login);
        if (optionalEditor.isPresent()) {
            throw EditorLoginDuplicationException.byLogin(login);
        }
    }
    @Override
    @Transactional
    public EditorResponseTo create(EditorRequestTo editorRequestTo) {
        checkOnLoginDuplication(editorRequestTo.login());
        return editorMapper.toEditorResponseTo(
                editorRepository.save(editorMapper.toEditor(editorRequestTo))
        );
    }

    @Override
    @Transactional
    public List<EditorResponseTo> readAll() {
        return editorRepository.findAll().stream()
                .map(editorMapper::toEditorResponseTo)
                .toList();
    }

    @Override
    @Transactional
    public EditorResponseTo readById(Long id) {
        return editorMapper.toEditorResponseTo(
                editorRepository.findById(id).orElseThrow(() -> EditorNotFoundException.byId(id))
        );
    }

    @Override
    @Transactional
    public EditorResponseTo update(EditorUpdateRequestTo editorUpdateRequestTo) {
        long editorId = editorUpdateRequestTo.id();
        Editor editor = editorRepository.findById(editorId)
                .orElseThrow(() -> EditorNotFoundException.byId(editorId));

        editor.setLogin(editorUpdateRequestTo.login());
        editor.setPassword(editorUpdateRequestTo.password());
        editor.setFirstname(editorUpdateRequestTo.firstname());
        editor.setLastname(editorUpdateRequestTo.lastname());

        return editorMapper.toEditorResponseTo(editor);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        editorRepository.findById(id)
                .orElseThrow(() -> EditorNotFoundException.byId(id));

        editorRepository.deleteById(id);
    }
}
