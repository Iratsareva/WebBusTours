package demo.repos;

import demo.models.Bus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository {
    Bus create(Bus bus);
    Bus findById(Class<Bus> busClass, String id);
    List<Bus> getAll(Class<Bus> busClass);
    Bus update(Bus bus);
    void delete(Bus bus);
    Bus findBusByTrip (String idTrip);




}
