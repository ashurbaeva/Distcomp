package bsuir.khanenko.modulepublisher.repository;

import bsuir.khanenko.modulepublisher.entity.Label;

import java.util.List;
import java.util.Optional;

public interface LabelRepository {
    Label create(Label label);
    Label update(Label updatedLabel);
    void deleteById(Long id);
    List<Label> findAll();
    Optional<Label> findById(Long id);
}