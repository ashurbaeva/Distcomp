package com.bsuir.dc.controller;

import com.bsuir.dc.dto.request.TweetRequestTo;
import com.bsuir.dc.dto.response.TweetResponseTo;
import com.bsuir.dc.service.TweetService;
import com.bsuir.dc.util.validator.TweetValidator;
import com.bsuir.dc.util.exception.ValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetController {
    private final TweetService tweetService;
    private final TweetValidator tweetValidator;

    @Autowired
    public TweetController(TweetService tweetService, TweetValidator tweetValidator) {
        this.tweetService = tweetService;
        this.tweetValidator = tweetValidator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponseTo createTweet(@RequestBody @Valid TweetRequestTo tweetRequestTo, BindingResult bindingResult) {
        validate(tweetRequestTo, bindingResult);
        return tweetService.save(tweetRequestTo);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TweetResponseTo> getAllTweets() { return tweetService.findAll(); }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TweetResponseTo getTweetById(@PathVariable Long id) { return tweetService.findById(id); }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TweetResponseTo updateTweet(@RequestBody @Valid TweetRequestTo tweetRequestTo, BindingResult bindingResult){
        validate(tweetRequestTo, bindingResult);
        return tweetService.update(tweetRequestTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTweet(@PathVariable long id){ tweetService.deleteById(id); }

    private void validate(TweetRequestTo tweetRequestTo, BindingResult bindingResult){
        tweetValidator.validate(tweetRequestTo, bindingResult);
        if (bindingResult.hasFieldErrors()){
            throw new ValidationException(bindingResult);
        }
    }
}
