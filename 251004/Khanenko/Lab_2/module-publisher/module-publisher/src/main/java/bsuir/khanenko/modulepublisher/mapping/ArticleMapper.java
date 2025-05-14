package bsuir.khanenko.modulepublisher.mapping;

import bsuir.khanenko.modulepublisher.dto.article.ArticleRequestTo;
import bsuir.khanenko.modulepublisher.dto.article.ArticleResponseTo;
import bsuir.khanenko.modulepublisher.entity.Article;
import bsuir.khanenko.modulepublisher.entity.Label;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "labels", source = "labels", qualifiedByName = "mapStringsToLabels")
    Article toEntity(ArticleRequestTo articleRequestTo);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "labels", source = "labels", qualifiedByName = "mapLabelsToStrings")
    ArticleResponseTo toResponse(Article article);

    @Named("mapStringsToLabels")
    default List<Label> mapStringsToLabels(List<String> labelNames) {
        return labelNames.stream()
                .map(name -> {
                    Label label = new Label();
                    label.setName(name);
                    return label;
                })
                .toList();
    }

    @Named("mapLabelsToStrings")
    default List<String> mapLabelsToStrings(List<Label> labels) {
        return labels.stream()
                .map(Label::getName)
                .toList();
    }
}
