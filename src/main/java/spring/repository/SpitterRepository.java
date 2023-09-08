package spring.repository;

import org.springframework.stereotype.Repository;
import spring.data.Spitter;
import spring.repository.imp.SpitterRepositoryImp;

@Repository
public class SpitterRepository implements SpitterRepositoryImp {
    @Override
    public Spitter save(Spitter spitter) {
        return null;
    }

    @Override
    public Spitter findByUsername(String username) {
        return null;
    }
}
