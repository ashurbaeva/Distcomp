package com.bsuir.dc.util.validator;

import com.bsuir.dc.dto.request.TweetRequestTo;
import com.bsuir.dc.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TweetValidator implements Validator {
    private final TweetService tweetService;

    @Autowired
    public TweetValidator(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @Override
    public boolean supports(Class<?> clazz) { return TweetRequestTo.class.equals(clazz); }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasFieldErrors()){
            TweetRequestTo topic = (TweetRequestTo) target;
            if (tweetService.existsByTitle(topic.getTitle())){
                errors.rejectValue("title", null, "Tweet with such title already exists");
            }
        }
    }
}
