package com.alina.discussion.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Named("MapperUtil")
@Component
@RequiredArgsConstructor
public class MapperUtil {

//
//    @Named("getTweetId")
//    public Long getTweetId(Long tweetId){
//        if(restClient
//                .get()
//                .uri("http://localhost:24110/api/v1.0/tweets/{id}",tweetId)
//                .retrieve().toEntity(TweetDTO.class).getStatusCode().is2xxSuccessful()){
//            return tweetId;
//        }
//        else{
//           throw new EntityNotFoundException("Tweet not found with id: " + tweetId);
//
//        }
//    }

}
