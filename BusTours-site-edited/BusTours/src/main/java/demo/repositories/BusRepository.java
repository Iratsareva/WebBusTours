package demo.repositories;

import demo.models.Bus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository {
    Bus create(Bus bus);
    Bus findById( String id);
    List<Bus> findAll();
    Bus update(Bus bus);
    void delete(Bus bus);
    Bus findBusByTrip (String idTrip);
}
