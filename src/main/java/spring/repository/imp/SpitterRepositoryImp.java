package spring.repository.imp;

import spring.data.Spitter;

public interface SpitterRepositoryImp {
    Spitter save(Spitter spitter);
    Spitter findByUsername(String username);
}