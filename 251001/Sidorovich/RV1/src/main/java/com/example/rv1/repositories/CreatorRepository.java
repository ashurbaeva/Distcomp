package com.example.rv1.repositories;

import com.example.rv1.entities.Article;
import com.example.rv1.entities.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
/*
@org.springframework.stereotype.Repository
public class CreatorRepository implements IRepository<Creator>{
    private final Map<Long, Creator> database = new ConcurrentHashMap<Long, Creator>();
    private static final AtomicLong counter = new AtomicLong();

    public CreatorRepository() {Creator creator = new Creator();
        creator.setId(counter.incrementAndGet());
        creator.setFirstname("Роман");
        creator.setLastname("Сидорович");
        creator.setPassword("SecurePassword");
        creator.setLogin("romasid2004@icloud.com");
        database.put(creator.getId(), new Creator());
    }

    @Override
    public Stream<Creator> getAll() {
        return database.values().stream();
    }

    @Override
    public Optional<Creator> get(long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Optional<Creator> create(Creator input) {
        long id = counter.incrementAndGet();
        input.setId(id);
        database.put(id, input);
        return Optional.of(input);
    }

    @Override
    public Optional<Creator> update(Creator input) {
        database.put(input.getId(), input);
        return Optional.of(input);
    }

    @Override
    public boolean delete(long id) {
        return database.remove(id) != null;
    }
}
*/
@Repository
public interface CreatorRepository extends JpaRepository<Creator,Long> {

}