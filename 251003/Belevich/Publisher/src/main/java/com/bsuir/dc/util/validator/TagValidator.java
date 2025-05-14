package com.bsuir.dc.util.validator;

import com.bsuir.dc.dto.request.TagRequestTo;
import com.bsuir.dc.dto.request.TweetRequestTo;
import com.bsuir.dc.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TagValidator implements Validator {
    private final TagService tagService;

    @Autowired
    public TagValidator(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public boolean supports(Class<?> clazz) { return TweetRequestTo.class.equals(clazz); }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasFieldErrors()){
            TagRequestTo tag = (TagRequestTo) target;
            if (tagService.existsByName(tag.getName())){
                errors.rejectValue("title", null, "Tag with such name already exists");
            }
        }
    }
}
