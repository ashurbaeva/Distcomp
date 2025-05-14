package com.bsuir.dc.service;

import com.bsuir.dc.dto.request.TweetRequestTo;
import com.bsuir.dc.dto.response.TweetResponseTo;
import com.bsuir.dc.model.Editor;
import com.bsuir.dc.model.Tag;
import com.bsuir.dc.model.Tweet;
import com.bsuir.dc.repository.EditorRepository;
import com.bsuir.dc.repository.TagRepository;
import com.bsuir.dc.repository.TweetRepository;
import com.bsuir.dc.util.exception.EntityNotFoundException;
import com.bsuir.dc.util.mapper.TagMapper;
import com.bsuir.dc.util.mapper.TweetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TweetService {
    private final TweetRepository tweetRepository;
    private final EditorRepository editorRepository;
    private final TweetMapper tweetMapper;
    private final TagMapper tagMapper;
    private final TagRepository tagRepository;

    @Autowired
    public TweetService(TweetRepository tweetRepository, EditorRepository editorRepository, TweetMapper tweetMapper,
                        TagMapper tagMapper, TagRepository tagRepository) {
        this.tweetRepository = tweetRepository;
        this.editorRepository = editorRepository;
        this.tweetMapper = tweetMapper;
        this.tagMapper = tagMapper;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public TweetResponseTo save(TweetRequestTo tweetRequestTo) {
        Tweet tweet = tweetMapper.toTweet(tweetRequestTo);
        setCreator(tweet, tweetRequestTo.getEditorId());
        if (!tweetRequestTo.getTags().isEmpty())
            saveTags(tweet, tweetRequestTo.getTags().stream().map(Tag::new).toList());
        tweet.setCreated(new Date());
        tweet.setModified(new Date());
        return tweetMapper.toTweetResponse(tweetRepository.save(tweet));
    }

    private void saveTags(Tweet tweet, List<Tag> tags){
        Set<String> tagNames = tags.stream().map(Tag::getName).collect(Collectors.toSet());
        List<Tag> existingTags = tagRepository.findByNameIn(tagNames);

        Set<String> existingTagNames = existingTags.stream().map(Tag::getName).collect(Collectors.toSet());
        List<Tag> newTags = tags.stream()
                .filter(tag -> !existingTagNames.contains(tag.getName()))
                .collect(Collectors.toList());

        if (!newTags.isEmpty()) { tagRepository.saveAll(newTags); }
        existingTags.addAll(newTags);
        tweet.setTags(existingTags);

        for (Tag tag : existingTags) {
            tag.getTweets().add(tweet);
        }
    }

    private void setCreator(Tweet tweet, long creatorId){
        Editor editor = editorRepository.findById(creatorId).orElseThrow(() -> new EntityNotFoundException("Author with such id does not exist"));
        tweet.setEditor(editor);
    }

    @Transactional(readOnly = true)
    public List<TweetResponseTo> findAll() {
        return tweetMapper.toTweetResponseList(tweetRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "tweets", key = "#id")
    public TweetResponseTo findById(long id) {
        return tweetMapper.toTweetResponse(
                tweetRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Tweet with such id does not exist")));
    }

    @Transactional
    @CacheEvict(value = "tweets", key = "#tweetRequestTo.id")
    public TweetResponseTo update(TweetRequestTo tweetRequestTo) {
        Tweet tweet = tweetMapper.toTweet(tweetRequestTo);
        Tweet oldTweet = tweetRepository.findById(tweet.getId()).orElseThrow(() -> new EntityNotFoundException("Old tweet not found"));
        Long editorId = tweetRequestTo.getEditorId();

        if (editorId != null) { setCreator(tweet, editorId); }

        tweet.setCreated(oldTweet.getCreated());
        tweet.setModified(new Date());
        return tweetMapper.toTweetResponse(tweetRepository.save(tweet));
    }

    @Transactional
    @CacheEvict(value = "tweets", key = "#id")
    public void deleteById(long id) {
        if (!tweetRepository.existsById(id)) { throw new EntityNotFoundException("Tweet with such id does not exist"); }
        tweetRepository.deleteById(id);
    }

    public boolean existsByTitle(String title){
        return tweetRepository.existsByTitle(title);
    }
    public boolean existsById(Long id){ return tweetRepository.existsById(id); }
}
