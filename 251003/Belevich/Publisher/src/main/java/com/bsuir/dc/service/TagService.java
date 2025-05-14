package com.bsuir.dc.service;

import com.bsuir.dc.dto.request.TagRequestTo;
import com.bsuir.dc.dto.response.TagResponseTo;
import com.bsuir.dc.model.Tag;
import com.bsuir.dc.model.Tweet;
import com.bsuir.dc.repository.TagRepository;
import com.bsuir.dc.repository.TweetRepository;
import com.bsuir.dc.util.exception.EntityNotFoundException;
import com.bsuir.dc.util.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final TweetRepository tweetRepository;
    private final TagMapper tagMapper;

    @Autowired
    public TagService(TagRepository tagRepository, TweetRepository tweetRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tweetRepository = tweetRepository;
        this.tagMapper = tagMapper;
    }

    public TagResponseTo save(Tag tag, long articleId) {
        Tweet tweet = tweetRepository.findById(articleId).orElseThrow(() -> new EntityNotFoundException("Tweet with such id does not exist"));
        tweet.getTags().add(tag);
        tag.getTweets().add(tweet);
        return tagMapper.toTagResponse(tagRepository.save(tag));
    }

    public TagResponseTo save(TagRequestTo tagRequestTo) {
        Tag tag = tagMapper.toTag(tagRequestTo);
        return tagMapper.toTagResponse(tagRepository.save(tag));
    }

    public List<TagResponseTo> findAll() {
        return tagMapper.toTagResponseList(tagRepository.findAll());
    }

    @CacheEvict(value = "tags", key = "#id")
    public TagResponseTo findById(long id) {
        return tagMapper.toTagResponse(tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag with such id does not exist")));
    }

    @CacheEvict(value = "tags", key = "#id")
    public void deleteById(long id) {
        if (!tagRepository.existsById(id))
            throw new EntityNotFoundException("Tag with such id not found");
        tagRepository.deleteById(id);
    }

    @CacheEvict(value = "tags", key = "#tagRequestTo.id")
    public TagResponseTo update(TagRequestTo tagRequestTo) {
        Tag tag = tagMapper.toTag(tagRequestTo);
        return tagMapper.toTagResponse(tagRepository.save(tag));
    }

    public boolean existsByName(String name) { return tagRepository.existsByName(name); }
}
