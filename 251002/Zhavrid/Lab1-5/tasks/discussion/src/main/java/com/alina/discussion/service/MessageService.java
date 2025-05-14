package com.alina.discussion.service;

import com.alina.discussion.mapper.MessageMapper;
import com.alina.discussion.dto.MessageCreateDTO;
import com.alina.discussion.dto.MessageDTO;
import com.alina.discussion.dto.MessageUpdateDTO;
import com.alina.discussion.exception.GlobalException;
import com.alina.discussion.model.Message;
import com.alina.discussion.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;


@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MessageMapper messageMapper;

    @Autowired
    private IdGeneratorService idGeneratorService;
    public MessageDTO getMessage(Long id){
        return messageMapper.toMessageDTO(messageRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Message not found with id: "+id)));
    }

    public List<MessageDTO> getAllMessages(){
        List<MessageDTO> list=new ArrayList<>();
        messageRepository.findAll().forEach(x->list.add(messageMapper.toMessageDTO(x)));
        return list;
    }

    public MessageDTO createMessage(MessageCreateDTO messageDTO){
        if(messageDTO.content().length()<2||messageDTO.content().length()>2048) {
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");

        }
        Message msg = messageMapper.toMessage(messageDTO);
        Long newId = idGeneratorService.getNextId();
        msg.setId(newId);
        return messageMapper.toMessageDTO(messageRepository.save(msg));
    }


    public MessageDTO updateMessage(MessageUpdateDTO messageDTO){

        if(messageDTO.content().length()<2||messageDTO.content().length()>2048) {
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");

        }
       Message message = messageMapper.toMessage(messageDTO);
            Message old = messageRepository.findById(message.getId()).orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + message.getId()));
            return messageMapper.toMessageDTO(messageRepository.save(messageMapper.toMessage(messageDTO)));

    }

    public void deleteMessage(Long id){
        messageRepository.deleteById(id);
    }


    public MessageDTO createMessageExchange(MessageCreateDTO messageDTO){
        if(messageDTO.content().length()<2||messageDTO.content().length()>2048) {
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");

        }
        return messageMapper.toMessageDTO(messageRepository.save(messageMapper.toMessage(messageDTO)));
    }

    public MessageDTO updateMessageExchange(MessageUpdateDTO messageDTO){

        if(messageDTO.content().length()<2||messageDTO.content().length()>2048) {
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");

        }
        Message message=messageMapper.toMessage(messageDTO);
        Message old= messageRepository.findById(message.getId()).orElseThrow(()->new EntityNotFoundException("Message not found with id: "+message.getId()));
        return messageMapper.toMessageDTO(messageRepository.save(messageMapper.toMessage(messageDTO)));
    }



}
