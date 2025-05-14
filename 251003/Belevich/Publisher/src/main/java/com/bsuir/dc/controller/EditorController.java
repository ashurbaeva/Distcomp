package com.bsuir.dc.controller;

import com.bsuir.dc.dto.request.EditorRequestTo;
import com.bsuir.dc.dto.response.EditorResponseTo;
import com.bsuir.dc.service.EditorService;
import com.bsuir.dc.util.exception.ValidationException;
import com.bsuir.dc.util.validator.EditorValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/editors")
public class EditorController {
    private final EditorService editorService;
    private final EditorValidator editorValidator;

    @Autowired
    public EditorController(EditorService editorService, EditorValidator editorValidator) {
        this.editorService = editorService;
        this.editorValidator = editorValidator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EditorResponseTo createEditor(@RequestBody @Valid EditorRequestTo editorRequestTo, BindingResult bindingResult)
            throws MethodArgumentNotValidException {
        validate(editorRequestTo, bindingResult);
        return editorService.save(editorRequestTo);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EditorResponseTo> getAllEditors() { return editorService.findAll(); }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EditorResponseTo getEditorById(@PathVariable long id) { return editorService.findById(id);}

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public EditorResponseTo updateEditor(@RequestBody @Valid EditorRequestTo editorRequestTo, BindingResult bindingResult){
        validate(editorRequestTo, bindingResult);
        return editorService.update(editorRequestTo);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Long updateCreator(@PathVariable Long id, @RequestBody @Valid EditorRequestTo editorRequestTo, BindingResult bindingResult){
        validate(editorRequestTo, bindingResult);
        return editorService.update(id, editorRequestTo).getId();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEditor(@PathVariable long id) { editorService.deleteById(id); }

    private void validate(EditorRequestTo editorRequestTo, BindingResult bindingResult){
        editorValidator.validate(editorRequestTo, bindingResult);
        if (bindingResult.hasFieldErrors()) { throw new ValidationException(bindingResult); }
    }
}
