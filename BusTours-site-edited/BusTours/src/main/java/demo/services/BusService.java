package demo.services;

import demo.dto.BusDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface BusService {
    BusDto createBus(String mark, String numberBus, Integer numberSeats, String classBus, LocalDate year);
    void updateBus(String id, String mark, String numberBus, Integer numberSeats, String classBus, LocalDate year);
    BusDto getBus(String id);
    void deleteBus(String id);

    Page<BusDto> getBus( int page, int size);

    List<BusDto> findAll();

}
