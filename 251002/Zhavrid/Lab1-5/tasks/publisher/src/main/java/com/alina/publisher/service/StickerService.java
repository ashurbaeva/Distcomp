package com.alina.publisher.service;

import com.alina.publisher.exception.GlobalException;
import com.alina.publisher.dto.StickerCreateDTO;
import com.alina.publisher.dto.StickerDTO;
import com.alina.publisher.dto.StickerUpdateDTO;
import com.alina.publisher.mapper.StickerMapper;
import com.alina.publisher.model.Sticker;
import com.alina.publisher.model.Tweet;
import com.alina.publisher.repository.StickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StickerService {
    @Autowired
    StickerRepository stickerRepository;
    @Autowired
    StickerMapper stickerMapper;

    public StickerDTO getSticker(Long id){
        return stickerMapper.toStickerDTO(stickerRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Sticker not found with id: "+id)));
    }
    public List<StickerDTO>  getAllStickers(){
        List<Sticker> list=new ArrayList<>();
        stickerRepository.findAll().forEach(list::add);
        return stickerMapper.toStickerDTOList(list);
    }
    public List<Sticker>  getStickers(){

        return stickerRepository.findAll();
    }
    public StickerDTO createSticker(StickerCreateDTO stickerCreateDTO){
        if(stickerCreateDTO.name().length()<2||stickerCreateDTO.name().length()>32) {
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");
        }
        return stickerMapper.toStickerDTO(stickerRepository.save(stickerMapper.toSticker(stickerCreateDTO)));
    }
    public StickerDTO updateSticker(StickerUpdateDTO stickerUpdateDTO){
        if(stickerUpdateDTO.name().length()<2||stickerUpdateDTO.name().length()>32) {
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");
        }
        return stickerMapper.toStickerDTO(stickerRepository.save(stickerMapper.toSticker(stickerUpdateDTO)));
    }
    public void deleteSticker(Long id) {
        if (stickerRepository.existsById(id))
            stickerRepository.deleteById(id);
        else throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");
    }
    public void addTweetToSticker(Long stickerId, Tweet tweet) {

        Sticker sticker = stickerRepository.findById(stickerId)
                .orElseThrow(() -> new RuntimeException("Стикер не найден"));

        sticker.getTweets().add(tweet);

        stickerRepository.save(sticker);
    }
}
