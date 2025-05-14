package by.ryabchikov.comment_service.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tweet-service", url = "${tweet.service.url}")
public interface TweetClient {

    @GetMapping("/api/v1.0/tweets/{id}")
    TweetResponseTo readById(@PathVariable("id") @Valid @NotNull Long id);
}
