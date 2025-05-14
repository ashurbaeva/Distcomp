package com.bsuir.dc.service;

import com.bsuir.dc.dto.request.EditorRequestTo;
import com.bsuir.dc.dto.response.EditorResponseTo;
import com.bsuir.dc.model.Editor;
import com.bsuir.dc.repository.EditorRepository;
import com.bsuir.dc.util.exception.EntityNotFoundException;
import com.bsuir.dc.util.mapper.EditorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EditorService {
    private final EditorRepository editorRepository;
    private final EditorMapper editorMapper;

    @Autowired
    public EditorService(EditorRepository editorRepository, EditorMapper editorMapper) {
        this.editorRepository = editorRepository;
        this.editorMapper = editorMapper;
    }

    @Transactional
    public EditorResponseTo save(EditorRequestTo editorRequestTo) {
        Editor editor = editorMapper.toEditor(editorRequestTo);
        return editorMapper.toEditorResponse(editorRepository.save(editor));
    }

    @Transactional(readOnly = true)
    public List<EditorResponseTo> findAll() { return editorMapper.toEditorResponseList(editorRepository.findAll()); }

    @Cacheable(value = "editors", key = "#id")
    @Transactional(readOnly = true)
    public EditorResponseTo findById(long id){
        Editor editor = editorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Editor not found"));
        return editorMapper.toEditorResponse(editor);
    }

    @CacheEvict(value = "editors", key = "#id")
    @Transactional
    public EditorResponseTo update(long id, EditorRequestTo editorRequestTo) {
        editorRequestTo.setId(id);
        return update(editorRequestTo);
    }

    @CacheEvict(value = "editors", key = "#editorRequestTo.id")
    @Transactional
    public EditorResponseTo update(EditorRequestTo editorRequestTo) {
        Editor editor = editorMapper.toEditor(editorRequestTo);
        if (!editorRepository.existsById(editor.getId())) { throw new EntityNotFoundException("Editor not found"); }
        return editorMapper.toEditorResponse(editorRepository.save(editor));
    }

    @CacheEvict(value = "editors", key = "#id")
    @Transactional
    public void deleteById(long id) {
        if (!editorRepository.existsById(id)) { throw new EntityNotFoundException("Editor not found"); }
        editorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByLogin(String login){ return editorRepository.existsByLogin(login); }
}
