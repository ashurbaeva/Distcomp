package com.example.rv1.repositories;

import com.example.rv1.entities.Creator;
import com.example.rv1.entities.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
//
//@org.springframework.stereotype.Repository
//public class LabelRepository implements IRepository<Label> {
//    private final Map<Long, Label> database = new ConcurrentHashMap<Long, Label>();
//    private static final AtomicLong counter = new AtomicLong();
//
//    @Override
//    public Stream<Label> getAll() {
//        return database.values().stream();
//    }
//
//    @Override
//    public Optional<Label> get(long id) {
//        return Optional.ofNullable(database.get(id));
//    }
//
//    @Override
//    public Optional<Label> create(Label input) {
//        long id = counter.incrementAndGet();
//        input.setId(id);
//        database.put(id, input);
//        return Optional.of(input);
//    }
//
//    @Override
//    public Optional<Label> update(Label input) {
//        database.put(input.getId(), input);
//        return Optional.of(input);
//    }
//
//    @Override
//    public boolean delete(long id) {
//        return database.remove(id) != null;
//    }
//}

@Repository
public interface LabelRepository extends JpaRepository<Label,Long> {

}