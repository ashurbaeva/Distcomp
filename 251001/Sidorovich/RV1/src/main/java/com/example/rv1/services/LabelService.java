package com.example.rv1.services;

import com.example.rv1.dto.responseDto.LabelResponseTo;
import com.example.rv1.dto.requestDto.LabelRequestTo;
import com.example.rv1.entities.Creator;
import com.example.rv1.entities.Label;
import com.example.rv1.mapper.LabelMapper;
import com.example.rv1.repositories.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LabelService {
    private final LabelRepository labelRepository;
    //private final LabelRepository repository;
    private final LabelMapper labelMapper;

    public List<LabelResponseTo> getAll() {
        return labelRepository
                .findAll()
                .stream()
                .map(labelMapper::from)
                .toList();
    }
    @Cacheable(value = "labels", key = "#id")
    public LabelResponseTo get(long id) {
        return labelRepository
                .findById(id)
                .map(labelMapper::from)
                .orElse(null);
    }   @CachePut(value = "labels", key = "#input.id")
    public LabelResponseTo create(LabelRequestTo input) {
//        return labelRepository
//                .create(labelMapper.to(input))
//                .map(labelMapper::from)
//                .orElseThrow();

        Label label = labelMapper.to(input);
        // Save the new entity with JPA and return the DTO via mapping
        Label savedLabel = labelRepository.save(label);
        //System.out.println("\n\n\n\n\n\n\n\n\n\n\n"+savedLabel+"\n" + labelMapper.from(savedLabel)+"\n\n\n\n\n\n\n\n\n\n\n");
        input.setId(savedLabel.getId());
        return labelMapper.from(savedLabel);
    }
    @CachePut(value = "labels", key = "#input.id")
    public LabelResponseTo update(LabelRequestTo input) {
//        return labelRepository
//                .update(labelMapper.to(input))
//                .map(labelMapper::from)
//                .orElseThrow();

      //  Label label = labelMapper.to(input);
        Label existingLabel = labelRepository.findById(input.getId()).orElse(null);
        assert existingLabel != null;
        existingLabel.setName(input.getName());

        Label updatedLabel = labelRepository.save(existingLabel);
        input.setId(updatedLabel.getId());
        return labelMapper.from(updatedLabel);
    }
    @CacheEvict(value = "labels", key = "#id")
    public boolean delete(long id) {
        if (labelRepository.existsById(id)) {
            labelRepository.deleteById(id);
            return true;
        }
        return false;
    }
}