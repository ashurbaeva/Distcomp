package com.bsuir.dc.controller;

import com.bsuir.dc.dto.request.TagRequestTo;
import com.bsuir.dc.dto.response.TagResponseTo;
import com.bsuir.dc.service.TagService;
import com.bsuir.dc.util.exception.ValidationException;
import com.bsuir.dc.util.validator.TagValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/tags")
public class TagController {
    private final TagService tagService;
    private final TagValidator tagValidator;

    @Autowired
    public TagController(TagService tagService, TagValidator tagValidator) {
        this.tagService = tagService;
        this.tagValidator = tagValidator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponseTo createTag(@RequestBody @Valid TagRequestTo tagRequestTo, BindingResult bindingResult) {
        validate(tagRequestTo, bindingResult);
        return tagService.save(tagRequestTo);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TagResponseTo> getAllTags() { return tagService.findAll(); }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagResponseTo getTagById(@PathVariable Long id) { return tagService.findById(id); }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TagResponseTo updateTag(@RequestBody @Valid TagRequestTo tagRequestTo, BindingResult bindingResult){
        validate(tagRequestTo, bindingResult);
        return tagService.update(tagRequestTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable long id) { tagService.deleteById(id); }

    private void validate(TagRequestTo tagRequestTo, BindingResult bindingResult){
        tagValidator.validate(tagRequestTo, bindingResult);
        if (bindingResult.hasFieldErrors()) { throw new ValidationException(bindingResult); }
    }
}
