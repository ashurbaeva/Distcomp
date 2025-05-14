package com.example.discussion;



//import com.example.rv1.repositories.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;


@Service
@AllArgsConstructor
public class service {
    @Autowired
    private final RepositoryC commentRepository;
    // private final LabelRepository repository;

    @Qualifier("mapperC")
    private final MapperC commentMapperC;
    // private final ArticleRepository articleRepository;

    public List<CommentResponse> getAll() {

        System.out.println("getall");
        return commentRepository
                .findAll()
                .stream()
                .map(commentMapperC::from)
                .toList();
    }
    public CommentResponse get(Long id) {

        System.out.println("get");
        return commentRepository
                .findById(id)
                .map(commentMapperC::from)
                .orElse(null);
    }

    public CommentResponse create(CommentRequest input) {

System.out.println("Create");
//        System.out.println(input.getId());
//        System.out.println(input.getArticleId());
//        System.out.println(input.getContent());
//        System.out.println(input.getClass());

/// ///////////-------------------------------------------------------------------------------------------------------------------------
//        if (!articleRepository.findById(input.getArticleId()).isPresent()) {
//            throw new NoSuchElementException("Creator with ID " + input.getArticleId() + " not found");
//        }

        CommentEntity comment = commentMapperC.to(input);
        if (input.getId() == null){
            System.out.println("\n\n\nid is null\n\n\n");
            Random random = new Random();
            long id = random.nextInt(999999999 - 1000) + 1000;

            comment.setId(id);
        }
        CommentEntity savedComment = commentRepository.save(comment);
      //  System.out.println(savedComment.getId());
      //  System.out.println(savedComment.getArticleId());
      //  System.out.println(savedComment.getContent());
      //  System.out.println(savedComment.getClass()+"\n");

        var a = commentMapperC.from(savedComment);
        System.out.println(a.getArticleId());

        return commentMapperC.from(savedComment);
    }
    public CommentResponse update(CommentRequest input) {


        System.out.println("update");
        CommentEntity existingComment = commentRepository.findById(input.getId()).orElse(null);


        assert existingComment != null;
        existingComment.setArticleId(input.getArticleId());
        existingComment.setContent(input.getContent());
        CommentEntity updatedComment = commentRepository.save(existingComment);

        return commentMapperC.from(updatedComment);
    }
    public boolean delete(Long id) {

        System.out.println("delete");
        //return commentRepository.delete(id);

        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}