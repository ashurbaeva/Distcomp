package bsuir.khanenko.modulepublisher.service.Impl;

import bsuir.khanenko.modulepublisher.dto.message.MessageRequestTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageResponseTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageUpdate;
import bsuir.khanenko.modulepublisher.entity.Article;
import bsuir.khanenko.modulepublisher.entity.Message;
import bsuir.khanenko.modulepublisher.exceptionHandler.UserNotFoundException;
import bsuir.khanenko.modulepublisher.mapping.MessageMapper;
import bsuir.khanenko.modulepublisher.repository.ArticleRepository;
import bsuir.khanenko.modulepublisher.repository.MessageRepository;
import bsuir.khanenko.modulepublisher.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private ArticleRepository articleRepository;
    private MessageMapper messageMapper;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, MessageMapper messageMapper, ArticleRepository articleRepository) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.articleRepository = articleRepository;
    }

    @Override
    public MessageResponseTo create(MessageRequestTo message) {
        articleRepository.findById(message.getArticleId())
                .orElseThrow(() -> new UserNotFoundException(message.getArticleId()));
        return messageMapper.toResponse(messageRepository.save(messageMapper.toEntity(message)));
    }

    @Override
    public MessageResponseTo update(MessageUpdate updatedMessage) {
        Message message = messageRepository.findById(updatedMessage.getId())
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));

        if (updatedMessage.getId() != null) {
            message.setId(updatedMessage.getId());
        }
        if (updatedMessage.getContent() != null) {
            message.setContent(updatedMessage.getContent());
        }
        if (updatedMessage.getArticleId() != null) {
            Article article = articleRepository.findById(updatedMessage.getArticleId())
                    .orElseThrow(() -> new IllegalArgumentException("Article to update not found"));
            message.setArticle(article);
        }

        return messageMapper.toResponse(messageRepository.save(message));
    }

    @Override
    public void deleteById(Long id) {
        messageRepository.deleteById(id);

    }

    @Override
    public List<MessageResponseTo> findAll() {
        return StreamSupport.stream(messageRepository.findAll().spliterator(), false)
                .map(messageMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<MessageResponseTo> findById(Long id) {
        return messageRepository.findById(id)
                .map(messageMapper::toResponse);
    }
}
