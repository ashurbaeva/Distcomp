package com.bsuir.dc.util.validator;

import com.bsuir.dc.dto.request.EditorRequestTo;
import com.bsuir.dc.service.EditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EditorValidator implements Validator {
    private final EditorService editorService;

    @Autowired
    public EditorValidator(EditorService editorService) {
        this.editorService = editorService;
    }

    @Override
    public boolean supports(Class<?> clazz) { return EditorRequestTo.class.equals(clazz); }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasFieldErrors()){
            EditorRequestTo creator = (EditorRequestTo) target;
            if (editorService.existsByLogin(creator.getLogin())){
                errors.rejectValue("login", null, "Editor with such login already exists");
            }
        }
    }
}