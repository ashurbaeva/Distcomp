package bsuir.khanenko.modulepublisher.service;

import bsuir.khanenko.modulepublisher.dto.label.LabelRequestTo;
import bsuir.khanenko.modulepublisher.dto.label.LabelResponseTo;
import bsuir.khanenko.modulepublisher.dto.label.LabelUpdate;

import java.util.List;
import java.util.Optional;

public interface LabelService {
    LabelResponseTo create(LabelRequestTo label);
    LabelResponseTo update(LabelUpdate updatedLabel);
    void deleteById(Long id);
    List<LabelResponseTo> findAll();
    Optional<LabelResponseTo> findById(Long id);
}
