package bsuir.khanenko.modulepublisher.service;

import bsuir.khanenko.modulepublisher.dto.label.LabelRequestTo;
import bsuir.khanenko.modulepublisher.dto.label.LabelResponseTo;
import bsuir.khanenko.modulepublisher.dto.label.LabelUpdate;
import bsuir.khanenko.modulepublisher.entity.Label;
import bsuir.khanenko.modulepublisher.mapping.LabelMapper;
import bsuir.khanenko.modulepublisher.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LabelServiceImplm implements LabelService {

    private LabelRepository labelRepository;
    private LabelMapper labelMapper;

    @Autowired
    public LabelServiceImplm(LabelRepository labelRepository, LabelMapper labelMapper) {
        this.labelRepository = labelRepository;
        this.labelMapper = labelMapper;
    }

    @Override
    public LabelResponseTo create(LabelRequestTo label) {
        return labelMapper.toResponse(labelRepository.create(labelMapper.toEntity(label)));

    }

    @Override
    public LabelResponseTo update(LabelUpdate updatedLabel) {
        Label label = labelRepository.findById(updatedLabel.getId())
                .orElseThrow(() -> new IllegalArgumentException("Label not found"));

        if (updatedLabel.getId() != null) {
            label.setId(updatedLabel.getId());
        }
        if (updatedLabel.getName() != null) {
            label.setName(updatedLabel.getName());
        }

        return labelMapper.toResponse(labelRepository.update(label));
    }

    @Override
    public void deleteById(Long id) {
        labelRepository.deleteById(id);
    }

    @Override
    public List<LabelResponseTo> findAll() {
        return labelRepository.findAll()
                .stream()
                .map(labelMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<LabelResponseTo> findById(Long id) {
        return labelRepository.findById(id)
                .map(labelMapper::toResponse);
    }
}