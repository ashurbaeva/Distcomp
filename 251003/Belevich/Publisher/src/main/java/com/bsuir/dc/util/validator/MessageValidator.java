package com.bsuir.dc.util.validator;

import com.bsuir.dc.dto.request.MessageRequestTo;
import com.bsuir.dc.dto.request.TweetRequestTo;
import com.bsuir.dc.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MessageValidator implements Validator {
    private final TweetService tweetService;

    @Autowired
    public MessageValidator(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return TweetRequestTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasFieldErrors()){
            MessageRequestTo message = (MessageRequestTo) target;
            if (!tweetService.existsById(message.getTweetId())){
                errors.rejectValue("tweetId", null, "Such tweetId doesn't exist");
            }
        }
    }
}
