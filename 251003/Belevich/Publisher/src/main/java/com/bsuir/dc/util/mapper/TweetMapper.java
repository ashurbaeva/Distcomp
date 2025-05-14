package com.bsuir.dc.util.mapper;

import com.bsuir.dc.dto.request.TweetRequestTo;
import com.bsuir.dc.dto.response.TweetResponseTo;
import com.bsuir.dc.model.Tweet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TweetMapper {
    @Mapping(target = "editorId", expression = "java(tweet.getEditor().getId())")
    @Mapping(target = "tags", expression = "java(tweet.getTags().stream().map(tag -> tag.getName()).toList())")
    TweetResponseTo toTweetResponse(Tweet tweet);

    List<TweetResponseTo> toTweetResponseList(List<Tweet> tweets);

    @Mapping(target = "tags", ignore = true)
    Tweet toTweet(TweetRequestTo tweetRequestTo);
}
