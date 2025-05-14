package com.example.rv1.services;

import com.example.rv1.dto.responseDto.CreatorResponseTo;
import com.example.rv1.dto.requestDto.CreatorRequestTo;
import com.example.rv1.entities.Article;
import com.example.rv1.entities.Creator;
import com.example.rv1.mapper.CreatorMapper;
import com.example.rv1.repositories.CreatorRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class CreatorService {
    @Autowired
    private final CreatorRepository creatorRepository;

    private final CreatorMapper creatorMapper;

    public List<CreatorResponseTo> getAll() {
        // Using JpaRepository's findAll() method
        return creatorRepository.findAll()
                .stream()
                .map(creatorMapper::from)
                .toList();
    }
    @Cacheable(value = "creators", key = "#id")
    public CreatorResponseTo get(long id) {
        // findById returns an Optional that we map to our DTO using the mapper
        return creatorRepository.findById(id)
                .map(creatorMapper::from)
                .orElse(null);
    }
   // @CachePut(value = "creators", key = "#input.id", condition="#input.id!=null")
    @CachePut(value = "creators", key = "#input.id")
    public CreatorResponseTo create(CreatorRequestTo input) {
        Random random = new Random();

        // Map the request DTO to an entity
        Creator creator = creatorMapper.to(input);
        // Save the new entity with JPA and return the DTO via mapping
        Creator savedCreator = creatorRepository.save(creator);

        input.setId(savedCreator.getId());
        return creatorMapper.from(savedCreator);
    }
    @CachePut(value = "creators", key = "#input.id")
    public CreatorResponseTo update(CreatorRequestTo input) {
        // Mapping the request DTO to an entity; for updates, ensure the ID is set
       // Creator creator = creatorMapper.to(input);

        Creator existingCreator = creatorRepository.findById(input.getId()).orElse(null);
        assert existingCreator != null;
        existingCreator.setFirstname(input.getFirstname());
        existingCreator.setLastname(input.getLastname());
        existingCreator.setLogin(input.getLogin());
        existingCreator.setPassword(input.getPassword());

        Creator updatedCreator = creatorRepository.save(existingCreator);
        input.setId(updatedCreator.getId());
        return creatorMapper.from(updatedCreator);
    }
    @CacheEvict(value = "creators", key = "#id")
    public boolean delete(long id) {
        if (creatorRepository.existsById(id)) {
            creatorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
