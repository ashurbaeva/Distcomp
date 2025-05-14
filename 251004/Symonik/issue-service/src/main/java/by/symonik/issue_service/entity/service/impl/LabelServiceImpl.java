package by.symonik.issue_service.entity.service.impl;

import by.symonik.issue_service.dto.label.LabelRequestTo;
import by.symonik.issue_service.dto.label.LabelResponseTo;
import by.symonik.issue_service.dto.label.LabelUpdateRequestTo;
import by.symonik.issue_service.entity.Label;
import by.symonik.issue_service.entity.service.LabelService;
import by.symonik.issue_service.exception.LabelNotFoundException;
import by.symonik.issue_service.mapper.LabelMapper;
import by.symonik.issue_service.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    @Override
    @Transactional
    public LabelResponseTo create(LabelRequestTo labelRequestTo) {
        return labelMapper.toLabelResponseTo(
                labelRepository.save(labelMapper.toLabel(labelRequestTo))
        );
    }

    @Override
    @Transactional
    public List<LabelResponseTo> readAll() {
        return labelRepository.findAll().stream()
                .map(labelMapper::toLabelResponseTo)
                .toList();
    }

    @Override
    @Transactional
    public LabelResponseTo readById(Long id) {
        return labelMapper.toLabelResponseTo(
                labelRepository.findById(id).orElseThrow(() -> LabelNotFoundException.byId(id))
        );
    }

    @Override
    @Transactional
    public LabelResponseTo update(LabelUpdateRequestTo labelUpdateRequestTo) {
        long labelId = labelUpdateRequestTo.id();
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> LabelNotFoundException.byId(labelId));

        label.setName(labelUpdateRequestTo.name());

        return labelMapper.toLabelResponseTo(label);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        labelRepository.findById(id)
                .orElseThrow(() -> LabelNotFoundException.byId(id));

        labelRepository.deleteById(id);
    }
}
