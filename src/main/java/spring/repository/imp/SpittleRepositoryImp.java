package spring.repository.imp;

import spring.data.Spittle;

import java.util.List;

public interface SpittleRepositoryImp {
    List<Spittle> findSpittles(long max, int count);
    Spittle findOne(long spittle_id);
}
